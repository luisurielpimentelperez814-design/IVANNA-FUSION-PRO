package com.ivanna.fusion.pro

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
// FIX #11: kotlin.math en lugar de java.lang.Math (idiomático Kotlin)
import kotlin.math.log10
import kotlin.math.pow

/**
 * Observable DSP parameter state.
 * All 0..1 normalized except freq (20..20000 Hz), resonance (0.1..10 Q),
 * and EQ bands (-12..+12 dB).
 */
class DSPState : ViewModel() {

    // GAIN STAGE
    var drive     by mutableFloatStateOf(0.65f)
    var wet       by mutableFloatStateOf(0.50f)
    var mix       by mutableFloatStateOf(0.70f)

    // DSP ENGINE
    var alpha     by mutableFloatStateOf(0.50f)
    var beta      by mutableFloatStateOf(0.50f)
    var gamma     by mutableFloatStateOf(0.50f)
    var freq      by mutableFloatStateOf(1000f)  // Hz
    var resonance by mutableFloatStateOf(0.707f) // Q

    // EQ & OUTPUT (dB -12..+12)
    var low       by mutableFloatStateOf(0f)
    var mid       by mutableFloatStateOf(0f)
    var high      by mutableFloatStateOf(0f)
    var presence  by mutableFloatStateOf(0f)
    var master    by mutableFloatStateOf(0f)

    /** Push current state to native DSP engine. */
    fun pushToNative() {
        DSPBridge.setParams(
            drive = drive,
            wet = wet,
            mix = mix,
            alpha = alpha,
            beta = beta,
            gamma = gamma,
            freq = freq,
            resonance = resonance,
            low = low,
            mid = mid,
            high = high,
            presence = presence,
            master = master
        )
    }

    companion object {
        fun sliderToDb(v: Float): Float = v * 24f - 12f
        fun dbToSlider(db: Float): Float = (db + 12f) / 24f

        // FIX #11: kotlin.math.pow — reemplaza Math.pow (Java)
        /** Maps 0..1 slider → 20..20000 Hz (log) */
        fun sliderToFreq(v: Float): Float =
            (20f * 1000.0.pow(v.toDouble())).toFloat()

        /** Maps 0..1 slider → 0.1..10 Q (log) */
        fun sliderToQ(v: Float): Float =
            (0.1f * 100.0.pow(v.toDouble())).toFloat()
    }
}
