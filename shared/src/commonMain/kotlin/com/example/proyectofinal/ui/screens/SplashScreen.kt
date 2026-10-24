package com.example.proyectofinal.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.proyectofinal.ui.components.Icono
import com.example.proyectofinal.ui.theme.Blanco
import com.example.proyectofinal.ui.theme.Terracota
import kotlinx.coroutines.delay
import proyectofinal.shared.generated.resources.*

/**
 * Splash (boceto 01): el único momento animado de la app. El sello aparece
 * y crece un poco; a los 1.6 s se pasa al selector de rol.
 */
@Composable
fun SplashScreen(onTerminar: () -> Unit) {
    val aparicion = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        aparicion.animateTo(1f, tween(700))
        delay(900)
        onTerminar()
    }
    Box(
        Modifier.fillMaxSize().background(Terracota.t500).safeDrawingPadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.alpha(aparicion.value).scale(0.9f + 0.1f * aparicion.value)
        ) {
            Box(
                Modifier.size(132.dp).clip(CircleShape).background(Terracota.t400),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    Modifier.size(88.dp).clip(RoundedCornerShape(24.dp)).background(Blanco),
                    contentAlignment = Alignment.Center
                ) {
                    Icono(Res.drawable.ic_herramienta, null, Terracota.t500, Modifier.size(44.dp))
                }
            }
            Spacer(Modifier.height(24.dp))
            Text("Oficios Jiquilpan", style = MaterialTheme.typography.displaySmall, color = Blanco)
            Spacer(Modifier.height(6.dp))
            Text(
                "Talento y confianza en nuestra comunidad",
                style = MaterialTheme.typography.bodyLarge,
                color = Terracota.t100,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(18.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf("Carpintería", "Herrería", "Plomería", "Albañilería").forEach {
                    Text(
                        it,
                        style = MaterialTheme.typography.labelSmall,
                        color = Blanco,
                        modifier = Modifier.clip(CircleShape).background(Color.White.copy(alpha = 0.16f))
                            .padding(horizontal = 9.dp, vertical = 4.dp)
                    )
                }
            }
        }
        Column(
            Modifier.align(Alignment.BottomCenter).padding(bottom = 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(color = Blanco, strokeWidth = 2.dp, modifier = Modifier.size(22.dp))
            Spacer(Modifier.height(14.dp))
            Text("Jiquilpan, Michoacán", style = MaterialTheme.typography.labelSmall, color = Terracota.t100)
        }
    }
}
