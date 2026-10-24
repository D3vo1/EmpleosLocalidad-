package com.example.proyectofinal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.proyectofinal.ui.components.Aviso
import com.example.proyectofinal.ui.components.BotonPrincipal
import com.example.proyectofinal.ui.components.EncabezadoPantalla
import com.example.proyectofinal.ui.components.Icono
import com.example.proyectofinal.ui.components.OpcionPastilla
import com.example.proyectofinal.ui.theme.Blanco
import com.example.proyectofinal.ui.theme.Crema
import com.example.proyectofinal.ui.theme.Neutro
import com.example.proyectofinal.ui.theme.Terracota
import proyectofinal.shared.generated.resources.*

/**
 * Ubicación (boceto 01 · "Permiso de ubicación"). La app no usa GPS, así que en lugar
 * de pedir el permiso del sistema pide la colonia: sus negocios aparecen primero en Explorar.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PermisoUbicacionScreen(
    colonias: List<String>,
    actual: String?,
    onVolver: (() -> Unit)?,
    onElegir: (String?) -> Unit
) {
    var elegida by remember { mutableStateOf(actual) }
    Column(Modifier.fillMaxSize().background(Crema).safeDrawingPadding()) {
        if (onVolver != null) EncabezadoPantalla("", onVolver) else Spacer(Modifier.height(16.dp))
        Column(
            Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(Modifier.size(120.dp).clip(CircleShape).background(Terracota.t100), contentAlignment = Alignment.Center) {
                Box(Modifier.size(76.dp).clip(RoundedCornerShape(22.dp)).background(Blanco), contentAlignment = Alignment.Center) {
                    Icono(Res.drawable.ic_ubicacion, null, Terracota.t500, Modifier.size(38.dp))
                }
            }
            Spacer(Modifier.height(20.dp))
            Text("Encuentra oficios cerca de ti", style = MaterialTheme.typography.displaySmall, color = Neutro.t500, textAlign = TextAlign.Center)
            Spacer(Modifier.height(8.dp))
            Text(
                "Elige tu colonia y te mostramos primero a los maestros y talleres que te quedan más cerca.",
                style = MaterialTheme.typography.bodyLarge, color = Neutro.t400, textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(20.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                colonias.forEach { c ->
                    OpcionPastilla(c, elegida == c, Res.drawable.ic_ubicacion) { elegida = if (elegida == c) null else c }
                }
            }
            Spacer(Modifier.height(20.dp))
            Aviso(
                Res.drawable.ic_candado, "Tu privacidad es local",
                "Solo usamos tu colonia mientras exploras el directorio. Tus datos nunca se venden ni se comparten."
            )
            Spacer(Modifier.height(16.dp))
        }
        Column(Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
            BotonPrincipal(
                elegida?.let { "Usar $it" } ?: "Elige una colonia",
                onClick = { onElegir(elegida) },
                habilitado = elegida != null
            )
            Text(
                "Ahora no", style = MaterialTheme.typography.labelLarge, color = Neutro.t400,
                modifier = Modifier.align(Alignment.CenterHorizontally).clip(RoundedCornerShape(8.dp))
                    .clickable { onElegir(null) }.padding(horizontal = 16.dp, vertical = 12.dp)
            )
        }
    }
}
