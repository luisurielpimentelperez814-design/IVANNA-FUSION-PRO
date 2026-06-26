package com.ivanna.fusion.pro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// ─── Palette ─────────────────────────────────────────────────────────────────
private val Carbon   = Color(0xFF0B0B0B)
private val Surface1 = Color(0xFF111111)
private val Surface2 = Color(0xFF181818)
private val Border1  = Color(0xFF222222)
private val CyanGlow = Color(0xFF00F5FF)
private val CyanDim  = Color(0x3300F5FF)
private val TextPri  = Color(0xFFFFFFFF)
private val TextSec  = Color(0xFF888888)
private val TextMid  = Color(0xFFCCCCCC)

// ─── Entry ───────────────────────────────────────────────────────────────────
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        DSPBridge.init(48000)
        setContent { IvannaApp() }
    }
}

@Composable
fun IvannaApp() {
    val nav = rememberNavController()
    val dsp: DSPState = viewModel()
    MaterialTheme(colorScheme = darkColorScheme(background = Carbon, surface = Surface1)) {
        NavHost(nav, startDestination = "splash") {
            composable("splash")    { SplashScreen   { nav.navigate("intro")     } }
            composable("intro")     { IntroScreen    { nav.navigate("dashboard") } }
            composable("dashboard") { DashboardScreen(dsp)                         }
        }
    }
}

// ─── Splash ──────────────────────────────────────────────────────────────────
@Composable
fun SplashScreen(onAccept: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Carbon)
            .windowInsetsPadding(WindowInsets.systemBars),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 28.dp)
        ) {
            Text("IVANNA-FUSION PRO", color = TextPri, fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold, letterSpacing = 3.sp)
            Spacer(Modifier.height(4.dp))
            Text("GORE TNS · LUPP-OR9", color = CyanGlow, fontSize = 11.sp, letterSpacing = 2.sp)
            Spacer(Modifier.height(8.dp))
            Box(Modifier.height(1.dp).width(220.dp).background(
                Brush.horizontalGradient(listOf(Color.Transparent, CyanGlow, Color.Transparent))
            ))
            Spacer(Modifier.height(36.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Border1, RoundedCornerShape(12.dp))
                    .background(Surface2, RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Text("TÉRMINOS Y CONDICIONES", color = CyanGlow, fontSize = 13.sp,
                    fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                Spacer(Modifier.height(10.dp))
                Text(
                    "ADVERTENCIA: Queda estrictamente prohibida la duplicación, falsificación, " +
                    "distribución no autorizada o ingeniería inversa de este software. " +
                    "El incumplimiento conlleva acciones legales bajo las leyes de propiedad " +
                    "intelectual aplicables. Uso exclusivo del titular de la licencia.",
                    color = TextMid, fontSize = 13.sp, lineHeight = 19.sp,
                    textAlign = TextAlign.Justify
                )
            }
            Spacer(Modifier.height(32.dp))
            Button(
                onClick = onAccept,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                border = BorderStroke(2.dp, CyanGlow),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Text("ACEPTAR Y CONTINUAR", color = TextPri,
                    fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
            }
        }
    }
}

// ─── Intro ───────────────────────────────────────────────────────────────────
@Composable
fun IntroScreen(onEnter: () -> Unit) {
    val bands = listOf(
        "Grand Funk Railroad", "Led Zeppelin", "Rush",
        "Budgie", "Edgar Winter", "Bachman-Turner Overdrive", "Steve Miller Band"
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Carbon)
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(28.dp))
        Text("EXPERIENCE THE LEGENDS", color = TextPri, fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold, letterSpacing = 1.5.sp)
        Spacer(Modifier.height(4.dp))
        Text("Audio procesado por IVANNA-FUSION PRO", color = TextSec, fontSize = 11.sp)
        Spacer(Modifier.height(16.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(bands) { band ->
                Box(
                    modifier = Modifier
                        .aspectRatio(16f / 9f)
                        .shadow(6.dp, RoundedCornerShape(8.dp))
                        .background(Surface2, RoundedCornerShape(8.dp))
                        .border(1.dp, CyanDim, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(Modifier.size(8.dp).clip(CircleShape).background(CyanGlow.copy(alpha = 0.5f)))
                        Spacer(Modifier.height(4.dp))
                        Text(band, color = TextSec, fontSize = 9.sp, textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 4.dp), lineHeight = 12.sp)
                    }
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = onEnter,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CyanDim),
            border = BorderStroke(2.dp, CyanGlow),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("ENTRAR A LA APP", color = TextPri, fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
        }
        Spacer(Modifier.height(16.dp))
    }
}

// ─── Dashboard ───────────────────────────────────────────────────────────────
@Composable
fun DashboardScreen(dsp: DSPState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Carbon)
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Surface2)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("IVANNA-FUSION PRO", color = TextPri, fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp, letterSpacing = 1.5.sp)
                Text("GORE TNS", color = CyanGlow, fontSize = 9.sp, letterSpacing = 1.sp)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                StatusDot(active = true,  label = "DSP")
                StatusDot(active = true,  label = "EQ")
                StatusDot(active = false, label = "FX")
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 10.dp)
        ) {
            // GAIN STAGE
            item {
                DspSection(title = "GAIN STAGE") {
                    FaderControl("DRIVE", dsp.drive, "Saturación") { v ->
                        dsp.drive = v; dsp.pushToNative()
                    }
                    FaderControl("WET", dsp.wet, "Señal proc.") { v ->
                        dsp.wet = v; dsp.pushToNative()
                    }
                    FaderControl("MIX", dsp.mix, "Seca/Húmeda") { v ->
                        dsp.mix = v; dsp.pushToNative()
                    }
                }
            }
            // DSP ENGINE
            item {
                DspSection(title = "DSP ENGINE") {
                    FaderControl("ALPHA", dsp.alpha, "Motor DSP A") { v ->
                        dsp.alpha = v; dsp.pushToNative()
                    }
                    FaderControl("BETA", dsp.beta, "Motor DSP B") { v ->
                        dsp.beta = v; dsp.pushToNative()
                    }
                    FaderControl("GAMMA", dsp.gamma, "Capas A/B") { v ->
                        dsp.gamma = v; dsp.pushToNative()
                    }
                    // FREQ: log-mapped display
                    val freqSlider = remember(dsp.freq) {
                        (Math.log10(dsp.freq.toDouble() / 20.0) / Math.log10(1000.0)).toFloat()
                            .coerceIn(0f, 1f)
                    }
                    FaderControl("FREQ", freqSlider,
                        "${dsp.freq.toInt()} Hz") { v ->
                        dsp.freq = DSPState.sliderToFreq(v); dsp.pushToNative()
                    }
                    // RESONANCE: log-mapped
                    val qSlider = remember(dsp.resonance) {
                        (Math.log10(dsp.resonance.toDouble() / 0.1) / Math.log10(100.0)).toFloat()
                            .coerceIn(0f, 1f)
                    }
                    FaderControl("RES", qSlider,
                        "Q=%.2f".format(dsp.resonance)) { v ->
                        dsp.resonance = DSPState.sliderToQ(v); dsp.pushToNative()
                    }
                }
            }
            // EQ & OUTPUT
            item {
                DspSection(title = "EQ & OUTPUT") {
                    EqFader("LOW",      dsp.low)      { dsp.low      = it; dsp.pushToNative() }
                    EqFader("MID",      dsp.mid)      { dsp.mid      = it; dsp.pushToNative() }
                    EqFader("HIGH",     dsp.high)     { dsp.high     = it; dsp.pushToNative() }
                    EqFader("PRESENCE", dsp.presence) { dsp.presence = it; dsp.pushToNative() }
                    EqFader("MASTER",   dsp.master)   { dsp.master   = it; dsp.pushToNative() }
                }
            }
        }
    }
}

// ─── Components ──────────────────────────────────────────────────────────────
@Composable
fun StatusDot(active: Boolean, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(Modifier.size(8.dp).clip(CircleShape)
            .background(if (active) CyanGlow else Color(0xFF333333)))
        Text(label, color = if (active) CyanGlow else Color(0xFF444444), fontSize = 7.sp)
    }
}

@Composable
fun DspSection(title: String, content: @Composable RowScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Border1, RoundedCornerShape(10.dp))
            .background(Surface1, RoundedCornerShape(10.dp))
            .padding(10.dp)
    ) {
        Text(title, color = CyanGlow, fontSize = 10.sp,
            fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            content = content
        )
    }
}

/** Normalized 0..1 fader */
@Composable
fun FaderControl(
    name: String,
    value: Float,
    desc: String,
    onValueChange: (Float) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(58.dp)
    ) {
        Text(
            text = "%.2f".format(value),
            color = CyanGlow, fontSize = 9.sp, fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(2.dp))
        Box(
            modifier = Modifier.width(36.dp).height(90.dp),
            contentAlignment = Alignment.Center
        ) {
            Slider(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.width(90.dp).rotate(-90f),
                colors = SliderDefaults.colors(
                    thumbColor = CyanGlow,
                    activeTrackColor = CyanGlow,
                    inactiveTrackColor = Border1
                )
            )
        }
        Spacer(Modifier.height(2.dp))
        Text(name, color = TextPri, fontSize = 10.sp,
            fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        Text(desc, color = TextSec, fontSize = 7.sp,
            textAlign = TextAlign.Center, lineHeight = 9.sp)
    }
}

/** EQ fader in dB -12..+12 */
@Composable
fun EqFader(name: String, db: Float, onDbChange: (Float) -> Unit) {
    val sliderVal = DSPState.dbToSlider(db)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(58.dp)
    ) {
        Text(
            text = if (db >= 0) "+%.1f".format(db) else "%.1f".format(db),
            color = CyanGlow, fontSize = 9.sp, fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(2.dp))
        Box(
            modifier = Modifier.width(36.dp).height(90.dp),
            contentAlignment = Alignment.Center
        ) {
            Slider(
                value = sliderVal,
                onValueChange = { onDbChange(DSPState.sliderToDb(it)) },
                modifier = Modifier.width(90.dp).rotate(-90f),
                colors = SliderDefaults.colors(
                    thumbColor = if (db > 0f) CyanGlow else Color(0xFF00AACC),
                    activeTrackColor = CyanGlow,
                    inactiveTrackColor = Border1
                )
            )
        }
        Spacer(Modifier.height(2.dp))
        Text(name, color = TextPri, fontSize = 10.sp,
            fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        Text("dB", color = TextSec, fontSize = 7.sp, textAlign = TextAlign.Center)
    }
}
