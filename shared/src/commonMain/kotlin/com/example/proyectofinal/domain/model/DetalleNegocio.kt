package com.example.proyectofinal.domain.model

/** Todo lo que muestra la pantalla de Detalle de negocio. */
data class DetalleNegocio(
    val negocio: Negocio,
    val servicios: List<Servicio>,
    val vacantes: List<Vacante>,
    val resenas: List<Resena>,
    val calificacion: Calificacion,
    val abiertoAhora: Boolean
)
