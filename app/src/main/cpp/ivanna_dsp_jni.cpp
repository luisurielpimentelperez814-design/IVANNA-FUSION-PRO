#include <jni.h>
#include <android/log.h>
#include <atomic>   // FIX #3: std::atomic<bool> para e.ready
#include <memory>
#include <mutex>
#include <vector>   // FIX #1: local buffers en nativeProcess (sin race entre threads)

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

static constexpr int kMaxFrames = 4096;

// ─── DSP Engine singleton ─────────────────────────────────────────────────────
struct DSPEngine {
    std::mutex              mtx;
    DSPParams               params;
    ParametricEQ            eq;
    Compressor              comp;
    HarmonicExciter         exciter;
    StereoWidener           widener;
    GainStage               gain;
    // FIX #3: atomic — se puede leer sin lock sin UB
    std::atomic<bool>       ready{false};

    // scratchL/scratchR eliminados del struct (FIX #1):
    // eran la causa de la race condition cuando dos threads llamaban
    // nativeProcess simultáneamente. Ahora se usan buffers locales por llamada.

    void applyParams() {
        eq.setParams(params);
        comp.setParams(params);
        exciter.setParams(params);
        widener.setParams(params);
        gain.setParams(params);
        ready.store(true, std::memory_order_release);
    }
};

// ─── JNI helpers ─────────────────────────────────────────────────────────────
// FIX #2: static local — C++11 garantiza inicialización atómica (§6.7).
// El patrón gEngine + if(!gEngine) era una initialization race si dos
// funciones JNI se llamaban concurrentemente antes del primer init.
static DSPEngine& engine() {
    static auto g = std::make_unique<DSPEngine>();
    return *g;
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
    // FIX #3: atomic load — seguro sin lock
    if (!e.ready.load(std::memory_order_acquire)) return;

    jfloat* data = env->GetFloatArrayElements(buf, nullptr);
    if (!data) return;

    const int n = (numFrames > kMaxFrames) ? kMaxFrames : numFrames;

    // FIX #1: buffers locales por llamada — cada thread tiene su propia pila.
    // Si dos callbacks de AudioTrack llaman nativeProcess en paralelo,
    // no comparten memoria: no hay race condition.
    std::vector<float> lBuf(n), rBuf(n);

    // De-interleave (fuera del lock — solo accede a memoria local)
    for (int i = 0; i < n; ++i) {
        lBuf[i] = data[i*2];
        rBuf[i] = data[i*2+1];
    }

    {
        std::lock_guard<std::mutex> lk(e.mtx);
        e.gain.processInput(lBuf.data(), rBuf.data(), n);
        e.exciter.process(lBuf.data(), rBuf.data(), n);
        e.comp.process(lBuf.data(), rBuf.data(), n);
        e.eq.process(lBuf.data(), rBuf.data(), n);
        e.widener.process(lBuf.data(), rBuf.data(), n);
        e.gain.processOutput(lBuf.data(), rBuf.data(), n);
    }

    // Re-interleave (fuera del lock — solo memoria local)
    for (int i = 0; i < n; ++i) {
        data[i*2]   = lBuf[i];
        data[i*2+1] = rBuf[i];
    }

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
    e.gain.reset();
    e.ready.store(false, std::memory_order_release);
    LOGI("DSP engine reset");
}

// ─── Version ──────────────────────────────────────────────────────────────────
JNIEXPORT jstring JNICALL
Java_com_ivanna_fusion_pro_DSPBridge_nativeVersion(JNIEnv* env, jobject) {
    return env->NewStringUTF("IVANNA-FUSION-PRO DSP v1.2 | GORE TNS");
}

} // extern "C"
