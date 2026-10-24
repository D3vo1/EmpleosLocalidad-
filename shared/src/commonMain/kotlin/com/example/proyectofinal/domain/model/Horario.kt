package com.example.proyectofinal.domain.model

/** Días de la semana en el orden de México (lunes primero). */
enum class DiaSemana(val nombreVisible: String) {
    LUNES("Lunes"),
    MARTES("Martes"),
    MIERCOLES("Miércoles"),
    JUEVES("Jueves"),
    VIERNES("Viernes"),
    SABADO("Sábado"),
    DOMINGO("Domingo")
}

/**
 * Horario de un día. Las horas se guardan en minutos desde medianoche
 * para poder compararlas fácilmente (9:30 = 570).
 * Si [cierre] es menor que [apertura], el horario cruza la medianoche (ej. 20:00 a 2:00).
 */
data class Horario(
    val dia: DiaSemana,
    val apertura: Int,
    val cierre: Int
) {
    init {
        require(apertura in 0..1439) { "Apertura fuera de rango: $apertura" }
        require(cierre in 0..1440) { "Cierre fuera de rango: $cierre" }
    }

    companion object {
        /** Ayuda para escribir horarios legibles: Horario.de(DiaSemana.LUNES, 9, 0, 18, 30) */
        fun de(dia: DiaSemana, hAbre: Int, mAbre: Int, hCierra: Int, mCierra: Int) =
            Horario(dia, hAbre * 60 + mAbre, hCierra * 60 + mCierra)
    }
}
