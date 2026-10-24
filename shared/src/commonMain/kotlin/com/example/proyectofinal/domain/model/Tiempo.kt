package com.example.proyectofinal.domain.model

import kotlin.random.Random

/**
 * Milisegundos desde 1970 (para fechas de reseñas y vacantes).
 * Cada plataforma lo implementa: androidMain → System.currentTimeMillis(),
 * iosMain → NSDate().timeIntervalSince1970.
 */
expect fun ahoraMillis(): Long

/** Id aleatorio de 16 caracteres hexadecimales; Kotlin común no tiene java.util.UUID. */
fun generarId(): String =
    Random.nextLong().toULong().toString(16).padStart(16, '0')
