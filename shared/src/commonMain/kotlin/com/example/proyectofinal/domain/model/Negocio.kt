package com.example.proyectofinal.domain.model

/**
 * Negocio u oficio registrado en la app.
 * [fotos] guarda los nombres de las imágenes en composeResources/drawable
 * (ej. "plomeria_juan_1"); la primera es la portada. Se usan nombres y no
 * ids de Android para que el domain funcione igual en Android e iOS.
 * [colonia] es lo que muestran las tarjetas ("Col. Centro"); va al final con valor
 * por defecto para no romper el código que crea negocios por posición.
 */
data class Negocio(
    val id: String,
    val nombre: String,
    val oficio: String,
    val categoria: Categoria,
    val descripcion: String,
    val direccion: String,
    val telefono: String,
    val whatsapp: String,
    val fotos: List<String>,
    val horarios: List<Horario>,
    val idDueno: String,
    val colonia: String = ""
)
