package com.example.proyectofinal.domain.model

/** Día de la semana y minutos desde medianoche (ej. 14:30 = 870). */
data class MomentoActual(val dia: DiaSemana, val minutos: Int)

/**
 * Hora local del teléfono. Kotlin común no tiene reloj con zona horaria,
 * así que cada plataforma lo implementa con su propio SDK:
 * androidMain → java.util.Calendar, iosMain → NSCalendar.
 */
expect fun obtenerMomentoActual(): MomentoActual
