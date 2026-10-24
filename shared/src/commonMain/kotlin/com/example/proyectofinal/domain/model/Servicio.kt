package com.example.proyectofinal.domain.model

/**
 * Servicio que ofrece un negocio.
 * Si [esPrecioDesde] es true se muestra "Desde $300"; si es false, "$300".
 * [activo] permite al negocio ocultar un servicio sin borrarlo.
 */
data class Servicio(
    val id: String,
    val idNegocio: String,
    val nombre: String,
    val descripcion: String,
    val precioAprox: Double,
    val esPrecioDesde: Boolean = true,
    val activo: Boolean = true
)
