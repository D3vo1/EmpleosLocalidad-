package com.example.proyectofinal.domain.model

/**
 * Resultado de calcular las reseñas de un negocio.
 * [distribucion] cuenta cuántas reseñas hay de cada estrella: clave 1..5.
 */
data class Calificacion(
    val promedio: Double,
    val total: Int,
    val distribucion: Map<Int, Int>
) {
    companion object {
        val VACIA = Calificacion(0.0, 0, (1..5).associateWith { 0 })
    }
}
