#pragma once
#include "dsp_types.h"

namespace ivanna {

// RMS feed-forward compressor driven by alpha/beta/gamma params
class Compressor {
public:
    void setParams(const DSPParams& p);
    void process(float* left, float* right, int frames);
    void reset();

private:
    float threshold_ = -12.f; // dB
    float ratio_     = 4.f;
    float attackCoef_  = 0.f;
    float releaseCoef_ = 0.f;
    float makeupGain_  = 1.f;
    float env_ = 0.f;
    uint32_t sr_ = 48000;
};

} // namespace ivanna
