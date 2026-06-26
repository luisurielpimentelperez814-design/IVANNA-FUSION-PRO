package com.ivanna.fusion.pro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { IvannaApp() }
    }
}

@Composable
fun IvannaApp() {
    val nav = rememberNavController()
    val bg = Color(0xFF0B0B0B)
    val cyan = Color(0xFF00F5FF)
    MaterialTheme(colorScheme = darkColorScheme(background = bg, surface = bg)) {
        NavHost(nav, startDestination = "splash") {
            composable("splash") { SplashScreen(cyan) { nav.navigate("intro") } }
            composable("intro") { IntroScreen(cyan) { nav.navigate("dashboard") } }
            composable("dashboard") { DashboardScreen(cyan) }
        }
    }
}

@Composable
fun SplashScreen(cyan: Color, onAccept: () -> Unit) {
    Box(Modifier.fillMaxSize().background(Color(0xFF0B0B0B)), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(24.dp)) {
            Text("IVANNA-FUSION", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
            Spacer(Modifier.height(8.dp))
            Box(Modifier.height(2.dp).width(200.dp).background(cyan.copy(alpha = 0.6f)))
            Spacer(Modifier.height(32.dp))
            Text("TÉRMINOS Y CONDICIONES", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(12.dp))
            Text(
                "ADVERTENCIA: Queda estrictamente prohibida la duplicación, falsificación, distribución no autorizada o ingeniería inversa de este software. El incumplimiento conlleva acciones legales.",
                color = Color(0xFFCCCCCC), fontSize = 14.sp, textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(40.dp))
            Button(onClick = onAccept,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                modifier = Modifier.border(2.dp, cyan, RoundedCornerShape(24.dp)).height(48.dp).width(240.dp)
            ) { Text("ACEPTAR Y CONTINUAR", color = Color.White) }
        }
    }
}

@Composable
fun IntroScreen(cyan: Color, onEnter: () -> Unit) {
    Column(Modifier.fillMaxSize().background(Color(0xFF0B0B0B)).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(24.dp))
        Text("EXPERIENCE THE LEGENDS", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        val bands = listOf("Grand Funk Railroad","Ted Nugent","Steve Miller Band","Led Zeppelin","Bachman-Turner Overdrive","Grand Funk Railroad","Ted Nugent")
        LazyVerticalGrid(columns = GridCells.Fixed(3), modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(bands) { band ->
                Box(Modifier.aspectRatio(16f/9f).shadow(4.dp, RoundedCornerShape(8.dp)).border(1.dp, cyan.copy(0.4f), RoundedCornerShape(8.dp)).background(Color(0xFF111111)),
                    contentAlignment = Alignment.Center) {
                    Text(band, color = Color(0xFF888888), fontSize = 10.sp, textAlign = TextAlign.Center, modifier = Modifier.padding(4.dp))
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Button(onClick = onEnter, modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = cyan.copy(alpha = 0.15f)),
            border = androidx.compose.foundation.BorderStroke(2.dp, cyan)
        ) { Text("ENTRAR A LA APP", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold) }
    }
}

@Composable
fun DashboardScreen(cyan: Color) {
    Column(Modifier.fillMaxSize().background(Color(0xFF0B0B0B)).padding(12.dp)) {
        // Top bar
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Text("IVANNA-FUSION", color = Color.White, fontWeight = FontWeight.Bold)
            Row { repeat(2){ Box(Modifier.size(40.dp,20.dp).background(Color(0xFF111111)).border(1.dp, cyan.copy(0.3f))) } }
        }
        Spacer(Modifier.height(12.dp))
        Section("GAIN STAGE", listOf(
            "DRIVE" to "Saturación de entrada, agrega armónicos y calidez analógica",
            "WET" to "Nivel de señal procesada en paralelo",
            "MIX" to "Relación de mezcla entre señal seca y húmeda"
        ), cyan)
        Section("DSP ENGINE", listOf(
            "ALPHA" to "Carácter del motor DSP A, textura y color",
            "BETA" to "Carácter del motor DSP B, variante de modulación",
            "GAMMA" to "Mezcla entre motor A y B, control de capas",
            "FREQ" to "Frecuencia central del filtro/efecto, en Hz",
            "RESONANCE" to "Factor Q o énfasis del filtro, realce selectivo"
        ), cyan)
        Section("EQ & OUTPUT", listOf(
            "LOW" to "Estante de graves y subgraves, realce o corte",
            "MID" to "Presencia de medios, cuerpo y definición",
            "HIGH" to "Aire de frecuencias altas, brillo sedoso",
            "PRESENCE" to "Excitador armónico superior, claridad y chispa",
            "MASTER" to "Nivel de salida final general"
        ), cyan)
    }
}

@Composable
fun Section(title: String, items: List<Pair<String,String>>, cyan: Color) {
    Column(Modifier.fillMaxWidth().padding(vertical = 8.dp).border(1.dp, Color(0xFF222222), RoundedCornerShape(8.dp)).padding(8.dp)) {
        Text(title, color = cyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            items.forEach { (name, desc) ->
                var value by remember { mutableStateOf(0f) }
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(70.dp)) {
                    Text(String.format("%.1f dB", value*10-5), color = Color.White, fontSize = 10.sp)
                    Slider(value = value, onValueChange = { value = it }, modifier = Modifier.height(100.dp), colors = SliderDefaults.colors(thumbColor = cyan, activeTrackColor = cyan))
                    Text(name, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
                    Text(desc, color = Color(0xFF888888), fontSize = 8.sp, textAlign = TextAlign.Center, lineHeight = 10.sp)
                }
            }
        }
    }
}
