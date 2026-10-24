package com.example.proyectofinal.domain.repository

/** Negocios guardados por cada usuario (Persona B). */
interface FavoritosRepository {
    fun obtenerFavoritos(idUsuario: String): Set<String>
    fun agregar(idUsuario: String, idNegocio: String)
    fun quitar(idUsuario: String, idNegocio: String)
}
