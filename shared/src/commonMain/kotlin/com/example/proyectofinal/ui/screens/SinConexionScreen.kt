package com.example.proyectofinal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.proyectofinal.ui.components.Aviso
import com.example.proyectofinal.ui.components.BotonPrincipal
import com.example.proyectofinal.ui.components.EncabezadoPantalla
import com.example.proyectofinal.ui.components.Etiqueta
import com.example.proyectofinal.ui.components.Icono
import com.example.proyectofinal.ui.theme.Blanco
import com.example.proyectofinal.ui.theme.Crema
import com.example.proyectofinal.ui.theme.Neutro
import com.example.proyectofinal.ui.theme.Terracota
import proyectofinal.shared.generated.resources.*

/**
 * Sin conexión (boceto 05). Hoy los datos viven en el teléfono y nunca hace falta internet,
 * así que se abre desde Ajustes como vista previa; queda lista para cuando haya un servidor.
 */
@Composable
fun SinConexionScreen(onVolver: () -> Unit, onReintentar: () -> Unit, onVerGuardados: () -> Unit) {
    Column(Modifier.fillMaxSize().background(Crema).safeDrawingPadding()) {
        EncabezadoPantalla("", onVolver) { Etiqueta("Sin conexión", Terracota.t100, Terracota.t700, Res.drawable.ic_sin_conexion) }
        Column(
            Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))
            Box(Modifier.size(132.dp).clip(CircleShape).background(Terracota.t100), contentAlignment = Alignment.Center) {
                Box(Modifier.size(84.dp).clip(RoundedCornerShape(24.dp)).background(Blanco), contentAlignment = Alignment.Center) {
                    Icono(Res.drawable.ic_sin_conexion, null, Terracota.t500, Modifier.size(42.dp))
                }
            }
            Spacer(Modifier.height(24.dp))
            Text("Parece que no hay señal", style = MaterialTheme.typography.displaySmall, color = Neutro.t500, textAlign = TextAlign.Center)
            Spacer(Modifier.height(8.dp))
            Text(
                "Revisa tus datos móviles o el Wi-Fi. Mientras tanto, tus oficios guardados siguen disponibles.",
                style = MaterialTheme.typography.bodyLarge, color = Neutro.t400, textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(24.dp))
            Aviso(
                Res.drawable.ic_telefono, "Consejo local",
                "Para emergencias de plomería o electricidad, la llamada normal no necesita datos móviles."
            )
        }
        Column(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp)) {
            BotonPrincipal("Reintentar", onReintentar)
            Spacer(Modifier.height(10.dp))
            OutlinedButton(onClick = onVerGuardados, modifier = Modifier.fillMaxWidth().height(52.dp), shape = MaterialTheme.shapes.small) {
                Text("Ver mis oficios guardados", style = MaterialTheme.typography.labelLarge, color = Neutro.t500)
            }
        }
    }
}
