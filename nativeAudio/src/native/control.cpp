#include <jni.h>
#include "audioEngine.h"
#include <android/log.h>

//
// Created by Mreza on 2/14/2021.
//

AudioEngine *audioEngine;

extern "C"
JNIEXPORT void JNICALL
Java_com_mbn_nativeaudio_Control_startEngine(JNIEnv *env, jclass clazz, jint sampling_rate,
                                             jfloat speed) {
    if (audioEngine == nullptr) {
        audioEngine = new AudioEngine();
        audioEngine->setSpeed(speed);
        audioEngine->start(sampling_rate);
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_com_mbn_nativeaudio_Control_stopEngine(JNIEnv *env, jclass clazz) {
    if (audioEngine != nullptr) {
        audioEngine->stop();
        delete audioEngine;
        audioEngine = nullptr;
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_com_mbn_nativeaudio_Control_loadData(JNIEnv *env, jclass clazz, jshortArray data, jint len) {
    auto *buff = env->GetShortArrayElements(data, nullptr);
    env->ReleaseShortArrayElements(data, buff, JNI_COMMIT);
    audioEngine->pushData(buff, len);
}