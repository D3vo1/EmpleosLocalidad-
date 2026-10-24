package com.example.proyectofinal.domain.usecase

import com.example.proyectofinal.domain.model.DiaSemana
import com.example.proyectofinal.domain.model.Horario
import com.example.proyectofinal.domain.model.MomentoActual
import com.example.proyectofinal.domain.model.obtenerMomentoActual

/**
 * Dice si un negocio está abierto en un momento dado.
 * Soporta horarios que cruzan la medianoche (ej. sábado 20:00 a 02:00).
 * [reloj] se puede reemplazar en las pruebas para fijar la hora.
 */
class EstaAbiertoAhoraUseCase(
    private val reloj: () -> MomentoActual = ::obtenerMomentoActual
) {

    operator fun invoke(horarios: List<Horario>): Boolean {
        val ahora = reloj()
        return estaAbierto(horarios, ahora.dia, ahora.minutos)
    }

    fun estaAbierto(horarios: List<Horario>, dia: DiaSemana, minutos: Int): Boolean {
        // 1) ¿Abierto por el horario de hoy?
        val hoy = horarios.filter { it.dia == dia }.any { h ->
            if (h.cierre > h.apertura) minutos >= h.apertura && minutos < h.cierre
            else minutos >= h.apertura // cruza medianoche: abierto desde apertura hasta 23:59
        }
        if (hoy) return true

        // 2) ¿Sigue abierto por el horario de ayer que cruzó la medianoche?
        val ayer = DiaSemana.entries[(dia.ordinal + 6) % 7]
        return horarios.filter { it.dia == ayer }.any { h ->
            h.cierre < h.apertura && minutos < h.cierre
        }
    }
}
