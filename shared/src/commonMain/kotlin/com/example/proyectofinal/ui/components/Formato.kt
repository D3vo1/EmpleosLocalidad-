package com.example.proyectofinal.ui.components

import com.example.proyectofinal.domain.model.DiaSemana
import com.example.proyectofinal.domain.model.Horario
import com.example.proyectofinal.domain.model.obtenerMomentoActual

/** "$1,800" — sin decimales si el precio es entero. Kotlin común no tiene NumberFormat. */
fun formatearPrecio(valor: Double): String {
    val entero = valor.toLong()
    val miles = entero.toString().reversed().chunked(3).joinToString(",").reversed()
    val centavos = ((valor - entero) * 100).toLong()
    return if (centavos == 0L) "$$miles" else "$$miles.${centavos.toString().padStart(2, '0')}"
}

/** 570 → "9:30 AM", 1080 → "6:00 PM", 1440 → "12:00 AM". */
fun formatearHora(minutos: Int): String {
    val m = minutos % 1440
    val h24 = m / 60
    val sufijo = if (h24 < 12) "AM" else "PM"
    val h12 = when { h24 == 0 -> 12; h24 > 12 -> h24 - 12; else -> h24 }
    return "$h12:${(m % 60).toString().padStart(2, '0')} $sufijo"
}

/** Texto bajo "Abierto ahora / Cerrado": a qué hora cierra hoy o cuándo abre. */
fun textoHorarioHoy(horarios: List<Horario>, abierto: Boolean): String {
    val ahora = obtenerMomentoActual()
    val hoy = horarios.firstOrNull { it.dia == ahora.dia }
    return when {
        horarios.size == 7 && horarios.all { it.apertura == 0 && it.cierre == 1440 } -> "Servicio 24 horas"
        abierto && hoy != null -> "Cierra ${formatearHora(hoy.cierre)}"
        hoy != null && ahora.minutos < hoy.apertura -> "Abre hoy ${formatearHora(hoy.apertura)}"
        else -> {
            val siguiente = (1..7).map { DiaSemana.entries[(ahora.dia.ordinal + it) % 7] }
                .firstNotNullOfOrNull { d -> horarios.firstOrNull { it.dia == d } }
            siguiente?.let { "Abre ${it.dia.nombreVisible.lowercase()} ${formatearHora(it.apertura)}" } ?: "Sin horario"
        }
    }
}

/** Codifica texto para usarlo en una URL (UTF-8, espacios como +). */
fun codificarUrl(texto: String): String = buildString {
    texto.encodeToByteArray().forEach { b ->
        val c = b.toInt() and 0xFF
        when {
            c.toChar().isLetterOrDigit() && c < 128 -> append(c.toChar())
            c.toChar() in "-_.~" -> append(c.toChar())
            c == ' '.code -> append('+')
            else -> append('%').append(c.toString(16).uppercase().padStart(2, '0'))
        }
    }
}

/** "3535550101" → "353 555 0101". Si no tiene 10 dígitos, lo deja como está. */
fun formatearTelefono(telefono: String): String =
    if (telefono.length == 10 && telefono.all { it.isDigit() })
        "${telefono.substring(0, 3)} ${telefono.substring(3, 6)} ${telefono.substring(6)}"
    else telefono
