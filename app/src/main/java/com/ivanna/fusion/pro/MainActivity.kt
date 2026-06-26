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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// ─── Colors ──────────────────────────────────────────────────────────────────
private val Carbon    = Color(0xFF0B0B0B)
private val Surface1  = Color(0xFF111111)
private val Surface2  = Color(0xFF181818)
private val Border1   = Color(0xFF222222)
private val CyanGlow  = Color(0xFF00F5FF)
private val CyanDim   = Color(0x3300F5FF)
private val TextPri   = Color(0xFFFFFFFF)
private val TextSec   = Color(0xFF888888)
private val TextMid   = Color(0xFFCCCCCC)

// ─── Entry ───────────────────────────────────────────────────────────────────
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { IvannaApp() }
    }
}

@Composable
fun IvannaApp() {
    val nav = rememberNavController()
    MaterialTheme(colorScheme = darkColorScheme(background = Carbon, surface = Surface1)) {
        NavHost(nav, startDestination = "splash") {
            composable("splash")    { SplashScreen   { nav.navigate("intro")      } }
            composable("intro")     { IntroScreen    { nav.navigate("dashboard")  } }
            composable("dashboard") { DashboardScreen()                            }
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
            // Logo
            Text(
                "IVANNA-FUSION PRO",
                color = TextPri,
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 3.sp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "GORE TNS · LUPP-OR9",
                color = CyanGlow,
                fontSize = 11.sp,
                letterSpacing = 2.sp
            )
            Spacer(Modifier.height(8.dp))
            // Glowing separator
            Box(
                Modifier
                    .height(1.dp)
                    .width(220.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color.Transparent, CyanGlow, Color.Transparent)
                        )
                    )
            )
            Spacer(Modifier.height(36.dp))
            // Terms box
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Border1, RoundedCornerShape(12.dp))
                    .background(Surface2, RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Text(
                    "TÉRMINOS Y CONDICIONES",
                    color = CyanGlow,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    "ADVERTENCIA: Queda estrictamente prohibida la duplicación, falsificación, " +
                    "distribución no autorizada o ingeniería inversa de este software. " +
                    "El incumplimiento conlleva acciones legales bajo las leyes de propiedad " +
                    "intelectual aplicables. Uso exclusivo del titular de la licencia.",
                    color = TextMid,
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    textAlign = TextAlign.Justify
                )
            }
            Spacer(Modifier.height(32.dp))
            Button(
                onClick = onAccept,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                border = BorderStroke(2.dp, CyanGlow),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    "ACEPTAR Y CONTINUAR",
                    color = TextPri,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )
            }
        }
    }
}

// ─── Intro ───────────────────────────────────────────────────────────────────
@Composable
fun IntroScreen(onEnter: () -> Unit) {
    val bands = listOf(
        "Grand Funk Railroad",
        "Led Zeppelin",
        "Rush",
        "Budgie",
        "Edgar Winter",
        "Bachman-Turner Overdrive",
        "Steve Miller Band"
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
        Text(
            "EXPERIENCE THE LEGENDS",
            color = TextPri,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.5.sp
        )
        Spacer(Modifier.height(4.dp))
        Text(
            "Audio procesado por IVANNA-FUSION PRO",
            color = TextSec,
            fontSize = 11.sp
        )
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
                        Box(
                            Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(CyanGlow.copy(alpha = 0.5f))
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            band,
                            color = TextSec,
                            fontSize = 9.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 4.dp),
                            lineHeight = 12.sp
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = onEnter,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CyanDim),
            border = BorderStroke(2.dp, CyanGlow),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                "ENTRAR A LA APP",
                color = TextPri,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            )
        }
        Spacer(Modifier.height(16.dp))
    }
}

// ─── Dashboard ───────────────────────────────────────────────────────────────
@Composable
fun DashboardScreen() {
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
                Text(
                    "IVANNA-FUSION PRO",
                    color = TextPri,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    letterSpacing = 1.5.sp
                )
                Text("GORE TNS", color = CyanGlow, fontSize = 9.sp, letterSpacing = 1.sp)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                StatusDot(active = true, label = "DSP")
                StatusDot(active = true, label = "EQ")
                StatusDot(active = false, label = "FX")
            }
        }
        // Body
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 10.dp)
        ) {
            item {
                DspSection(
                    "GAIN STAGE",
                    listOf(
                        Triple("DRIVE",    0.65f, "Saturación de entrada"),
                        Triple("WET",      0.50f, "Señal procesada"),
                        Triple("MIX",      0.70f, "Seca / Húmeda")
                    )
                )
            }
            item {
                DspSection(
                    "DSP ENGINE",
                    listOf(
                        Triple("ALPHA",     0.50f, "Motor DSP A"),
                        Triple("BETA",      0.50f, "Motor DSP B"),
                        Triple("GAMMA",     0.50f, "Capas A/B"),
                        Triple("FREQ",      0.40f, "Frec. central"),
                        Triple("RESONANCE", 0.30f, "Factor Q")
                    )
                )
            }
            item {
                DspSection(
                    "EQ & OUTPUT",
                    listOf(
                        Triple("LOW",      0.55f, "Graves"),
                        Triple("MID",      0.50f, "Medios"),
                        Triple("HIGH",     0.45f, "Agudos"),
                        Triple("PRESENCE", 0.60f, "Excitador"),
                        Triple("MASTER",   0.75f, "Salida")
                    )
                )
            }
        }
    }
}

@Composable
fun StatusDot(active: Boolean, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(if (active) CyanGlow else Color(0xFF333333))
        )
        Text(label, color = if (active) CyanGlow else Color(0xFF444444), fontSize = 7.sp)
    }
}

@Composable
fun DspSection(title: String, controls: List<Triple<String, Float, String>>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Border1, RoundedCornerShape(10.dp))
            .background(Surface1, RoundedCornerShape(10.dp))
            .padding(10.dp)
    ) {
        Text(
            title,
            color = CyanGlow,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp
        )
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            controls.forEach { (name, initial, desc) ->
                FaderControl(name = name, initialValue = initial, desc = desc)
            }
        }
    }
}

@Composable
fun FaderControl(name: String, initialValue: Float, desc: String) {
    var value by remember { mutableFloatStateOf(initialValue) }
    val dbVal = (value * 24f) - 12f   // -12 dB .. +12 dB

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(58.dp)
    ) {
        // dB readout
        Text(
            text = if (dbVal >= 0) "+%.1f".format(dbVal) else "%.1f".format(dbVal),
            color = CyanGlow,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(2.dp))
        // Vertical fader via rotate + constrained height
        Box(
            modifier = Modifier
                .width(36.dp)
                .height(90.dp),
            contentAlignment = Alignment.Center
        ) {
            Slider(
                value = value,
                onValueChange = { value = it },
                modifier = Modifier
                    .width(90.dp)
                    .rotate(-90f),
                colors = SliderDefaults.colors(
                    thumbColor = CyanGlow,
                    activeTrackColor = CyanGlow,
                    inactiveTrackColor = Border1
                )
            )
        }
        Spacer(Modifier.height(2.dp))
        Text(name, color = TextPri, fontSize = 10.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        Text(desc, color = TextSec, fontSize = 7.sp, textAlign = TextAlign.Center, lineHeight = 9.sp)
    }
}
