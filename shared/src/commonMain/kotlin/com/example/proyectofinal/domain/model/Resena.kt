package com.example.proyectofinal.domain.model

/**
 * Reseña de un cliente a un negocio.
 * [fecha] en milisegundos desde 1970; sirve para ordenar de la más nueva a la más vieja.
 */
data class Resena(
    val id: String,
    val idNegocio: String,
    val idUsuario: String,
    val nombreUsuario: String,
    val estrellas: Int,
    val comentario: String,
    val fecha: Long
)
