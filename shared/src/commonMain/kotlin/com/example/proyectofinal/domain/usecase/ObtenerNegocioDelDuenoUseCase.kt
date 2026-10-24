package com.example.proyectofinal.domain.usecase

import com.example.proyectofinal.domain.model.Negocio
import com.example.proyectofinal.domain.model.Rol
import com.example.proyectofinal.domain.model.Usuario
import com.example.proyectofinal.domain.repository.NegocioRepository

/**
 * Negocio que administra el usuario. Devuelve null si no es cuenta de negocio
 * o si todavía no tiene negocio registrado. Lo usa el Panel del negocio.
 */
class ObtenerNegocioDelDuenoUseCase(private val lectura: NegocioRepository) {
    operator fun invoke(usuario: Usuario?): Negocio? {
        if (usuario == null || usuario.rol != Rol.NEGOCIO) return null
        return lectura.obtenerNegocios().firstOrNull { it.idDueno == usuario.id }
    }
}
