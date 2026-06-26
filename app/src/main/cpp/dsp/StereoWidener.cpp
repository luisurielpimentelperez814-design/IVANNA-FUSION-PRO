#include "../include/StereoWidener.h"

namespace ivanna {

void StereoWidener::setParams(const DSPParams& p) {
    // gamma 0→narrow (0.0), 0.5→unity (1.0), 1→wide (2.0)
    width_ = p.gamma * 2.f;
}

void StereoWidener::process(float* left, float* right, int frames) {
    const float w = width_;
    const float coefM = 1.f;
    const float coefS = w;
    for (int i = 0; i < frames; ++i) {
        float m = (left[i] + right[i]) * 0.5f;
        float s = (left[i] - right[i]) * 0.5f;
        left[i]  = m + coefS * s;
        right[i] = m - coefS * s;
    }
}

} // namespace ivanna
