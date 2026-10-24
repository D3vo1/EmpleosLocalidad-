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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.proyectofinal.data.AppContainer
import com.example.proyectofinal.domain.model.Negocio
import com.example.proyectofinal.domain.model.Resultado
import com.example.proyectofinal.domain.model.Servicio
import com.example.proyectofinal.domain.model.Usuario
import com.example.proyectofinal.domain.model.Vacante
import com.example.proyectofinal.domain.usecase.BuscarNegociosUseCase
import com.example.proyectofinal.domain.usecase.EstaAbiertoAhoraUseCase
import com.example.proyectofinal.ui.components.Aviso
import com.example.proyectofinal.ui.components.BarraInferiorNegocio
import com.example.proyectofinal.ui.components.EstadoVacio
import com.example.proyectofinal.ui.components.Estrellas
import com.example.proyectofinal.ui.components.Etiqueta
import com.example.proyectofinal.ui.components.Icono
import com.example.proyectofinal.ui.components.PestanaNegocio
import com.example.proyectofinal.ui.components.coloresCampo
import com.example.proyectofinal.ui.components.coloresSwitch
import com.example.proyectofinal.ui.components.formatearPrecio
import com.example.proyectofinal.ui.theme.Ambar
import com.example.proyectofinal.ui.theme.Blanco
import com.example.proyectofinal.ui.theme.Borde
import com.example.proyectofinal.ui.theme.Crema
import com.example.proyectofinal.ui.theme.Neutro
import com.example.proyectofinal.ui.theme.Oliva
import com.example.proyectofinal.ui.theme.Terracota
import kotlinx.coroutines.launch
import proyectofinal.shared.generated.resources.*

/**
 * Pantalla principal del dueño de negocio (boceto 04): Panel, Servicios, Vacantes y Ajustes.
 * [version] sube cada vez que algo cambia para volver a leer los datos del repositorio.
 */
@Composable
fun PrincipalNegocioScreen(
    container: AppContainer,
    usuario: Usuario,
    pestana: PestanaNegocio,
    onPestana: (PestanaNegocio) -> Unit,
    onVerPerfil: (String) -> Unit,
    onNuevoServicio: () -> Unit,
    onEditarServicio: (String) -> Unit,
    onNuevaVacante: () -> Unit,
    mensajeInicial: String?,
    onMensajeMostrado: () -> Unit,
    ajustes: @Composable () -> Unit
) {
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val avisar: (String) -> Unit = { msg -> scope.launch { snackbar.showSnackbar(msg) } }
    var version by remember { mutableStateOf(0) }
    val negocio = remember(version) { container.negocioDelDueno(usuario) }
    // Mensaje que deja un formulario al regresar ("Servicio publicado", "Vacante publicada")
    LaunchedEffect(mensajeInicial) {
        if (mensajeInicial != null) {
            // Se lanza en el scope de la pantalla para que no se cancele al limpiar el mensaje
            scope.launch { snackbar.showSnackbar(mensajeInicial) }
            onMensajeMostrado()
        }
    }

    Scaffold(
        containerColor = Crema,
        snackbarHost = { SnackbarHost(snackbar) },
        bottomBar = { BarraInferiorNegocio(pestana, onPestana) },
        floatingActionButton = {
            when (pestana) {
                PestanaNegocio.SERVICIOS -> BotonFlotante("Agregar servicio", onNuevoServicio)
                PestanaNegocio.VACANTES -> BotonFlotante("Publicar vacante", onNuevaVacante)
                else -> {}
            }
        }
    ) { relleno ->
        Box(Modifier.fillMaxSize().padding(relleno)) {
            if (pestana == PestanaNegocio.AJUSTES) {
                ajustes()
            } else if (negocio == null) {
                EstadoVacio(
                    Res.drawable.ic_tienda, "Tu cuenta aún no tiene negocio",
                    "No encontramos un negocio ligado a esta cuenta. Revisa en Ajustes con qué cuenta entraste."
                )
            } else when (pestana) {
                PestanaNegocio.PANEL -> PanelContenido(container, negocio, usuario, version, onVerPerfil,
                    onNuevoServicio, onNuevaVacante, onIrServicios = { onPestana(PestanaNegocio.SERVICIOS) })
                PestanaNegocio.SERVICIOS -> ServiciosContenido(container, negocio, usuario, version, onEditarServicio) { msg ->
                    version++; avisar(msg)
                }
                PestanaNegocio.VACANTES -> VacantesContenido(container, negocio, usuario, version) { msg ->
                    version++; avisar(msg)
                }
                PestanaNegocio.AJUSTES -> ajustes()
            }
        }
    }
}

@Composable
private fun BotonFlotante(texto: String, onClick: () -> Unit) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        containerColor = Terracota.t500,
        contentColor = Blanco,
        shape = RoundedCornerShape(16.dp),
        icon = { Icono(Res.drawable.ic_mas, null, Blanco, Modifier.size(20.dp)) },
        text = { Text(texto, style = MaterialTheme.typography.labelLarge) }
    )
}

@Composable
private fun PanelContenido(
    container: AppContainer,
    negocio: Negocio,
    usuario: Usuario,
    version: Int,
    onVerPerfil: (String) -> Unit,
    onNuevoServicio: () -> Unit,
    onNuevaVacante: () -> Unit,
    onIrServicios: () -> Unit
) {
    val detalle = remember(version, negocio.id) { container.obtenerDetalle(negocio.id) }
    val servicios = remember(version, negocio.id) { container.servicios.listar(negocio.id) }
    val vacantes = remember(version, negocio.id) { container.vacantes.listar(negocio.id) }
    val abierto = remember(negocio) { EstaAbiertoAhoraUseCase()(negocio.horarios) }

    LazyColumn(
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Hola, ${usuario.nombre.substringBefore(' ')}", style = MaterialTheme.typography.bodyMedium, color = Neutro.t400)
                    Text(negocio.nombre, style = MaterialTheme.typography.displaySmall, color = Neutro.t500)
                    Spacer(Modifier.height(6.dp))
                    Etiqueta(
                        if (abierto) "Abierto ahora" else "Cerrado ahora",
                        if (abierto) Oliva.t100 else Neutro.t50,
                        if (abierto) Oliva.t700 else Neutro.t400,
                        Res.drawable.ic_reloj
                    )
                }
                Box(Modifier.size(52.dp).clip(CircleShape).background(Terracota.t100), contentAlignment = Alignment.Center) {
                    Text(usuario.nombre.take(1).uppercase(), style = MaterialTheme.typography.headlineSmall, color = Terracota.t700)
                }
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AccionRapida("Agregar servicio", "Nuevos trabajos y precios", Modifier.weight(1f), onNuevoServicio)
                AccionRapida("Publicar vacante", "Oficial o aprendiz", Modifier.weight(1f), onNuevaVacante)
            }
        }
        item {
            Tarjeta {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(44.dp).clip(CircleShape).background(Ambar.t50), contentAlignment = Alignment.Center) {
                        Icono(Res.drawable.ic_estrella, null, Ambar.t500, Modifier.size(24.dp))
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text("Tu calificación", style = MaterialTheme.typography.bodySmall, color = Neutro.t400)
                        if (detalle != null) Estrellas(detalle.calificacion.promedio, detalle.calificacion.total)
                    }
                }
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Metrica("${servicios.count { it.activo }}", "Servicios activos", Modifier.weight(1f), onIrServicios)
                Metrica("${vacantes.count { it.activa }}", "Vacantes abiertas", Modifier.weight(1f), null)
            }
        }
        item {
            Surface(
                onClick = { onVerPerfil(negocio.id) },
                shape = MaterialTheme.shapes.small,
                color = Blanco,
                border = BorderStroke(1.dp, Borde),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icono(Res.drawable.ic_ojo, null, Terracota.t500, Modifier.size(20.dp))
                    Spacer(Modifier.width(10.dp))
                    Text("Ver mi perfil como lo ven los vecinos", style = MaterialTheme.typography.labelLarge, color = Neutro.t500, modifier = Modifier.weight(1f))
                    Icono(Res.drawable.ic_adelante, null, Neutro.t400, Modifier.size(18.dp))
                }
            }
        }
        item { Text("Reseñas recientes", style = MaterialTheme.typography.headlineSmall, color = Neutro.t500) }
        val recientes = detalle?.resenas?.take(3).orEmpty()
        if (recientes.isEmpty()) {
            item { Text("Aún no tienes reseñas. Pide a tus clientes que te califiquen en la app.", style = MaterialTheme.typography.bodyMedium, color = Neutro.t400) }
        }
        items(recientes, key = { it.id }) { r ->
            Tarjeta {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(r.nombreUsuario, style = MaterialTheme.typography.titleSmall, color = Neutro.t500, modifier = Modifier.weight(1f))
                        Row { repeat(5) { i -> Icono(Res.drawable.ic_estrella, null, if (i < r.estrellas) Ambar.t500 else Neutro.t100, Modifier.size(13.dp)) } }
                    }
                    if (r.comentario.isNotBlank()) {
                        Spacer(Modifier.height(6.dp))
                        Text(r.comentario, style = MaterialTheme.typography.bodyMedium, color = Neutro.t500)
                    }
                }
            }
        }
        item {
            Aviso(
                Res.drawable.ic_mensaje, "Trato directo, sin comisiones",
                "Acuerda presupuestos, anticipos y visitas directamente por WhatsApp. Tu talento y tus ganancias son tuyos."
            )
        }
    }
}

@Composable
private fun AccionRapida(titulo: String, subtitulo: String, modifier: Modifier, onClick: () -> Unit) {
    Column(
        modifier.clip(RoundedCornerShape(16.dp)).background(Terracota.t500).clickable(onClick = onClick).padding(14.dp)
    ) {
        Box(Modifier.size(32.dp).clip(CircleShape).background(Terracota.t400), contentAlignment = Alignment.Center) {
            Icono(Res.drawable.ic_mas, null, Blanco, Modifier.size(20.dp))
        }
        Spacer(Modifier.height(10.dp))
        Text(titulo, style = MaterialTheme.typography.titleMedium, color = Blanco)
        Text(subtitulo, style = MaterialTheme.typography.bodySmall, color = Terracota.t100)
    }
}

@Composable
private fun Metrica(valor: String, etiqueta: String, modifier: Modifier, onClick: (() -> Unit)?) {
    Column(
        modifier.clip(RoundedCornerShape(16.dp)).background(Blanco)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(16.dp)
    ) {
        Text(valor, style = MaterialTheme.typography.displaySmall, color = Neutro.t500)
        Text(etiqueta, style = MaterialTheme.typography.bodySmall, color = Neutro.t400)
    }
}

@Composable
private fun Tarjeta(contenido: @Composable () -> Unit) {
    Surface(shape = MaterialTheme.shapes.medium, color = Blanco, border = BorderStroke(1.dp, Borde), modifier = Modifier.fillMaxWidth()) {
        Box(Modifier.padding(16.dp)) { contenido() }
    }
}

@Composable
private fun ServiciosContenido(
    container: AppContainer,
    negocio: Negocio,
    usuario: Usuario,
    version: Int,
    onEditar: (String) -> Unit,
    onCambio: (String) -> Unit
) {
    var busqueda by remember { mutableStateOf("") }
    val todos = remember(version, negocio.id) { container.servicios.listar(negocio.id) }
    val texto = BuscarNegociosUseCase.normalizar(busqueda)
    val lista = todos.filter { texto.isBlank() || BuscarNegociosUseCase.normalizar(it.nombre + " " + it.descripcion).contains(texto) }

    LazyColumn(
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Text("Mis servicios", style = MaterialTheme.typography.displaySmall, color = Neutro.t500)
            Text(
                "${todos.count { it.activo }} de ${todos.size} visibles para los vecinos de Jiquilpan y la Ciénega.",
                style = MaterialTheme.typography.bodyMedium, color = Neutro.t400
            )
        }
        if (todos.size > 3) {
            item {
                OutlinedTextField(
                    value = busqueda, onValueChange = { busqueda = it }, singleLine = true,
                    placeholder = { Text("Buscar en mis servicios") },
                    leadingIcon = { Icono(Res.drawable.ic_buscar, null, Neutro.t300) },
                    modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.small, colors = coloresCampo()
                )
            }
        }
        if (todos.isEmpty()) {
            item {
                EstadoVacio(Res.drawable.ic_lista, "Aún no publicas servicios",
                    "Agrega lo que haces y un precio aproximado para que los vecinos te encuentren.")
            }
        }
        items(lista, key = { it.id }) { s ->
            TarjetaServicioNegocio(
                s,
                onEditar = { onEditar(s.id) },
                onActivo = { activo ->
                    when (val r = container.servicios.cambiarActivo(s.id, usuario.id, activo)) {
                        is Resultado.Exito -> onCambio(if (activo) "\"${s.nombre}\" ya es visible" else "\"${s.nombre}\" quedó oculto")
                        is Resultado.Error -> onCambio(r.mensaje)
                    }
                }
            )
        }
        if (todos.isNotEmpty()) {
            item {
                Aviso(Res.drawable.ic_info, "Consejo",
                    "Ocultar un servicio no lo borra: apágalo cuando no tengas material o agenda, y vuelve a encenderlo después.", verde = false)
            }
        }
    }
}

@Composable
private fun TarjetaServicioNegocio(s: Servicio, onEditar: () -> Unit, onActivo: (Boolean) -> Unit) {
    Tarjeta {
        Column {
            Row(verticalAlignment = Alignment.Top) {
                Column(Modifier.weight(1f)) {
                    Text(s.nombre, style = MaterialTheme.typography.titleMedium, color = if (s.activo) Neutro.t500 else Neutro.t300)
                    if (s.descripcion.isNotBlank()) {
                        Text(s.descripcion, style = MaterialTheme.typography.bodySmall, color = Neutro.t400, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    }
                }
                IconButton(onClick = onEditar, modifier = Modifier.size(36.dp)) {
                    Icono(Res.drawable.ic_editar, "Editar ${s.nombre}", Neutro.t400, Modifier.size(18.dp))
                }
            }
            Spacer(Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "${if (s.esPrecioDesde) "Desde " else ""}${formatearPrecio(s.precioAprox)} MXN",
                    style = MaterialTheme.typography.titleSmall, color = Terracota.t600, modifier = Modifier.weight(1f)
                )
                Text(if (s.activo) "Visible" else "Oculto", style = MaterialTheme.typography.labelSmall, color = Neutro.t400)
                Spacer(Modifier.width(8.dp))
                Switch(checked = s.activo, onCheckedChange = onActivo, colors = coloresSwitch())
            }
        }
    }
}

@Composable
private fun VacantesContenido(
    container: AppContainer,
    negocio: Negocio,
    usuario: Usuario,
    version: Int,
    onCambio: (String) -> Unit
) {
    val lista = remember(version, negocio.id) { container.vacantes.listar(negocio.id) }
    LazyColumn(
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Text("Mis vacantes", style = MaterialTheme.typography.displaySmall, color = Neutro.t500)
            Text("Las vacantes abiertas aparecen en la pestaña Vacantes de tu perfil.", style = MaterialTheme.typography.bodyMedium, color = Neutro.t400)
        }
        if (lista.isEmpty()) {
            item {
                EstadoVacio(Res.drawable.ic_maletin, "No tienes vacantes",
                    "Cuando necesites un ayudante u oficial, publícalo aquí y los vecinos interesados te escribirán.")
            }
        }
        items(lista, key = { it.id }) { v ->
            TarjetaVacanteNegocio(v) { activa ->
                when (val r = container.vacantes.cambiarActiva(v.id, usuario.id, activa)) {
                    is Resultado.Exito -> onCambio(if (activa) "Vacante reabierta" else "Vacante cerrada")
                    is Resultado.Error -> onCambio(r.mensaje)
                }
            }
        }
    }
}

@Composable
private fun TarjetaVacanteNegocio(v: Vacante, onActiva: (Boolean) -> Unit) {
    Tarjeta {
        Column {
            Text(v.puesto, style = MaterialTheme.typography.titleMedium, color = if (v.activa) Neutro.t500 else Neutro.t300)
            Text(v.descripcion, style = MaterialTheme.typography.bodySmall, color = Neutro.t400, maxLines = 2, overflow = TextOverflow.Ellipsis)
            Spacer(Modifier.height(8.dp))
            if (v.horario.isNotBlank()) Text(v.horario, style = MaterialTheme.typography.bodySmall, color = Neutro.t500)
            Spacer(Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    v.sueldoAprox?.let { "Sueldo aprox. ${formatearPrecio(it)} MXN" } ?: "Sueldo a tratar",
                    style = MaterialTheme.typography.titleSmall, color = Terracota.t600, modifier = Modifier.weight(1f)
                )
                Text(if (v.activa) "Abierta" else "Cerrada", style = MaterialTheme.typography.labelSmall, color = Neutro.t400)
                Spacer(Modifier.width(8.dp))
                Switch(checked = v.activa, onCheckedChange = onActiva, colors = coloresSwitch())
            }
        }
    }
}
