package com.example.proyectofinal.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.proyectofinal.domain.model.DetalleNegocio
import com.example.proyectofinal.domain.model.DiaSemana
import com.example.proyectofinal.domain.model.Resena
import com.example.proyectofinal.domain.model.Servicio
import com.example.proyectofinal.domain.model.Vacante
import com.example.proyectofinal.ui.components.EstadoVacio
import com.example.proyectofinal.ui.components.Estrellas
import com.example.proyectofinal.ui.components.Etiqueta
import com.example.proyectofinal.ui.components.FotoNegocio
import com.example.proyectofinal.ui.components.Icono
import com.example.proyectofinal.ui.components.formatearHora
import com.example.proyectofinal.ui.components.formatearPrecio
import com.example.proyectofinal.ui.components.formatearTelefono
import com.example.proyectofinal.ui.components.icono
import com.example.proyectofinal.ui.components.rememberContacto
import com.example.proyectofinal.ui.components.textoHorarioHoy
import com.example.proyectofinal.ui.theme.Ambar
import com.example.proyectofinal.ui.theme.Blanco
import com.example.proyectofinal.ui.theme.Borde
import com.example.proyectofinal.ui.theme.Crema
import com.example.proyectofinal.ui.theme.Neutro
import com.example.proyectofinal.ui.theme.Oliva
import com.example.proyectofinal.ui.theme.Terracota
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import proyectofinal.shared.generated.resources.*

private enum class PestanaDetalle(val titulo: String) { SERVICIOS("Servicios"), VACANTES("Vacantes"), RESENAS("Reseñas"), INFO("Información") }

/** Detalle de negocio (boceto 03): datos, contacto y pestañas de servicios, vacantes, reseñas e información. */
@Composable
fun DetalleNegocioScreen(
    detalle: DetalleNegocio?,
    esFavorito: Boolean,
    onVolver: () -> Unit,
    onFavorito: () -> String,
    onResena: (estrellas: Int, comentario: String) -> String? = { _, _ -> null }
) {
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val contacto = rememberContacto()
    val avisar: (String?) -> Unit = { msg -> if (msg != null) scope.launch { snackbar.showSnackbar(msg) } }
    var pestana by remember { mutableStateOf(PestanaDetalle.SERVICIOS) }
    var escribiendoResena by remember { mutableStateOf(false) }

    if (detalle == null) {
        Box(Modifier.fillMaxSize().background(Crema), contentAlignment = Alignment.Center) {
            EstadoVacio(Res.drawable.ic_buscar, "Este negocio ya no está disponible",
                "Puede que su dueño lo haya dado de baja.", accion = "Volver", onAccion = onVolver)
        }
        return
    }
    val n = detalle.negocio

    Scaffold(containerColor = Crema, snackbarHost = { SnackbarHost(snackbar) }) { relleno ->
        LazyColumn(
            Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = relleno.calculateBottomPadding() + 24.dp)
        ) {
            item {
                Box {
                    FotoNegocio(n.categoria, Modifier.fillMaxWidth().height(250.dp), tamanoIcono = 72)
                    Row(Modifier.fillMaxWidth().statusBarsPadding().padding(16.dp)) {
                        BotonRedondo(Res.drawable.ic_atras, "Volver", Neutro.t500, onVolver)
                        Spacer(Modifier.weight(1f))
                        BotonRedondo(
                            if (esFavorito) Res.drawable.ic_corazon_lleno else Res.drawable.ic_corazon,
                            if (esFavorito) "Quitar de favoritos" else "Guardar en favoritos",
                            if (esFavorito) Terracota.t500 else Neutro.t500
                        ) { avisar(onFavorito()) }
                    }
                }
            }
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Blanco,
                    border = BorderStroke(1.dp, Borde),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).offset(y = (-28).dp)
                ) {
                    Column(Modifier.padding(18.dp)) {
                        Etiqueta(n.categoria.nombreVisible, Terracota.t50, Terracota.t700, n.categoria.icono())
                        Spacer(Modifier.height(8.dp))
                        Text(n.nombre, style = MaterialTheme.typography.displaySmall, color = Neutro.t500)
                        Text(n.oficio, style = MaterialTheme.typography.bodyLarge, color = Neutro.t400)
                        Spacer(Modifier.height(8.dp))
                        Estrellas(detalle.calificacion.promedio, detalle.calificacion.total)
                        Spacer(Modifier.height(12.dp))
                        FilaDato(Res.drawable.ic_ubicacion, n.colonia, n.direccion)
                        Spacer(Modifier.height(8.dp))
                        FilaDato(
                            Res.drawable.ic_reloj,
                            if (detalle.abiertoAhora) "Abierto ahora" else "Cerrado",
                            textoHorarioHoy(n.horarios, detalle.abiertoAhora),
                            tinta = if (detalle.abiertoAhora) Oliva.t600 else Neutro.t400
                        )
                        Spacer(Modifier.height(16.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            AccionContacto(Res.drawable.ic_telefono, "Llamar", "Directo", Terracota.t50, Terracota.t600, Modifier.weight(1f)) {
                                avisar(contacto.llamar(n))
                            }
                            AccionContacto(Res.drawable.ic_mensaje, "WhatsApp", "Cotizar", Oliva.t100, Oliva.t700, Modifier.weight(1f)) {
                                avisar(contacto.whatsapp(n))
                            }
                            AccionContacto(Res.drawable.ic_ruta, "Cómo llegar", "Google Maps", Ambar.t50, Ambar.t800, Modifier.weight(1f)) {
                                avisar(contacto.comoLlegar(n))
                            }
                        }
                    }
                }
            }
            item {
                Row(Modifier.fillMaxWidth().offset(y = (-12).dp).padding(horizontal = 16.dp)) {
                    PestanaDetalle.entries.forEach { p ->
                        val cuenta = when (p) {
                            PestanaDetalle.VACANTES -> detalle.vacantes.size
                            PestanaDetalle.RESENAS -> detalle.resenas.size
                            else -> null
                        }
                        val activa = p == pestana
                        Column(
                            Modifier.weight(1f).clip(RoundedCornerShape(8.dp)).clickable { pestana = p }.padding(top = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                if (cuenta != null && cuenta > 0) "${p.titulo} ($cuenta)" else p.titulo,
                                style = MaterialTheme.typography.labelMedium,
                                color = if (activa) Terracota.t600 else Neutro.t400,
                                maxLines = 1
                            )
                            Spacer(Modifier.height(8.dp))
                            Box(Modifier.fillMaxWidth().height(2.dp).background(if (activa) Terracota.t500 else Borde))
                        }
                    }
                }
            }
            when (pestana) {
                PestanaDetalle.SERVICIOS -> {
                    if (detalle.servicios.isEmpty()) item { EstadoVacio(Res.drawable.ic_herramienta, "Sin servicios publicados", "Este negocio aún no publica sus precios. Pregúntale por WhatsApp.") }
                    items(detalle.servicios, key = { it.id }) { TarjetaServicio(it) }
                }
                PestanaDetalle.VACANTES -> {
                    if (detalle.vacantes.isEmpty()) item { EstadoVacio(Res.drawable.ic_usuario, "Sin vacantes por ahora", "Cuando este negocio busque personal, aparecerá aquí.") }
                    items(detalle.vacantes, key = { it.id }) { TarjetaVacante(it) }
                }
                PestanaDetalle.RESENAS -> {
                    item { ResumenResenas(detalle) }
                    item {
                        Surface(
                            onClick = { escribiendoResena = true },
                            shape = MaterialTheme.shapes.small, color = Terracota.t50,
                            border = BorderStroke(1.dp, Terracota.t200),
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp)
                        ) {
                            Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icono(Res.drawable.ic_editar, null, Terracota.t600, Modifier.size(18.dp))
                                Spacer(Modifier.width(8.dp))
                                Text("Escribir una reseña", style = MaterialTheme.typography.labelLarge, color = Terracota.t700)
                            }
                        }
                    }
                    items(detalle.resenas, key = { it.id }) { TarjetaResena(it) }
                }
                PestanaDetalle.INFO -> item { Informacion(detalle) }
            }
        }
    }

    if (escribiendoResena) {
        ResenaSheet(
            nombreNegocio = n.nombre,
            onPublicar = { estrellas, comentario ->
                val error = onResena(estrellas, comentario)
                if (error == null) {
                    escribiendoResena = false
                    avisar("Gracias, tu reseña ya es visible")
                }
                error
            },
            onCerrar = { escribiendoResena = false }
        )
    }
}

@Composable
private fun BotonRedondo(icono: DrawableResource, descripcion: String, tinta: Color, onClick: () -> Unit) {
    Box(
        Modifier.size(42.dp).clip(CircleShape).background(Blanco).clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) { Icono(icono, descripcion, tinta, Modifier.size(20.dp)) }
}

@Composable
private fun FilaDato(icono: DrawableResource, titulo: String, detalle: String, tinta: Color = Neutro.t500) {
    Row(verticalAlignment = Alignment.Top) {
        Icono(icono, null, Terracota.t500, Modifier.padding(top = 1.dp).size(16.dp))
        Spacer(Modifier.width(8.dp))
        Column {
            Text(titulo, style = MaterialTheme.typography.titleSmall, color = tinta)
            Text(detalle, style = MaterialTheme.typography.bodySmall, color = Neutro.t400)
        }
    }
}

@Composable
private fun AccionContacto(
    icono: DrawableResource, titulo: String, subtitulo: String,
    fondo: Color, tinta: Color, modifier: Modifier, onClick: () -> Unit
) {
    Column(
        modifier.clip(RoundedCornerShape(14.dp)).background(fondo).clickable(onClick = onClick).padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icono(icono, null, tinta, Modifier.size(20.dp))
        Spacer(Modifier.height(4.dp))
        Text(titulo, style = MaterialTheme.typography.labelMedium, color = tinta)
        Text(subtitulo, style = MaterialTheme.typography.bodySmall, color = Neutro.t400)
    }
}

@Composable
private fun Tarjeta(contenido: @Composable () -> Unit) {
    Surface(
        shape = MaterialTheme.shapes.medium, color = Blanco, border = BorderStroke(1.dp, Borde),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp)
    ) { Box(Modifier.padding(16.dp)) { contenido() } }
}

@Composable
private fun TarjetaServicio(s: Servicio) = Tarjeta {
    Column {
        Text(s.nombre, style = MaterialTheme.typography.titleMedium, color = Neutro.t500)
        if (s.descripcion.isNotBlank()) Text(s.descripcion, style = MaterialTheme.typography.bodyMedium, color = Neutro.t400)
        Spacer(Modifier.height(10.dp))
        Text(
            "${if (s.esPrecioDesde) "Desde " else ""}${formatearPrecio(s.precioAprox)} MXN",
            style = MaterialTheme.typography.titleSmall, color = Terracota.t600
        )
    }
}

@Composable
private fun TarjetaVacante(v: Vacante) = Tarjeta {
    Column {
        Text(v.puesto, style = MaterialTheme.typography.titleMedium, color = Neutro.t500)
        Text(v.descripcion, style = MaterialTheme.typography.bodyMedium, color = Neutro.t400)
        Spacer(Modifier.height(8.dp))
        if (v.horario.isNotBlank()) FilaDato(Res.drawable.ic_reloj, v.horario, v.sueldoAprox?.let { "Sueldo aprox. ${formatearPrecio(it)} MXN" } ?: "Sueldo a tratar")
        if (v.requisitos.isNotEmpty()) {
            Spacer(Modifier.height(8.dp))
            v.requisitos.forEach { Text("• $it", style = MaterialTheme.typography.bodySmall, color = Neutro.t500) }
        }
    }
}

@Composable
private fun ResumenResenas(d: DetalleNegocio) = Tarjeta {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(if (d.calificacion.total == 0) "–" else d.calificacion.promedio.toString(),
                style = MaterialTheme.typography.displaySmall, color = Neutro.t500)
            Text("${d.calificacion.total} reseñas", style = MaterialTheme.typography.bodySmall, color = Neutro.t400)
        }
        Spacer(Modifier.width(18.dp))
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            (5 downTo 1).forEach { estrellas ->
                val cuenta = d.calificacion.distribucion[estrellas] ?: 0
                val fraccion = if (d.calificacion.total == 0) 0f else cuenta.toFloat() / d.calificacion.total
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("$estrellas", style = MaterialTheme.typography.labelSmall, color = Neutro.t400, modifier = Modifier.width(12.dp))
                    Box(Modifier.weight(1f).height(6.dp).clip(CircleShape).background(Ambar.t50)) {
                        Box(Modifier.fillMaxWidth(fraccion).height(6.dp).clip(CircleShape).background(Ambar.t500))
                    }
                }
            }
        }
    }
}

@Composable
private fun TarjetaResena(r: Resena) = Tarjeta {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(32.dp).clip(CircleShape).background(Oliva.t100), contentAlignment = Alignment.Center) {
                Text(r.nombreUsuario.take(1), style = MaterialTheme.typography.titleSmall, color = Oliva.t700)
            }
            Spacer(Modifier.width(10.dp))
            Text(r.nombreUsuario, style = MaterialTheme.typography.titleSmall, color = Neutro.t500, modifier = Modifier.weight(1f))
            Row {
                repeat(5) { i -> Icono(Res.drawable.ic_estrella, null, if (i < r.estrellas) Ambar.t500 else Neutro.t100, Modifier.size(13.dp)) }
            }
        }
        if (r.comentario.isNotBlank()) {
            Spacer(Modifier.height(8.dp))
            Text(r.comentario, style = MaterialTheme.typography.bodyMedium, color = Neutro.t500)
        }
    }
}

@Composable
private fun Informacion(d: DetalleNegocio) = Tarjeta {
    val n = d.negocio
    Column {
        Text("Sobre el negocio", style = MaterialTheme.typography.titleMedium, color = Neutro.t500)
        Spacer(Modifier.height(4.dp))
        Text(n.descripcion, style = MaterialTheme.typography.bodyMedium, color = Neutro.t400)
        Spacer(Modifier.height(14.dp))
        FilaDato(Res.drawable.ic_ubicacion, "Dirección", n.direccion)
        Spacer(Modifier.height(8.dp))
        FilaDato(Res.drawable.ic_telefono, "Teléfono", formatearTelefono(n.telefono))
        Spacer(Modifier.height(14.dp))
        Text("Horario", style = MaterialTheme.typography.titleMedium, color = Neutro.t500)
        Spacer(Modifier.height(6.dp))
        DiaSemana.entries.forEach { dia ->
            val h = n.horarios.firstOrNull { it.dia == dia }
            Row(Modifier.fillMaxWidth().padding(vertical = 3.dp)) {
                Text(dia.nombreVisible, style = MaterialTheme.typography.bodyMedium, color = Neutro.t500, modifier = Modifier.weight(1f))
                Text(
                    when {
                        h == null -> "Cerrado"
                        h.apertura == 0 && h.cierre == 1440 -> "24 horas"
                        else -> "${formatearHora(h.apertura)} – ${formatearHora(h.cierre)}"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (h == null) Neutro.t300 else Neutro.t500
                )
            }
        }
    }
}
