#include <jni.h>
#include <android/log.h>
#include <memory>
#include <mutex>

#include "include/dsp_types.h"
#include "include/ParametricEQ.h"
#include "include/Compressor.h"
#include "include/HarmonicExciter.h"
#include "include/StereoWidener.h"
#include "include/GainStage.h"

#define TAG "IVANNA_DSP"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,  TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, TAG, __VA_ARGS__)

using namespace ivanna;

// ─── DSP Engine singleton ─────────────────────────────────────────────────────
struct DSPEngine {
    std::mutex          mtx;
    DSPParams           params;
    ParametricEQ        eq;
    Compressor          comp;
    HarmonicExciter     exciter;
    StereoWidener       widener;
    GainStage           gain;
    bool                ready = false;

    void applyParams() {
        eq.setParams(params);
        comp.setParams(params);
        exciter.setParams(params);
        widener.setParams(params);
        gain.setParams(params);
        ready = true;
    }
};

static std::unique_ptr<DSPEngine> gEngine;

// ─── JNI helpers ─────────────────────────────────────────────────────────────
static DSPEngine& engine() {
    if (!gEngine) gEngine = std::make_unique<DSPEngine>();
    return *gEngine;
}

extern "C" {

// ─── Init ─────────────────────────────────────────────────────────────────────
JNIEXPORT void JNICALL
Java_com_ivanna_fusion_pro_DSPBridge_nativeInit(JNIEnv*, jobject, jint sampleRate) {
    auto& e = engine();
    std::lock_guard<std::mutex> lk(e.mtx);
    e.params.sampleRate = (uint32_t)sampleRate;
    e.applyParams();
    LOGI("DSP engine init  sr=%d", sampleRate);
}

// ─── Set params ──────────────────────────────────────────────────────────────
JNIEXPORT void JNICALL
Java_com_ivanna_fusion_pro_DSPBridge_nativeSetParams(
    JNIEnv*, jobject,
    jfloat drive, jfloat wet, jfloat mix,
    jfloat alpha, jfloat beta, jfloat gamma,
    jfloat freq,  jfloat resonance,
    jfloat low,   jfloat mid,   jfloat high,
    jfloat presence, jfloat master
) {
    auto& e = engine();
    std::lock_guard<std::mutex> lk(e.mtx);
    e.params.drive     = drive;
    e.params.wet       = wet;
    e.params.mix       = mix;
    e.params.alpha     = alpha;
    e.params.beta      = beta;
    e.params.gamma     = gamma;
    e.params.freq      = freq;
    e.params.resonance = resonance;
    e.params.low       = low;
    e.params.mid       = mid;
    e.params.high      = high;
    e.params.presence  = presence;
    e.params.master    = master;
    e.applyParams();
}

// ─── Process interleaved stereo PCM float buffer ──────────────────────────────
// buf: interleaved L,R,L,R... float32
JNIEXPORT void JNICALL
Java_com_ivanna_fusion_pro_DSPBridge_nativeProcess(
    JNIEnv* env, jobject,
    jfloatArray buf, jint numFrames
) {
    auto& e = engine();
    if (!e.ready) return;

    jfloat* data = env->GetFloatArrayElements(buf, nullptr);
    if (!data) return;

    // De-interleave
    int n = numFrames;
    float* lBuf = new float[n];
    float* rBuf = new float[n];
    for (int i = 0; i < n; ++i) {
        lBuf[i] = data[i*2];
        rBuf[i] = data[i*2+1];
    }

    {
        std::lock_guard<std::mutex> lk(e.mtx);
        e.gain.processInput(lBuf, rBuf, n);
        e.exciter.process(lBuf, rBuf, n);
        e.comp.process(lBuf, rBuf, n);
        e.eq.process(lBuf, rBuf, n);
        e.widener.process(lBuf, rBuf, n);
        e.gain.processOutput(lBuf, rBuf, n);
    }

    // Re-interleave
    for (int i = 0; i < n; ++i) {
        data[i*2]   = lBuf[i];
        data[i*2+1] = rBuf[i];
    }

    delete[] lBuf;
    delete[] rBuf;
    env->ReleaseFloatArrayElements(buf, data, 0);
}

// ─── Reset ────────────────────────────────────────────────────────────────────
JNIEXPORT void JNICALL
Java_com_ivanna_fusion_pro_DSPBridge_nativeReset(JNIEnv*, jobject) {
    auto& e = engine();
    std::lock_guard<std::mutex> lk(e.mtx);
    e.eq.reset();
    e.comp.reset();
    e.exciter.reset();
    e.ready = false;
    LOGI("DSP engine reset");
}

// ─── Version ──────────────────────────────────────────────────────────────────
JNIEXPORT jstring JNICALL
Java_com_ivanna_fusion_pro_DSPBridge_nativeVersion(JNIEnv* env, jobject) {
    return env->NewStringUTF("IVANNA-FUSION-PRO DSP v1.1 | GORE TNS");
}

} // extern "C"
