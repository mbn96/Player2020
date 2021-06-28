package com.br.mreza.musicplayer.p2020.visualizer;

import android.media.audiofx.Visualizer;
import android.util.Log;

import java.util.ArrayList;

import mbn.libs.utils.BatchFunctionList;

public class VisualizerManager implements Visualizer.OnDataCaptureListener {
    public static final VisualizerManager INSTANCE = new VisualizerManager();

    private static final String TAG = "VManager";

    public static void addListener(VisualizerListener listener) {
        if (!INSTANCE.listeners.contains(listener)) {
            INSTANCE.listeners.add(listener);
        }
        INSTANCE.checkForActivation();
    }

    public static void removeListener(VisualizerListener listener) {
        INSTANCE.listeners.remove(listener);
        if (INSTANCE.listeners.size() == 0) {
            INSTANCE.releaseVisualizer();
        }
    }

    //---------------- Fields ------------//
    private boolean activeSession = false;
    private int sessionID;
    private Visualizer visualizer;
    private boolean isVisualizerActive = false;
    private BatchFunctionList<VisualizerListener> listeners = new BatchFunctionList<>();
    private BatchF batchFunction = new BatchF();

    private VisualizerManager() {
    }

    private boolean createVisualizer() {
        boolean done = false;
        try {
            visualizer = new Visualizer(sessionID);
            visualizer.setMeasurementMode(Visualizer.MEASUREMENT_MODE_PEAK_RMS);
            visualizer.setScalingMode(Visualizer.SCALING_MODE_NORMALIZED);

            // Set the size of the byte array returned for visualization
            visualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
            // Whenever audio data is available, update the visualizer view
            visualizer.setDataCaptureListener(this, Visualizer.getMaxCaptureRate(), false, true);
            visualizer.setEnabled(true);

            isVisualizerActive = true;
            done = true;
        } catch (Exception ignored) {
            if (visualizer != null) visualizer.release();
            visualizer = null;
            isVisualizerActive = false;
        }
        return done;
    }

    private void start() {
        int tries = 0;
        while (!createVisualizer() && tries < 5) {
            tries++;
        }
    }

    public void setSession(boolean active, int id) {
        if (active) {
            sessionID = id;
            activeSession = true;
            checkForActivation();
        } else {
            activeSession = false;
            releaseVisualizer();
        }

    }

    private void checkForActivation() {
        if (activeSession && (!isVisualizerActive) && listeners.size() > 0) start();
    }

    private void releaseVisualizer() {
        if (visualizer != null) {
            visualizer.release();
            visualizer = null;
        }
        isVisualizerActive = false;
    }

    //--------------------------- Classes ---------------------------//

    @Override
    public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
        batchFunction.setData(waveform, samplingRate);
        listeners.batch(batchFunction);
    }

    @Override
    public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {
        batchFunction.setData(fft, samplingRate);
        listeners.batch(batchFunction);
    }

    public interface VisualizerListener {
        void onData(byte[] bytes, int samplingRate);
    }

    private class BatchF implements BatchFunctionList.BatchFunction<VisualizerListener> {
        private byte[] bytes;
        private int samplingRate;

        void setData(byte[] bytes, int samplingRate) {
            this.bytes = bytes;
            this.samplingRate = samplingRate;
        }

        @Override
        public void function(VisualizerListener element) {
            element.onData(bytes, samplingRate);
        }
    }

}
