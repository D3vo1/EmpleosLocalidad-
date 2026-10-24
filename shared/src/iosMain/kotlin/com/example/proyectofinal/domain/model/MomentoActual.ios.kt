package com.example.proyectofinal.domain.model

import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarUnitHour
import platform.Foundation.NSCalendarUnitMinute
import platform.Foundation.NSCalendarUnitWeekday
import platform.Foundation.NSDate

actual fun obtenerMomentoActual(): MomentoActual {
    val partes = NSCalendar.currentCalendar.components(
        NSCalendarUnitWeekday or NSCalendarUnitHour or NSCalendarUnitMinute,
        fromDate = NSDate()
    )
    // En iOS weekday va de 1 (domingo) a 7 (sábado)
    val dia = when (partes.weekday.toInt()) {
        2 -> DiaSemana.LUNES
        3 -> DiaSemana.MARTES
        4 -> DiaSemana.MIERCOLES
        5 -> DiaSemana.JUEVES
        6 -> DiaSemana.VIERNES
        7 -> DiaSemana.SABADO
        else -> DiaSemana.DOMINGO
    }
    return MomentoActual(dia, partes.hour.toInt() * 60 + partes.minute.toInt())
}
