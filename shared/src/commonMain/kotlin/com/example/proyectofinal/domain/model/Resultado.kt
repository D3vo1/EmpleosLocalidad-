package com.example.proyectofinal.domain.model

/**
 * Respuesta común de los casos de uso que pueden fallar.
 * La UI hace `when (r) { is Exito -> ...; is Error -> mostrar(r.mensaje) }`.
 * [Error.errores] trae los errores por campo para mostrarlos bajo cada TextField.
 */
sealed class Resultado<out T> {
    data class Exito<T>(val dato: T) : Resultado<T>()
    data class Error(
        val mensaje: String,
        val errores: Map<Campo, String> = emptyMap()
    ) : Resultado<Nothing>()
}
