package com.example.proyectofinal.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.proyectofinal.data.AppContainer
import com.example.proyectofinal.domain.model.Resultado
import com.example.proyectofinal.domain.model.Rol
import com.example.proyectofinal.ui.components.EstadoVacio
import com.example.proyectofinal.ui.components.Icono
import com.example.proyectofinal.ui.components.PestanaCliente
import com.example.proyectofinal.ui.components.PestanaNegocio
import com.example.proyectofinal.ui.screens.AjustesScreen
import com.example.proyectofinal.ui.screens.DetalleNegocioScreen
import com.example.proyectofinal.ui.screens.ExplorarEstado
import com.example.proyectofinal.ui.screens.FormularioServicioEstado
import com.example.proyectofinal.ui.screens.FormularioServicioScreen
import com.example.proyectofinal.ui.screens.LoginEstado
import com.example.proyectofinal.ui.screens.LoginScreen
import com.example.proyectofinal.ui.screens.PermisoUbicacionScreen
import com.example.proyectofinal.ui.screens.PrincipalNegocioScreen
import com.example.proyectofinal.ui.screens.PrincipalScreen
import com.example.proyectofinal.ui.screens.PublicarVacanteEstado
import com.example.proyectofinal.ui.screens.PublicarVacanteScreen
import com.example.proyectofinal.ui.screens.RegistroClienteEstado
import com.example.proyectofinal.ui.screens.RegistroClienteScreen
import com.example.proyectofinal.ui.screens.RegistroNegocioEstado
import com.example.proyectofinal.ui.screens.RegistroNegocioScreen
import com.example.proyectofinal.ui.screens.SelectorRolScreen
import com.example.proyectofinal.ui.screens.SinConexionScreen
import com.example.proyectofinal.ui.screens.SplashScreen
import com.example.proyectofinal.ui.screens.TEXTO_TERMINOS
import com.example.proyectofinal.ui.theme.Blanco
import com.example.proyectofinal.ui.theme.Crema
import com.example.proyectofinal.ui.theme.Neutro
import com.example.proyectofinal.ui.theme.Terracota
import proyectofinal.shared.generated.resources.*

/** Decide qué pantalla dibujar según el tope de la pila del [Navegador]. */
@Composable
fun AppNavegacion(navegador: Navegador, container: AppContainer) {
    val sesion = remember { Sesion(container) }
    val explorar = remember { ExplorarEstado(container) }
    var pestana by remember { mutableStateOf(PestanaCliente.EXPLORAR) }
    var pestanaNegocio by remember { mutableStateOf(PestanaNegocio.PANEL) }
    var mensajeNegocio by remember { mutableStateOf<String?>(null) }
    var versionDetalle by remember { mutableStateOf(0) }
    var verTerminos by remember { mutableStateOf(false) }

    fun entrarComo(rolCliente: Boolean) {
        if (rolCliente) {
            pestana = PestanaCliente.EXPLORAR
            navegador.reiniciarEn(Pantalla.Principal)
        } else {
            pestanaNegocio = PestanaNegocio.PANEL
            navegador.reiniciarEn(Pantalla.PrincipalNegocio)
        }
    }

    fun cerrarSesion() {
        sesion.entrar(null)
        explorar.coloniaPreferida = null
        navegador.reiniciarEn(Pantalla.SelectorRol)
    }

    /** Ajustes es igual para clientes y negocios; solo cambia "Mi colonia" y el banner. */
    val ajustes: @Composable () -> Unit = {
        val usuario = sesion.usuario
        AjustesScreen(
            usuario = usuario,
            colonia = explorar.coloniaPreferida,
            onCambiarColonia = if (usuario?.rol == Rol.NEGOCIO) null else {
                { navegador.ir(Pantalla.PermisoUbicacion(primeraVez = false)) }
            },
            onVerSinConexion = { navegador.ir(Pantalla.SinConexion) },
            onRegistrarNegocio = { navegador.ir(Pantalla.RegistroNegocio) },
            onIniciarSesion = { navegador.ir(Pantalla.Login(Rol.CLIENTE)) },
            onCerrarSesion = { cerrarSesion() }
        )
    }

    when (val p = navegador.actual) {
        Pantalla.Splash -> SplashScreen(onTerminar = { navegador.reiniciarEn(Pantalla.SelectorRol) })

        Pantalla.SelectorRol -> SelectorRolScreen(
            onElegirRol = { navegador.ir(Pantalla.Login(it)) },
            onInvitado = {
                sesion.entrar(null)
                entrarComo(rolCliente = true)
            }
        )

        is Pantalla.Login -> {
            val estado = remember(p.rol) { LoginEstado(container.iniciarSesion, p.rol) }
            LoginScreen(
                estado = estado,
                onVolver = { navegador.volver() },
                onEntrar = { usuario ->
                    sesion.entrar(usuario)
                    entrarComo(rolCliente = usuario.rol == Rol.CLIENTE)
                },
                onRegistro = {
                    navegador.ir(if (p.rol == Rol.CLIENTE) Pantalla.RegistroCliente else Pantalla.RegistroNegocio)
                }
            )
        }

        Pantalla.RegistroCliente -> {
            val estado = remember { RegistroClienteEstado(container.registrarCliente, container.validar) }
            RegistroClienteScreen(
                estado = estado,
                onVolver = { navegador.volver() },
                onCreada = { usuario ->
                    sesion.entrar(usuario)
                    entrarComo(rolCliente = true)
                    navegador.ir(Pantalla.PermisoUbicacion(primeraVez = true))
                },
                onIniciarSesion = { navegador.volver() },
                onTerminos = { verTerminos = true }
            )
        }

        Pantalla.RegistroNegocio -> {
            val estado = remember { RegistroNegocioEstado(container.registrarNegocio, container.validar) }
            RegistroNegocioScreen(
                estado = estado,
                colonias = remember { container.colonias },
                onSalir = { navegador.volver() },
                onRegistrado = { (dueno, negocio) ->
                    sesion.entrar(dueno)
                    entrarComo(rolCliente = false)
                    mensajeNegocio = "¡Bienvenido! ${negocio.nombre} ya aparece en Explorar"
                }
            )
        }

        is Pantalla.PermisoUbicacion -> PermisoUbicacionScreen(
            colonias = remember { container.colonias },
            actual = explorar.coloniaPreferida,
            onVolver = if (p.primeraVez) null else { { navegador.volver() } },
            onElegir = { colonia ->
                // "Ahora no" desde Ajustes no borra la colonia que ya estaba elegida
                if (colonia != null || p.primeraVez) explorar.coloniaPreferida = colonia
                navegador.volver()
            }
        )

        Pantalla.Principal -> PrincipalScreen(
            container = container,
            sesion = sesion,
            explorar = explorar,
            pestana = pestana,
            onPestana = { pestana = it },
            onAbrirNegocio = { navegador.ir(Pantalla.Detalle(it)) },
            onIniciarSesion = { navegador.ir(Pantalla.Login(Rol.CLIENTE)) },
            onOfrecerServicio = { navegador.ir(Pantalla.RegistroNegocio) },
            onCerrarSesion = { cerrarSesion() },
            onElegirColonia = { navegador.ir(Pantalla.PermisoUbicacion(primeraVez = false)) },
            ajustes = ajustes
        )

        Pantalla.PrincipalNegocio -> {
            val usuario = sesion.usuario
            if (usuario == null) {
                LaunchedEffect(Unit) { navegador.reiniciarEn(Pantalla.SelectorRol) }
            } else {
                PrincipalNegocioScreen(
                    container = container,
                    usuario = usuario,
                    pestana = pestanaNegocio,
                    onPestana = { pestanaNegocio = it },
                    onVerPerfil = { navegador.ir(Pantalla.Detalle(it)) },
                    onNuevoServicio = { navegador.ir(Pantalla.FormularioServicio(null)) },
                    onEditarServicio = { navegador.ir(Pantalla.FormularioServicio(it)) },
                    onNuevaVacante = { navegador.ir(Pantalla.PublicarVacante) },
                    mensajeInicial = mensajeNegocio,
                    onMensajeMostrado = { mensajeNegocio = null },
                    ajustes = ajustes
                )
            }
        }

        is Pantalla.FormularioServicio -> {
            val usuario = sesion.usuario
            val negocio = remember(usuario) { container.negocioDelDueno(usuario) }
            if (usuario == null || negocio == null) {
                LaunchedEffect(Unit) { navegador.volver() }
            } else {
                val estado = remember(p.idServicio) {
                    val existente = p.idServicio?.let { id -> container.servicios.listar(negocio.id).firstOrNull { it.id == id } }
                    FormularioServicioEstado(container.servicios, negocio.id, usuario.id, existente)
                }
                FormularioServicioScreen(
                    estado = estado,
                    onVolver = { navegador.volver() },
                    onGuardado = { msg ->
                        mensajeNegocio = msg
                        pestanaNegocio = PestanaNegocio.SERVICIOS
                        navegador.volver()
                    }
                )
            }
        }

        Pantalla.PublicarVacante -> {
            val usuario = sesion.usuario
            val negocio = remember(usuario) { container.negocioDelDueno(usuario) }
            if (usuario == null || negocio == null) {
                LaunchedEffect(Unit) { navegador.volver() }
            } else {
                val estado = remember { PublicarVacanteEstado(container.vacantes, negocio, usuario.id) }
                PublicarVacanteScreen(
                    estado = estado,
                    onVolver = { navegador.volver() },
                    onPublicada = {
                        mensajeNegocio = "Vacante publicada"
                        pestanaNegocio = PestanaNegocio.VACANTES
                        navegador.volver()
                    }
                )
            }
        }

        is Pantalla.Detalle -> {
            val detalle = remember(p.idNegocio, versionDetalle) { container.obtenerDetalle(p.idNegocio) }
            DetalleNegocioScreen(
                detalle = detalle,
                esFavorito = p.idNegocio in sesion.favoritos,
                onVolver = { navegador.volver() },
                onFavorito = { sesion.alternarFavorito(p.idNegocio) },
                onResena = { estrellas, comentario ->
                    when (val r = container.resenas(p.idNegocio, sesion.usuario, estrellas, comentario)) {
                        is Resultado.Exito -> { versionDetalle++; null }
                        is Resultado.Error -> r.errores.values.firstOrNull() ?: r.mensaje
                    }
                }
            )
        }

        Pantalla.SinConexion -> SinConexionScreen(
            onVolver = { navegador.volver() },
            onReintentar = { navegador.volver() },
            onVerGuardados = {
                if (sesion.usuario?.rol != Rol.NEGOCIO) pestana = PestanaCliente.FAVORITOS
                navegador.volver()
            }
        )

        is Pantalla.Proximamente -> Box(Modifier.fillMaxSize().background(Crema).safeDrawingPadding()) {
            Column {
                IconButton(onClick = {
                    if (!navegador.volver()) navegador.reiniciarEn(Pantalla.SelectorRol)
                }, modifier = Modifier.padding(8.dp)) {
                    Icono(Res.drawable.ic_atras, "Volver", Neutro.t500, Modifier.size(22.dp))
                }
            }
            EstadoVacio(
                Res.drawable.ic_herramienta, p.titulo, p.descripcion,
                accion = "Volver",
                onAccion = { if (!navegador.volver()) navegador.reiniciarEn(Pantalla.SelectorRol) },
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }

    if (verTerminos) {
        AlertDialog(
            onDismissRequest = { verTerminos = false },
            confirmButton = { TextButton(onClick = { verTerminos = false }) { Text("Entendido", color = Terracota.t600) } },
            title = { Text("Términos y convivencia vecinal", style = MaterialTheme.typography.titleLarge) },
            text = { Text(TEXTO_TERMINOS, style = MaterialTheme.typography.bodyMedium) },
            containerColor = Blanco
        )
    }
}
