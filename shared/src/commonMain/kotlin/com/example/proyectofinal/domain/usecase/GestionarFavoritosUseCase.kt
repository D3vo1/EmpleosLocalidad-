package com.example.proyectofinal.domain.usecase

import com.example.proyectofinal.domain.model.Negocio
import com.example.proyectofinal.domain.model.Resultado
import com.example.proyectofinal.domain.model.Usuario
import com.example.proyectofinal.domain.repository.FavoritosRepository
import com.example.proyectofinal.domain.repository.NegocioRepository

/** Corazón de las tarjetas y pantalla Favoritos. En modo invitado ([usuario] null) pide iniciar sesión. */
class GestionarFavoritosUseCase(
    private val favoritos: FavoritosRepository,
    private val lectura: NegocioRepository
) {
    fun esFavorito(usuario: Usuario?, idNegocio: String): Boolean =
        usuario != null && idNegocio in favoritos.obtenerFavoritos(usuario.id)

    /** Lo que hace el icono de corazón: si estaba lo quita, si no lo agrega. Devuelve el nuevo estado. */
    fun alternar(usuario: Usuario?, idNegocio: String): Resultado<Boolean> {
        if (usuario == null) return Resultado.Error("Inicia sesión para guardar favoritos")
        if (lectura.obtenerNegocio(idNegocio) == null) return Resultado.Error("El negocio no existe")
        return if (esFavorito(usuario, idNegocio)) {
            favoritos.quitar(usuario.id, idNegocio)
            Resultado.Exito(false)
        } else {
            favoritos.agregar(usuario.id, idNegocio)
            Resultado.Exito(true)
        }
    }

    /** Negocios guardados que todavía existen, ordenados por nombre. */
    fun listar(usuario: Usuario?): List<Negocio> {
        if (usuario == null) return emptyList()
        return favoritos.obtenerFavoritos(usuario.id)
            .mapNotNull { lectura.obtenerNegocio(it) }
            .sortedBy { BuscarNegociosUseCase.normalizar(it.nombre) }
    }
}
