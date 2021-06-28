//
// Created by Mreza on 11/21/2020.
//

#include <aaudio/AAudio.h>
#include "oscillator.h"


class AudioEngine {
public:
    bool start(int32_t sr);

    void stop();

    void restart();

    void setToneOn(bool isToneOn);

    void setTune(float amp, float freq);

    void pushData(int16_t *, int len);

    void setSpeed(float speed);

private:
    int32_t sp;
    float speed{1};
    Oscillator oscillator_;
    AAudioStream *stream_;
};


