package com.example.proyectofinal.domain.usecase

import com.example.proyectofinal.domain.model.NegocioResumen

/**
 * Busca por texto en nombre, oficio, descripción y nombre de categoría.
 * No distingue mayúsculas ni acentos: "plomeria" encuentra "Plomería".
 * Todas las palabras escritas deben aparecer (ej. "clases ingles").
 */
class BuscarNegociosUseCase {

    operator fun invoke(negocios: List<NegocioResumen>, texto: String): List<NegocioResumen> {
        val palabras = normalizar(texto).split(" ").filter { it.isNotBlank() }
        if (palabras.isEmpty()) return negocios

        return negocios.filter { resumen ->
            val n = resumen.negocio
            val contenido = normalizar(
                "${n.nombre} ${n.oficio} ${n.descripcion} ${n.categoria.nombreVisible}"
            )
            palabras.all { contenido.contains(it) }
        }
    }

    companion object {
        // Kotlin común no tiene java.text.Normalizer, así que se quitan los acentos a mano
        private val SIN_ACENTO = mapOf(
            'á' to 'a', 'é' to 'e', 'í' to 'i', 'ó' to 'o', 'ú' to 'u', 'ü' to 'u',
            'à' to 'a', 'è' to 'e', 'ì' to 'i', 'ò' to 'o', 'ù' to 'u'
        )

        /** Pasa a minúsculas, quita acentos (la ñ se respeta) y limpia espacios. */
        fun normalizar(texto: String): String =
            texto.lowercase()
                .map { SIN_ACENTO[it] ?: it }
                .joinToString("")
                .trim()
    }
}
