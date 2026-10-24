package com.example.proyectofinal.domain.model

import java.util.Calendar

actual fun obtenerMomentoActual(): MomentoActual {
    val c = Calendar.getInstance()
    val dia = when (c.get(Calendar.DAY_OF_WEEK)) {
        Calendar.MONDAY -> DiaSemana.LUNES
        Calendar.TUESDAY -> DiaSemana.MARTES
        Calendar.WEDNESDAY -> DiaSemana.MIERCOLES
        Calendar.THURSDAY -> DiaSemana.JUEVES
        Calendar.FRIDAY -> DiaSemana.VIERNES
        Calendar.SATURDAY -> DiaSemana.SABADO
        else -> DiaSemana.DOMINGO
    }
    return MomentoActual(dia, c.get(Calendar.HOUR_OF_DAY) * 60 + c.get(Calendar.MINUTE))
}
