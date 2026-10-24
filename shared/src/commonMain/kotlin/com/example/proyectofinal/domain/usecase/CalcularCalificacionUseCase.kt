package com.example.proyectofinal.domain.usecase

import com.example.proyectofinal.domain.model.Calificacion
import com.example.proyectofinal.domain.model.Resena
import kotlin.math.roundToInt

/**
 * Calcula promedio (redondeado a 1 decimal), total y distribución por estrellas.
 * Ignora reseñas con estrellas fuera de 1..5 por seguridad.
 */
class CalcularCalificacionUseCase {

    operator fun invoke(resenas: List<Resena>): Calificacion {
        val validas = resenas.filter { it.estrellas in 1..5 }
        if (validas.isEmpty()) return Calificacion.VACIA

        val promedio = validas.map { it.estrellas }.average()
        val redondeado = (promedio * 10).roundToInt() / 10.0
        val distribucion = (1..5).associateWith { estrella ->
            validas.count { it.estrellas == estrella }
        }
        return Calificacion(redondeado, validas.size, distribucion)
    }
}
