package com.example.proyectofinal.ui.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.proyectofinal.data.AppContainer
import com.example.proyectofinal.domain.model.Resultado
import com.example.proyectofinal.domain.model.Rol
import com.example.proyectofinal.domain.model.Usuario

/** Todas las pantallas de esta tanda. Se navega agregando o quitando de una pila. */
sealed interface Pantalla {
    data object Splash : Pantalla
    data object SelectorRol : Pantalla
    data class Login(val rol: Rol) : Pantalla
    data object Principal : Pantalla
    data class Detalle(val idNegocio: String) : Pantalla
    data class Proximamente(val titulo: String, val descripcion: String) : Pantalla

    // Registro y ubicación
    data object RegistroCliente : Pantalla
    data object RegistroNegocio : Pantalla
    /** [primeraVez] = justo después de crear la cuenta (sin botón volver). */
    data class PermisoUbicacion(val primeraVez: Boolean) : Pantalla

    // Lado negocio
    data object PrincipalNegocio : Pantalla
    /** [idServicio] null = servicio nuevo. */
    data class FormularioServicio(val idServicio: String?) : Pantalla
    data object PublicarVacante : Pantalla

    // Estados del sistema
    data object SinConexion : Pantalla
}

/**
 * Navegación sin librerías: una pila observable de pantallas.
 * El botón atrás de Android llama a [volver] (ver MainActivity).
 */
class Navegador(inicio: Pantalla = Pantalla.Splash) {
    private val pila = mutableStateListOf(inicio)

    val actual: Pantalla get() = pila.last()
    val puedeVolver: Boolean get() = pila.size > 1 && actual != Pantalla.Splash

    fun ir(pantalla: Pantalla) { pila.add(pantalla) }

    /** Borra el historial: después de Splash o al iniciar sesión no se regresa con "atrás". */
    fun reiniciarEn(pantalla: Pantalla) {
        pila.clear()
        pila.add(pantalla)
    }

    fun volver(): Boolean {
        if (pila.size <= 1) return false
        pila.removeAt(pila.lastIndex)
        return true
    }
}

/** Quién está usando la app (null = invitado) y sus favoritos, observables por la UI. */
class Sesion(private val container: AppContainer) {
    var usuario by mutableStateOf<Usuario?>(null)
        private set
    var favoritos by mutableStateOf<Set<String>>(emptySet())
        private set

    fun entrar(usuario: Usuario?) {
        this.usuario = usuario
        favoritos = container.favoritos.listar(usuario).map { it.id }.toSet()
    }

    /** Devuelve el mensaje para mostrar en la barra inferior (snackbar). */
    fun alternarFavorito(idNegocio: String): String =
        when (val r = container.favoritos.alternar(usuario, idNegocio)) {
            is Resultado.Exito -> {
                favoritos = if (r.dato) favoritos + idNegocio else favoritos - idNegocio
                if (r.dato) "Guardado en favoritos" else "Quitado de favoritos"
            }
            is Resultado.Error -> r.mensaje
        }
}
