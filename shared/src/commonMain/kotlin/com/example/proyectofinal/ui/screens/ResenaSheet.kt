package com.example.proyectofinal.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.proyectofinal.ui.components.BotonPrincipal
import com.example.proyectofinal.ui.components.CampoArea
import com.example.proyectofinal.ui.components.Icono
import com.example.proyectofinal.ui.theme.Ambar
import com.example.proyectofinal.ui.theme.Blanco
import com.example.proyectofinal.ui.theme.Neutro
import com.example.proyectofinal.ui.theme.Terracota
import proyectofinal.shared.generated.resources.*

private val TEXTO_ESTRELLAS = listOf("", "Mala experiencia", "Regular", "Buena", "Muy buena", "Excelente")

/**
 * Hoja para dejar una reseña. [onPublicar] devuelve null si se guardó, o el mensaje
 * de error del caso de uso (ya reseñaste, inicia sesión, etc.) para mostrarlo aquí.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResenaSheet(nombreNegocio: String, onPublicar: (Int, String) -> String?, onCerrar: () -> Unit) {
    var estrellas by remember { mutableStateOf(0) }
    var comentario by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    ModalBottomSheet(
        onDismissRequest = onCerrar,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Blanco
    ) {
        Column(Modifier.fillMaxWidth().padding(horizontal = 20.dp).navigationBarsPadding().imePadding()) {
            Text("¿Cómo te fue con $nombreNegocio?", style = MaterialTheme.typography.titleLarge, color = Neutro.t500)
            Spacer(Modifier.height(4.dp))
            Text("Tu reseña ayuda a otros vecinos a elegir.", style = MaterialTheme.typography.bodyMedium, color = Neutro.t400)
            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                (1..5).forEach { i ->
                    IconButton(onClick = { estrellas = i; error = null }, modifier = Modifier.size(48.dp)) {
                        Icono(Res.drawable.ic_estrella, "$i estrellas", if (i <= estrellas) Ambar.t500 else Neutro.t100, Modifier.size(36.dp))
                    }
                }
            }
            Text(
                if (estrellas == 0) "Toca las estrellas para calificar" else TEXTO_ESTRELLAS[estrellas],
                style = MaterialTheme.typography.labelMedium, color = if (estrellas == 0) Neutro.t300 else Ambar.t700
            )
            Spacer(Modifier.height(16.dp))
            CampoArea("Comentario (opcional)", comentario, { comentario = it },
                "¿Fue puntual? ¿Respetó el presupuesto? ¿Quedó bien el trabajo?", maximo = 500)
            error?.let {
                Spacer(Modifier.height(10.dp))
                Text(it, style = MaterialTheme.typography.bodyMedium, color = Terracota.t700)
            }
            Spacer(Modifier.height(16.dp))
            BotonPrincipal(
                "Publicar reseña",
                onClick = { error = onPublicar(estrellas, comentario) },
                habilitado = estrellas > 0
            )
            Spacer(Modifier.height(16.dp))
        }
    }
}
