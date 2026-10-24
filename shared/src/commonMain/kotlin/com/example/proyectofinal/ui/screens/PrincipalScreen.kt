package com.example.proyectofinal.ui.screens

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.unit.dp
import com.example.proyectofinal.data.AppContainer
import com.example.proyectofinal.domain.model.Categoria
import com.example.proyectofinal.domain.model.Negocio
import com.example.proyectofinal.domain.model.NegocioResumen
import com.example.proyectofinal.domain.model.Usuario
import com.example.proyectofinal.domain.usecase.Orden
import com.example.proyectofinal.ui.components.BarraBusqueda
import com.example.proyectofinal.ui.components.BarraInferior
import com.example.proyectofinal.ui.components.BotonPrincipal
import com.example.proyectofinal.ui.components.ChipCategoria
import com.example.proyectofinal.ui.components.EstadoVacio
import com.example.proyectofinal.ui.components.Icono
import com.example.proyectofinal.ui.components.PestanaCliente
import com.example.proyectofinal.ui.components.TarjetaNegocio
import com.example.proyectofinal.ui.components.icono
import com.example.proyectofinal.ui.components.rememberContacto
import com.example.proyectofinal.ui.navigation.Sesion
import com.example.proyectofinal.ui.theme.Blanco
import com.example.proyectofinal.ui.theme.Crema
import com.example.proyectofinal.ui.theme.Neutro
import com.example.proyectofinal.ui.theme.Oliva
import com.example.proyectofinal.ui.theme.Terracota
import kotlinx.coroutines.launch
import proyectofinal.shared.generated.resources.*

/**
 * Pantalla principal del cliente: barra inferior con Explorar, Mapa, Favoritos y Ajustes.
 * [pestana] vive fuera para conservarse al volver del detalle.
 */
@Composable
fun PrincipalScreen(
    container: AppContainer,
    sesion: Sesion,
    explorar: ExplorarEstado,
    pestana: PestanaCliente,
    onPestana: (PestanaCliente) -> Unit,
    onAbrirNegocio: (String) -> Unit,
    onIniciarSesion: () -> Unit,
    onOfrecerServicio: () -> Unit,
    onCerrarSesion: () -> Unit,
    onElegirColonia: () -> Unit,
    ajustes: @Composable () -> Unit
) {
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val contacto = rememberContacto()
    val avisar: (String?) -> Unit = { msg -> if (msg != null) scope.launch { snackbar.showSnackbar(msg) } }
    var mostrarFiltros by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Crema,
        snackbarHost = { SnackbarHost(snackbar) },
        bottomBar = { BarraInferior(pestana, onPestana) },
        floatingActionButton = {
            if (pestana == PestanaCliente.EXPLORAR && sesion.usuario == null) {
                ExtendedFloatingActionButton(
                    onClick = onOfrecerServicio,
                    containerColor = Terracota.t600,
                    contentColor = Blanco,
                    shape = RoundedCornerShape(16.dp),
                    icon = { Icono(Res.drawable.ic_herramienta, null, Blanco, Modifier.size(18.dp)) },
                    text = { Text("Ofrecer servicio", style = MaterialTheme.typography.labelLarge) }
                )
            }
        }
    ) { relleno ->
        Box(Modifier.fillMaxSize().padding(relleno)) {
            when (pestana) {
                PestanaCliente.EXPLORAR -> ExplorarContenido(
                    estado = explorar,
                    usuario = sesion.usuario,
                    favoritos = sesion.favoritos,
                    onAbrir = onAbrirNegocio,
                    onFavorito = { avisar(sesion.alternarFavorito(it)) },
                    onMensaje = { avisar(contacto.whatsapp(it)) },
                    onLlamar = { avisar(contacto.llamar(it)) },
                    onFiltros = { mostrarFiltros = true }
                )
                PestanaCliente.MAPA -> MapaColoniasContenido(
                    negocios = remember { container.obtenerNegocios() },
                    coloniaPreferida = explorar.coloniaPreferida,
                    onAbrir = onAbrirNegocio,
                    onComoLlegar = { avisar(contacto.comoLlegar(it)) },
                    onElegirColonia = onElegirColonia
                )
                PestanaCliente.FAVORITOS -> FavoritosContenido(
                    container, sesion,
                    onAbrir = onAbrirNegocio,
                    onIniciarSesion = onIniciarSesion,
                    onExplorar = { onPestana(PestanaCliente.EXPLORAR) },
                    onFavorito = { avisar(sesion.alternarFavorito(it)) },
                    onMensaje = { avisar(contacto.whatsapp(it)) },
                    onLlamar = { avisar(contacto.llamar(it)) },
                    abierto = explorar::abierto
                )
                PestanaCliente.AJUSTES -> ajustes()
            }
        }
    }

    if (mostrarFiltros) {
        FiltrosSheet(
            iniciales = explorar.filtros,
            contar = explorar::contar,
            onAplicar = { explorar.filtros = it; mostrarFiltros = false },
            onCerrar = { mostrarFiltros = false }
        )
    }
}

@Composable
private fun ExplorarContenido(
    estado: ExplorarEstado,
    usuario: Usuario?,
    favoritos: Set<String>,
    onAbrir: (String) -> Unit,
    onFavorito: (String) -> Unit,
    onMensaje: (Negocio) -> Unit,
    onLlamar: (Negocio) -> Unit,
    onFiltros: () -> Unit
) {
    val resultados = estado.resultados
    LazyColumn(
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(34.dp).clip(RoundedCornerShape(10.dp)).background(Terracota.t500), contentAlignment = Alignment.Center) {
                    Icono(Res.drawable.ic_herramienta, null, Blanco, Modifier.size(18.dp))
                }
                Spacer(Modifier.width(10.dp))
                Text("Explorar", style = MaterialTheme.typography.titleLarge, color = Neutro.t500, modifier = Modifier.weight(1f))
                Box(Modifier.size(34.dp).clip(CircleShape).background(Terracota.t100), contentAlignment = Alignment.Center) {
                    if (usuario != null) {
                        Text(usuario.nombre.take(1).uppercase(), style = MaterialTheme.typography.titleSmall, color = Terracota.t700)
                    } else {
                        Icono(Res.drawable.ic_usuario, "Invitado", Terracota.t600, Modifier.size(18.dp))
                    }
                }
            }
        }
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icono(Res.drawable.ic_ubicacion, null, Terracota.t500, Modifier.size(14.dp))
                Spacer(Modifier.width(4.dp))
                Text("Jiquilpan de Juárez, Mich.", style = MaterialTheme.typography.labelMedium, color = Neutro.t400)
            }
            Spacer(Modifier.height(6.dp))
            Text(
                usuario?.let { "Hola, ${it.nombre.substringBefore(' ')}. ¿Qué necesitas hoy?" } ?: "Hola, ¿qué necesitas hoy?",
                style = MaterialTheme.typography.displaySmall, color = Neutro.t500
            )
        }
        item { BarraBusqueda(estado.texto, { estado.texto = it }, estado.filtrosActivos, onFiltros) }
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item { ChipCategoria("Todos", null, estado.filtros.categorias.isEmpty()) { estado.elegirCategoria(null) } }
                items(Categoria.entries) { cat ->
                    ChipCategoria(cat.nombreVisible, cat.icono(), estado.categoriaRapida == cat) {
                        estado.elegirCategoria(if (estado.categoriaRapida == cat) null else cat)
                    }
                }
            }
        }
        item {
            Row(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(Oliva.t50).padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icono(Res.drawable.ic_verificado, null, Oliva.t500, Modifier.size(20.dp))
                Spacer(Modifier.width(10.dp))
                Column {
                    Text("Directorio de Jiquilpan y la Ciénega", style = MaterialTheme.typography.titleSmall, color = Oliva.t800)
                    Text("Trato directo, sin comisiones.", style = MaterialTheme.typography.bodySmall, color = Oliva.t700)
                }
            }
        }
        item { EncabezadoLista(estado, resultados.size) }
        if (resultados.isEmpty()) {
            item {
                EstadoVacio(
                    Res.drawable.ic_buscar,
                    "Sin resultados",
                    "No encontramos oficios con esa búsqueda. Prueba con otra palabra o quita algunos filtros.",
                    accion = "Limpiar búsqueda y filtros",
                    onAccion = estado::limpiar
                )
            }
        } else {
            items(resultados, key = { it.negocio.id }) { r ->
                TarjetaNegocio(
                    resumen = r,
                    abierto = estado.abierto(r),
                    esFavorito = r.negocio.id in favoritos,
                    onAbrir = { onAbrir(r.negocio.id) },
                    onFavorito = { onFavorito(r.negocio.id) },
                    onMensaje = { onMensaje(r.negocio) },
                    onLlamar = { onLlamar(r.negocio) }
                )
            }
        }
    }
}

@Composable
private fun EncabezadoLista(estado: ExplorarEstado, total: Int) {
    var menu by remember { mutableStateOf(false) }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text(estado.tituloLista, style = MaterialTheme.typography.headlineSmall, color = Neutro.t500)
            Text(
                "$total ${if (total == 1) "profesional" else "profesionales"}",
                style = MaterialTheme.typography.bodySmall, color = Neutro.t400
            )
        }
        Box {
            Row(
                Modifier.clip(RoundedCornerShape(10.dp)).background(Blanco).clickable { menu = true }
                    .padding(horizontal = 10.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(estado.orden.nombreVisible, style = MaterialTheme.typography.labelMedium, color = Neutro.t500)
                Spacer(Modifier.width(4.dp))
                Icono(Res.drawable.ic_filtros, "Cambiar orden", Neutro.t400, Modifier.size(14.dp))
            }
            DropdownMenu(expanded = menu, onDismissRequest = { menu = false }, containerColor = Blanco) {
                Orden.entries.forEach { o ->
                    DropdownMenuItem(
                        text = { Text(o.nombreVisible, style = MaterialTheme.typography.bodyMedium) },
                        onClick = { estado.orden = o; menu = false },
                        trailingIcon = if (o == estado.orden) {
                            { Icono(Res.drawable.ic_check, null, Terracota.t500, Modifier.size(16.dp)) }
                        } else null
                    )
                }
            }
        }
    }
}

@Composable
private fun FavoritosContenido(
    container: AppContainer,
    sesion: Sesion,
    onAbrir: (String) -> Unit,
    onIniciarSesion: () -> Unit,
    onExplorar: () -> Unit,
    onFavorito: (String) -> Unit,
    onMensaje: (Negocio) -> Unit,
    onLlamar: (Negocio) -> Unit,
    abierto: (NegocioResumen) -> Boolean
) {
    val usuario = sesion.usuario
    if (usuario == null) {
        EstadoVacio(
            Res.drawable.ic_corazon, "Guarda tus oficios de confianza",
            "Inicia sesión para guardar negocios y encontrarlos rápido la próxima vez.",
            accion = "Iniciar sesión", onAccion = onIniciarSesion
        )
        return
    }
    val lista = remember(sesion.favoritos) {
        container.obtenerNegocios().filter { it.negocio.id in sesion.favoritos }
    }
    if (lista.isEmpty()) {
        EstadoVacio(
            Res.drawable.ic_corazon, "Aún no tienes favoritos",
            "Toca el corazón de cualquier negocio para guardarlo aquí.",
            accion = "Explorar oficios", onAccion = onExplorar
        )
        return
    }
    LazyColumn(
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Text("Mis favoritos", style = MaterialTheme.typography.displaySmall, color = Neutro.t500)
            Text("${lista.size} guardados", style = MaterialTheme.typography.bodySmall, color = Neutro.t400)
        }
        items(lista, key = { it.negocio.id }) { r ->
            TarjetaNegocio(
                resumen = r, abierto = abierto(r), esFavorito = true,
                onAbrir = { onAbrir(r.negocio.id) },
                onFavorito = { onFavorito(r.negocio.id) },
                onMensaje = { onMensaje(r.negocio) },
                onLlamar = { onLlamar(r.negocio) }
            )
        }
    }
}
