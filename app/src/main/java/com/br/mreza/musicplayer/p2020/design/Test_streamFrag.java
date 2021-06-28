package com.br.mreza.musicplayer.p2020.design;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioPlaybackCaptureConfiguration;
import android.media.AudioPlaybackConfiguration;
import android.media.AudioTrack;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.media.AudioAttributesCompat;

import com.br.mreza.musicplayer.R;
import com.br.mreza.musicplayer.newdesign.newmodelfrags.MediaDecoder;
import com.mbn.nativeaudio.Control;

import java.io.IOException;
import java.util.List;

import mbn.libs.fragmanager.BaseFragment;

public class Test_streamFrag extends BaseFragment {


    private AudioTrack audioTrack;
    private HandlerThread handlerThread;
    private Handler uiHandler;
    private MediaDecoder mediaDecoder;
    private float speed = 1;

    public static Test_streamFrag newInstance(String fPath) {

        Bundle args = new Bundle();
        args.putString("F_PATH", fPath);
        Test_streamFrag fragment = new Test_streamFrag();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateViewForChild(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.slow_play_layout, container, false);
    }

    @Override
    public void makeState(Bundle state) {

    }

    @Override
    public void checkForRestoreState() {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final TextView speedText = (TextView) findViewById(R.id.speed_text);
        SeekBar seekBar = (SeekBar) findViewById(R.id.speed_seekbar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                speed = (seekBar.getProgress() / 20f) + 0.5f;
                speedText.setText("Speed: " + speed);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                speed = (seekBar.getProgress() / 20f) + 0.5f;
                speedText.setText("Speed: " + speed);
            }
        });

        findViewById(R.id.play_btn).setOnClickListener(v -> {
            try {

//            AudioManager manager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                manager.registerAudioPlaybackCallback(new AudioManager.AudioPlaybackCallback() {
//                    @Override
//                    public void onPlaybackConfigChanged(List<AudioPlaybackConfiguration> configs) {
//                        super.onPlaybackConfigChanged(configs);
//                        configs.get(0).
//                    }
//                }, uiHandler);
//            }

//            MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) getContext().getSystemService(Context.MEDIA_PROJECTION_SERVICE);
//            mediaProjectionManager.getMediaProjection(96199, mediaProjectionManager.createScreenCaptureIntent()).registerCallback();
//            AudioPlaybackCaptureConfiguration.Builder captureConfigBuilder = new AudioPlaybackCaptureConfiguration.Builder(null);


                handlerThread = new HandlerThread("mbn_audioRead");
                handlerThread.start();
                uiHandler = new Handler(handlerThread.getLooper());
                mediaDecoder = new MediaDecoder(getArguments().getString("F_PATH"));
//            AudioAttributesCompat.Builder audioAttributesCompatBuilder = new AudioAttributesCompat.Builder();
//            audioAttributesCompatBuilder.setUsage(AudioAttributesCompat.USAGE_MEDIA).setLegacyStreamType(AudioManager.STREAM_MUSIC);
//            AudioFormat.Builder formatBuilder = new AudioFormat.Builder();
//            formatBuilder.setEncoding(AudioFormat.ENCODING_PCM_16BIT).setSampleRate(mediaDecoder.getSampleRate()).setChannelMask(AudioFormat.CHANNEL_OUT_STEREO);
//            AudioTrack.Builder builder = new AudioTrack.Builder();
//            builder.setAudioFormat(formatBuilder.build()).setTransferMode(AudioTrack.MODE_STREAM).setBufferSizeInBytes(AudioTrack.getMinBufferSize(mediaDecoder.getSampleRate(),
//                    AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT) * 100).setAudioAttributes((AudioAttributes) audioAttributesCompatBuilder.build().unwrap());
//            audioTrack = builder.build();
//            audioTrack.play();
                Control.stopEngine();
                Control.startEngine(mediaDecoder.getSampleRate(), speed);
                uiHandler.post(readRunnable);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });


//        int ignoreRate = mediaDecoder.getSampleRate() > 10 ? mediaDecoder.getSampleRate() / 100 : 1;
//        ignoreRate--;


    }

    private final Runnable readRunnable = new Runnable() {
        @Override
        public void run() {
            short[] data = mediaDecoder.readShortData();
            if (data != null) {
//                Log.i("MBN_STREAM", "before: " + System.currentTimeMillis());
                try {
//                    audioTrack.write(data, 0, data.length);
                    Control.loadData(data, data.length);
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                Log.i("MBN_STREAM", "after: " + System.currentTimeMillis());

                uiHandler.post(this);
            }
        }
    };

    @Override
    public void onStop() {
        super.onStop();
//        audioTrack.release();
        uiHandler.removeCallbacks(readRunnable);
        handlerThread.quitSafely();
        Control.stopEngine();

    }
}
