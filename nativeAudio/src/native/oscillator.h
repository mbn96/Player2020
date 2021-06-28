//
// Created by Mreza on 11/21/2020.
//




#include <atomic>
#include <stdint.h>
#include <queue>
#include <mutex>
#include <android/log.h>


typedef struct {
    int len{0}, read{0};
    int16_t *buff;
} audio_buff;

class Oscillator {
public:
    void setWaveOn(bool isWaveOn);

    void setSampleRate(int32_t sampleRate);

    void render(int16_t *audioData, int32_t numFrames);

    void setTune(float amp, float freq);

    void pushData(int16_t *, int len);

    void selectBuff();

    void free();

private:
    std::atomic<bool> isWaveOn_{false};
    double phase_ = 0.0;
    double phase_2 = 0.0;
    double phase_3 = 0.0;
    double phaseIncrement_ = 0.0;
    double phaseIncrement_2 = 0.0;
    double phaseIncrement_3 = 0.0;
    float amp_ = 0.5;
    float freq_ = 440;
    float freq_2 = freq_ * 1.5;
    float freq_3 = freq_ * 1.5;
    int32_t sampleRate_ = 1;
    std::queue<audio_buff> buffs;
    audio_buff selected_buff;
    std::mutex m_lock;
};


