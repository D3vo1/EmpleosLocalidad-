package com.example.proyectofinal.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.proyectofinal.domain.model.Negocio
import com.example.proyectofinal.domain.model.NegocioResumen
import com.example.proyectofinal.ui.components.BotonContacto
import com.example.proyectofinal.ui.components.Estrellas
import com.example.proyectofinal.ui.components.Etiqueta
import com.example.proyectofinal.ui.components.FotoNegocio
import com.example.proyectofinal.ui.components.Icono
import com.example.proyectofinal.ui.theme.Blanco
import com.example.proyectofinal.ui.theme.Borde
import com.example.proyectofinal.ui.theme.Neutro
import com.example.proyectofinal.ui.theme.Terracota
import proyectofinal.shared.generated.resources.*

/**
 * Pestaña Mapa: sin librería de mapas, los negocios se agrupan por colonia.
 * La colonia del usuario va primero y "Cómo llegar" abre Google Maps.
 */
@Composable
fun MapaColoniasContenido(
    negocios: List<NegocioResumen>,
    coloniaPreferida: String?,
    onAbrir: (String) -> Unit,
    onComoLlegar: (Negocio) -> Unit,
    onElegirColonia: () -> Unit
) {
    val grupos = negocios.groupBy { it.negocio.colonia.ifBlank { "Sin colonia" } }
        .toList()
        .sortedWith(compareBy({ it.first != coloniaPreferida }, { it.first }))

    LazyColumn(
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Text("Oficios por colonia", style = MaterialTheme.typography.displaySmall, color = Neutro.t500)
            Text(
                "${negocios.size} negocios en ${grupos.size} colonias de Jiquilpan.",
                style = MaterialTheme.typography.bodyMedium, color = Neutro.t400
            )
            Spacer(Modifier.height(8.dp))
            Row(
                Modifier.clip(RoundedCornerShape(10.dp)).background(Terracota.t50)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icono(Res.drawable.ic_ubicacion, null, Terracota.t600, Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text(
                    coloniaPreferida?.let { "Tu colonia: $it" } ?: "Elige tu colonia para verla primero",
                    style = MaterialTheme.typography.labelMedium, color = Terracota.t700
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "Cambiar", style = MaterialTheme.typography.labelMedium, color = Terracota.t500,
                    modifier = Modifier.clip(RoundedCornerShape(6.dp)).clickable(onClick = onElegirColonia)
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                )
            }
        }
        grupos.forEach { (colonia, lista) ->
            item(key = "encabezado-$colonia") {
                Row(Modifier.padding(top = 14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(colonia, style = MaterialTheme.typography.headlineSmall, color = Neutro.t500, modifier = Modifier.weight(1f))
                    if (colonia == coloniaPreferida) Etiqueta("Tu colonia", Terracota.t100, Terracota.t700)
                    Spacer(Modifier.width(6.dp))
                    Text("${lista.size}", style = MaterialTheme.typography.labelMedium, color = Neutro.t400)
                }
            }
            items(lista, key = { it.negocio.id }) { r -> FilaNegocio(r, { onAbrir(r.negocio.id) }, { onComoLlegar(r.negocio) }) }
        }
    }
}

@Composable
private fun FilaNegocio(r: NegocioResumen, onAbrir: () -> Unit, onComoLlegar: () -> Unit) {
    Surface(
        onClick = onAbrir, shape = MaterialTheme.shapes.medium, color = Blanco,
        border = BorderStroke(1.dp, Borde), modifier = Modifier.fillMaxWidth()
    ) {
        Row(Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
            FotoNegocio(r.negocio.categoria, Modifier.size(52.dp).clip(RoundedCornerShape(10.dp)), tamanoIcono = 24)
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text(r.negocio.nombre, style = MaterialTheme.typography.titleSmall, color = Neutro.t500, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(r.negocio.oficio, style = MaterialTheme.typography.bodySmall, color = Neutro.t400, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Estrellas(r.calificacion.promedio, r.calificacion.total, conTexto = false)
            }
            BotonContacto("Cómo llegar", Res.drawable.ic_ruta, principal = false, onClick = onComoLlegar)
        }
    }
}
