//
// Created by Mreza on 11/21/2020.
//

#include "oscillator.h"
#include <cmath>

#define TWO_PI (3.14159 * 2)
//#define AMPLITUDE 0.3
#define FREQUENCY 600

#define TAG "NATIVE_LOG"


static float amplFactor{0};

void Oscillator::setSampleRate(int32_t sampleRate) {
    sampleRate_ = sampleRate;
    phaseIncrement_ = (TWO_PI * freq_) / (double) sampleRate;
    phaseIncrement_2 = (TWO_PI * freq_2) / (double) sampleRate;
    phaseIncrement_3 = (TWO_PI * freq_3) / (double) sampleRate;
}

void Oscillator::setWaveOn(bool isWaveOn) {
    isWaveOn_.store(isWaveOn);
}

void Oscillator::setTune(float amp, float freq) {
    amp_ = amp * 2;
    freq_ = FREQUENCY * freq;
    freq_2 = freq_ + 80;
    freq_3 = freq_ + 110;
    setSampleRate(sampleRate_);
}

void Oscillator::render(int16_t *audioData, int32_t numFrames) {

//    if (!isWaveOn_.load()) phase_ = 0;
    int j;
    for (int i = 0; i < numFrames; i++) {
        j = 2 * i;
//        __android_log_print(ANDROID_LOG_INFO, TAG, "render: %s", "Before select");

        if (selected_buff.len <= 0) {
            selectBuff();
        }
//        __android_log_print(ANDROID_LOG_INFO, TAG, "render: %s", "Before read");

        if (selected_buff.buff == nullptr) {
            audioData[j] = 0;
            audioData[j + 1] = 0;
            continue;
        }

        audioData[j] = selected_buff.buff[selected_buff.read++];
        audioData[j + 1] = selected_buff.buff[selected_buff.read++];
        selected_buff.len -= 2;

//        __android_log_print(ANDROID_LOG_INFO, TAG, "render: %s", "After read");

    }

//        if (isWaveOn_.load()) {
//            if (amplFactor < 1) {
//                amplFactor += 0.001;
//                if (amplFactor > 1) amplFactor = 1;
//            }
//        } else {
//            if (amplFactor > 0) {
//                amplFactor -= 0.001;
//                if (amplFactor < 0) amplFactor = 0;
//            }
//        }
//
//        // Calculates the next sample value for the sine wave.
//
//        auto w1 = (float) (sin(phase_) * (amp_ * amplFactor));
//        auto w2 = (float) (sin(phase_2) * (amp_ * amplFactor));
//        auto w3 = (float) (sin(phase_3) * (amp_ * amplFactor));
//
//        audioData[i] = w1 + w2 + w3;
//
//        // Increments the phase, handling wrap around.
//        phase_ += phaseIncrement_;
//        phase_2 += phaseIncrement_2;
//        phase_3 += phaseIncrement_3;
//        if (phase_ > TWO_PI) phase_ -= TWO_PI;
//        if (phase_2 > TWO_PI) phase_2 -= TWO_PI;
//        if (phase_3 > TWO_PI) phase_3 -= TWO_PI;
//
////        } else {
////            // Outputs silence by setting sample value to zero.
////            if (buff != 0) {
////                audioData[i] = buff = (buff * 0.7f);
////                if (abs(buff) <= 0.1) buff = 0;
////            } else
////                audioData[i] = 0;
////        }
//    }
}

void Oscillator::pushData(int16_t *d, int len) {
    if (m_lock.try_lock()) {
        audio_buff a;
        a.len = len;
        a.buff = d;
        buffs.push(a);
        m_lock.unlock();
    }
}

void Oscillator::selectBuff() {
    if (m_lock.try_lock()) {
        delete selected_buff.buff;
        if (!buffs.empty()) {
            selected_buff = buffs.front();
            buffs.pop();
        }
        m_lock.unlock();
    }
}

void Oscillator::free() {
    if (m_lock.try_lock()) {
        while (!buffs.empty()) {
            auto a = buffs.front();
            delete a.buff;
            buffs.pop();
        }
        m_lock.unlock();
    }
}
