package com.example.proyectofinal.domain.usecase

/**
 * Reglas de validación de formularios. Cada función devuelve el mensaje de error
 * o null si el valor es válido, para mostrarlo debajo del TextField.
 * Los demás casos de uso de la Persona B las reutilizan.
 */
class ValidarFormularioUseCase {

    fun nombre(valor: String, campo: String = "el nombre"): String? = when {
        valor.isBlank() -> "Escribe $campo"
        valor.trim().length < 2 -> "Debe tener al menos 2 caracteres"
        valor.trim().length > 60 -> "Máximo 60 caracteres"
        else -> null
    }

    fun correo(valor: String): String? = when {
        valor.isBlank() -> "Escribe tu correo"
        !REGEX_CORREO.matches(normalizarCorreo(valor)) -> "Correo no válido"
        else -> null
    }

    /** 10 dígitos (formato México). Acepta espacios y guiones: "353 123 4567". */
    fun telefono(valor: String): String? = when {
        valor.isBlank() -> "Escribe un teléfono"
        valor.any { !it.isDigit() && it != ' ' && it != '-' } -> "Solo números"
        soloDigitos(valor).length != 10 -> "Debe tener 10 dígitos"
        else -> null
    }

    /** Mínimo 8 caracteres, con al menos una letra y un número. */
    fun contrasena(valor: String): String? = when {
        valor.length < 8 -> "Mínimo 8 caracteres"
        valor.none { it.isLetter() } || valor.none { it.isDigit() } -> "Usa letras y números"
        else -> null
    }

    fun confirmacion(contrasena: String, confirmacion: String): String? =
        if (contrasena != confirmacion) "Las contraseñas no coinciden" else null

    fun precio(valor: Double?): String? = when {
        valor == null -> "Escribe un precio"
        valor <= 0.0 -> "El precio debe ser mayor a 0"
        valor > 1_000_000.0 -> "Precio demasiado alto"
        else -> null
    }

    fun textoRequerido(valor: String, campo: String, maximo: Int = 500): String? = when {
        valor.isBlank() -> "Escribe $campo"
        valor.trim().length > maximo -> "Máximo $maximo caracteres"
        else -> null
    }

    companion object {
        private val REGEX_CORREO = Regex("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$")

        fun normalizarCorreo(correo: String): String = correo.trim().lowercase()
        fun soloDigitos(texto: String): String = texto.filter { it.isDigit() }
    }
}
