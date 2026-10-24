package com.example.proyectofinal.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.proyectofinal.domain.model.NegocioResumen
import com.example.proyectofinal.ui.theme.Blanco
import com.example.proyectofinal.ui.theme.Borde
import com.example.proyectofinal.ui.theme.Neutro
import com.example.proyectofinal.ui.theme.Oliva
import com.example.proyectofinal.ui.theme.Terracota
import proyectofinal.shared.generated.resources.*

/**
 * Tarjeta de la lista de Explorar (boceto "03 · Explorar y contactar"):
 * foto, nombre, oficio, estrellas, "Desde $X MXN", colonia, estado y botones de contacto.
 */
@Composable
fun TarjetaNegocio(
    resumen: NegocioResumen,
    abierto: Boolean,
    esFavorito: Boolean,
    onAbrir: () -> Unit,
    onFavorito: () -> Unit,
    onMensaje: () -> Unit,
    onLlamar: () -> Unit
) {
    val n = resumen.negocio
    Surface(
        onClick = onAbrir,
        shape = MaterialTheme.shapes.medium,
        color = Blanco,
        border = BorderStroke(1.dp, Borde),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(12.dp)) {
            Row {
                FotoNegocio(n.categoria, Modifier.size(92.dp).clip(RoundedCornerShape(12.dp)))
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.Top) {
                        Text(
                            n.nombre, style = MaterialTheme.typography.titleMedium, color = Neutro.t500,
                            maxLines = 2, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = onFavorito, modifier = Modifier.size(28.dp)) {
                            Icono(
                                if (esFavorito) Res.drawable.ic_corazon_lleno else Res.drawable.ic_corazon,
                                if (esFavorito) "Quitar de favoritos" else "Guardar en favoritos",
                                if (esFavorito) Terracota.t500 else Neutro.t300,
                                Modifier.size(20.dp)
                            )
                        }
                    }
                    Text(n.oficio, style = MaterialTheme.typography.bodySmall, color = Neutro.t400, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Spacer(Modifier.height(4.dp))
                    Estrellas(resumen.calificacion.promedio, resumen.calificacion.total)
                    Spacer(Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        resumen.precioDesde?.let {
                            Text("Desde ${formatearPrecio(it)} MXN", style = MaterialTheme.typography.titleSmall, color = Terracota.t600)
                            Punto()
                        }
                        Text(n.colonia, style = MaterialTheme.typography.bodySmall, color = Neutro.t400, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                }
            }
            Spacer(Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(8.dp).clip(CircleShape).background(if (abierto) Oliva.t500 else Neutro.t200))
                Spacer(Modifier.width(6.dp))
                Text(
                    if (abierto) "Abierto ahora" else "Cerrado",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (abierto) Oliva.t600 else Neutro.t300,
                    modifier = Modifier.weight(1f)
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    BotonContacto("Mensaje", Res.drawable.ic_mensaje, principal = false, onClick = onMensaje)
                    BotonContacto("Llamar", Res.drawable.ic_telefono, principal = true, onClick = onLlamar)
                }
            }
        }
    }
}
