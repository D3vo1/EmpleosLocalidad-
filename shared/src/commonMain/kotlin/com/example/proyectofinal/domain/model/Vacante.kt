package com.example.proyectofinal.domain.model

/**
 * Oferta de empleo que publica un negocio.
 * [sueldoAprox] es opcional (null = "A tratar").
 * [activa] permite cerrar la vacante sin borrarla.
 * [fecha] en milisegundos desde 1970.
 */
data class Vacante(
    val id: String,
    val idNegocio: String,
    val puesto: String,
    val descripcion: String,
    val horario: String,
    val sueldoAprox: Double?,
    val requisitos: List<String>,
    val activa: Boolean = true,
    val fecha: Long
)
