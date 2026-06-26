#include "../include/GainStage.h"
#include <cmath>

namespace ivanna {

static inline float dbToLin(float db) {
    return std::pow(10.f, db / 20.f);
}

void GainStage::setParams(const DSPParams& p) {
    // mix maps 0..1 → -6..+6 dB input gain
    inputGain_  = dbToLin((p.mix - 0.5f) * 12.f);
    outputGain_ = dbToLin(p.master);
}

void GainStage::processInput(float* left, float* right, int frames) {
    constexpr float smooth = 0.9995f;
    for (int i = 0; i < frames; ++i) {
        currentIn_ = smooth * currentIn_ + (1.f - smooth) * inputGain_;
        left[i]  *= currentIn_;
        right[i] *= currentIn_;
    }
}

void GainStage::processOutput(float* left, float* right, int frames) {
    constexpr float smooth = 0.9995f;
    for (int i = 0; i < frames; ++i) {
        currentOut_ = smooth * currentOut_ + (1.f - smooth) * outputGain_;
        left[i]  *= currentOut_;
        right[i] *= currentOut_;
    }
}

} // namespace ivanna
