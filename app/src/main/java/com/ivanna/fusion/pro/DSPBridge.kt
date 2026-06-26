package com.ivanna.fusion.pro

import android.util.Log

/**
 * Kotlin singleton that wraps the native DSP engine (libivanna_dsp.so).
 * All parameter values are normalized 0..1 from the UI sliders.
 * freq and resonance are physical (Hz, Q).
 * low/mid/high/presence/master are in dB (-12..+12).
 */
object DSPBridge {

    private const val TAG = "IVANNA_DSP"
    private var loaded = false

    init {
        try {
            System.loadLibrary("ivanna_dsp")
            loaded = true
            Log.i(TAG, "libivanna_dsp loaded — ${nativeVersion()}")
        } catch (e: UnsatisfiedLinkError) {
            Log.e(TAG, "Native lib not available: ${e.message}")
        }
    }

    fun init(sampleRate: Int = 48000) {
        if (loaded) nativeInit(sampleRate)
    }

    fun setParams(
        drive: Float,
        wet: Float,
        mix: Float,
        alpha: Float,
        beta: Float,
        gamma: Float,
        freq: Float,        // Hz
        resonance: Float,   // Q
        low: Float,         // dB
        mid: Float,
        high: Float,
        presence: Float,
        master: Float
    ) {
        if (!loaded) return
        nativeSetParams(
            drive, wet, mix,
            alpha, beta, gamma,
            freq, resonance,
            low, mid, high, presence, master
        )
    }

    /** Process interleaved stereo float32 PCM in-place. */
    fun process(buffer: FloatArray, numFrames: Int) {
        if (loaded) nativeProcess(buffer, numFrames)
    }

    fun reset() {
        if (loaded) nativeReset()
    }

    fun version(): String = if (loaded) nativeVersion() else "native unavailable"

    // ── JNI declarations ────────────────────────────────────────────────────
    private external fun nativeInit(sampleRate: Int)
    private external fun nativeSetParams(
        drive: Float, wet: Float, mix: Float,
        alpha: Float, beta: Float, gamma: Float,
        freq: Float, resonance: Float,
        low: Float, mid: Float, high: Float,
        presence: Float, master: Float
    )
    private external fun nativeProcess(buf: FloatArray, numFrames: Int)
    private external fun nativeReset()
    private external fun nativeVersion(): String
}
