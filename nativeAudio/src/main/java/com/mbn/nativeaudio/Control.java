package com.mbn.nativeaudio;

public class Control {

    static {
        System.loadLibrary("native-lib");
    }

    public static native void startEngine(int samplingRate, float speed);

    public static native void stopEngine();

    public static native void loadData(short[] data, int len);
}
