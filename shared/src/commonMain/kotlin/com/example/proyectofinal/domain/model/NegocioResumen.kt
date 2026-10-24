package com.example.proyectofinal.domain.model

/**
 * Lo que necesita una tarjeta de la lista de Explorar:
 * el negocio + su calificación + el precio más bajo de sus servicios activos.
 * [precioDesde] es null si el negocio no tiene servicios activos.
 */
data class NegocioResumen(
    val negocio: Negocio,
    val calificacion: Calificacion,
    val precioDesde: Double?
)
