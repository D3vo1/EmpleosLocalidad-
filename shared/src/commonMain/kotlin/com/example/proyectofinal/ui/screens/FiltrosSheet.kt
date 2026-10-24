package com.example.proyectofinal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.proyectofinal.domain.model.Categoria
import com.example.proyectofinal.domain.usecase.FiltrosNegocio
import com.example.proyectofinal.ui.components.BotonPrincipal
import com.example.proyectofinal.ui.components.Etiqueta
import com.example.proyectofinal.ui.components.Icono
import com.example.proyectofinal.ui.components.formatearPrecio
import com.example.proyectofinal.ui.components.icono
import com.example.proyectofinal.ui.theme.Ambar
import com.example.proyectofinal.ui.theme.Blanco
import com.example.proyectofinal.ui.theme.Borde
import com.example.proyectofinal.ui.theme.Neutro
import com.example.proyectofinal.ui.theme.Terracota
import proyectofinal.shared.generated.resources.*

/** Topes del slider de precio; el último (null) significa "Sin límite". */
private val TOPES_PRECIO: List<Double?> = listOf(200.0, 300.0, 500.0, 800.0, 1500.0, 3000.0, null)
private val CALIFICACIONES = listOf(0.0 to "Todas", 3.0 to "3+", 4.0 to "4+", 4.5 to "4.5+")

/**
 * Hoja de filtros (boceto "Filtros"). Se edita una copia y solo se aplica con
 * "Ver resultados", para que la lista de atrás no salte mientras se elige.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FiltrosSheet(
    iniciales: FiltrosNegocio,
    contar: (FiltrosNegocio) -> Int,
    onAplicar: (FiltrosNegocio) -> Unit,
    onCerrar: () -> Unit
) {
    var borrador by remember { mutableStateOf(iniciales) }
    val total = contar(borrador)
    val indicePrecio = TOPES_PRECIO.indexOf(borrador.precioMaximo).coerceAtLeast(0)
        .let { if (borrador.precioMaximo == null) TOPES_PRECIO.lastIndex else it }

    ModalBottomSheet(
        onDismissRequest = onCerrar,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Blanco
    ) {
        Column(
            Modifier.fillMaxWidth().verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp).navigationBarsPadding()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Filtros de búsqueda", style = MaterialTheme.typography.titleLarge, color = Neutro.t500)
                Spacer(Modifier.width(8.dp))
                val activos = borrador.categorias.size + (if (borrador.calificacionMinima > 0) 1 else 0) +
                    (if (borrador.precioMaximo != null) 1 else 0)
                if (activos > 0) Etiqueta("$activos ${if (activos == 1) "aplicado" else "aplicados"}", Terracota.t50, Terracota.t700)
                Spacer(Modifier.weight(1f))
                Text(
                    "Limpiar", style = MaterialTheme.typography.labelMedium, color = Terracota.t600,
                    modifier = Modifier.clip(RoundedCornerShape(8.dp)).clickable { borrador = FiltrosNegocio() }.padding(8.dp)
                )
            }

            Seccion("Categorías de oficio", "Puedes elegir varias")
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Categoria.entries.forEach { cat ->
                    val elegida = cat in borrador.categorias
                    OpcionFiltro(cat.nombreVisible, elegida, cat) {
                        borrador = borrador.copy(
                            categorias = if (elegida) borrador.categorias - cat else borrador.categorias + cat
                        )
                    }
                }
            }

            Seccion("Calificación mínima", null)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                CALIFICACIONES.forEach { (valor, texto) ->
                    val elegida = borrador.calificacionMinima == valor
                    Row(
                        Modifier.weight(1f).clip(RoundedCornerShape(12.dp))
                            .background(if (elegida) Terracota.t50 else Blanco)
                            .border(1.dp, if (elegida) Terracota.t500 else Borde, RoundedCornerShape(12.dp))
                            .clickable { borrador = borrador.copy(calificacionMinima = valor) }
                            .padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(texto, style = MaterialTheme.typography.labelMedium, color = if (elegida) Terracota.t700 else Neutro.t500)
                        if (valor > 0) {
                            Spacer(Modifier.width(3.dp))
                            Icono(Res.drawable.ic_estrella, null, Ambar.t500, Modifier.size(13.dp))
                        }
                    }
                }
            }

            Seccion("Precio máximo por servicio", null)
            Text(
                borrador.precioMaximo?.let { "Hasta ${formatearPrecio(it)} MXN" } ?: "Sin límite",
                style = MaterialTheme.typography.titleMedium, color = Terracota.t600
            )
            Slider(
                value = indicePrecio.toFloat(),
                onValueChange = { borrador = borrador.copy(precioMaximo = TOPES_PRECIO[it.toInt().coerceIn(0, TOPES_PRECIO.lastIndex)]) },
                valueRange = 0f..TOPES_PRECIO.lastIndex.toFloat(),
                steps = TOPES_PRECIO.size - 2,
                colors = SliderDefaults.colors(
                    thumbColor = Terracota.t500,
                    activeTrackColor = Terracota.t500,
                    inactiveTrackColor = Terracota.t100,
                    activeTickColor = Terracota.t300,
                    inactiveTickColor = Terracota.t200
                )
            )
            Row(Modifier.fillMaxWidth()) {
                Text("Económico", style = MaterialTheme.typography.bodySmall, color = Neutro.t300, modifier = Modifier.weight(1f))
                Text("Sin límite", style = MaterialTheme.typography.bodySmall, color = Neutro.t300)
            }

            Spacer(Modifier.height(24.dp))
            BotonPrincipal(
                if (total == 0) "Sin resultados con estos filtros" else "Ver $total ${if (total == 1) "resultado" else "resultados"}",
                onClick = { onAplicar(borrador) },
                habilitado = total > 0
            )
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun Seccion(titulo: String, ayuda: String?) {
    Spacer(Modifier.height(22.dp))
    Row(verticalAlignment = Alignment.Bottom) {
        Text(titulo, style = MaterialTheme.typography.titleMedium, color = Neutro.t500, modifier = Modifier.weight(1f))
        if (ayuda != null) Text(ayuda, style = MaterialTheme.typography.bodySmall, color = Neutro.t300)
    }
    Spacer(Modifier.height(10.dp))
}

@Composable
private fun OpcionFiltro(texto: String, elegida: Boolean, categoria: Categoria, onClick: () -> Unit) {
    Row(
        Modifier.clip(CircleShape)
            .background(if (elegida) Terracota.t50 else Blanco)
            .border(1.dp, if (elegida) Terracota.t500 else Borde, CircleShape)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icono(
            if (elegida) Res.drawable.ic_check else categoria.icono(), null,
            if (elegida) Terracota.t600 else Neutro.t400, Modifier.size(15.dp)
        )
        Spacer(Modifier.width(6.dp))
        Text(texto, style = MaterialTheme.typography.labelMedium, color = if (elegida) Terracota.t700 else Neutro.t500)
    }
}
