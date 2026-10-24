package com.example.proyectofinal.domain.usecase

import com.example.proyectofinal.domain.model.Campo
import com.example.proyectofinal.domain.model.Resena
import com.example.proyectofinal.domain.model.Resultado
import com.example.proyectofinal.domain.model.Rol
import com.example.proyectofinal.domain.model.Usuario
import com.example.proyectofinal.domain.model.ahoraMillis
import com.example.proyectofinal.domain.model.generarId
import com.example.proyectofinal.domain.repository.GestionNegocioRepository
import com.example.proyectofinal.domain.repository.NegocioRepository

/**
 * Reglas:
 * - [usuario] null = modo invitado → pide iniciar sesión.
 * - Solo cuentas de cliente reseñan; nadie reseña su propio negocio.
 * - Una reseña por usuario por negocio.
 * - Estrellas de 1 a 5; comentario opcional de hasta 500 caracteres.
 */
class AgregarResenaUseCase(
    private val lectura: NegocioRepository,
    private val escritura: GestionNegocioRepository,
    private val nuevoId: () -> String = ::generarId,
    private val ahora: () -> Long = ::ahoraMillis
) {
    operator fun invoke(idNegocio: String, usuario: Usuario?, estrellas: Int, comentario: String): Resultado<Resena> {
        if (usuario == null) return Resultado.Error("Inicia sesión para dejar una reseña")
        if (usuario.rol != Rol.CLIENTE) return Resultado.Error("Solo los clientes pueden dejar reseñas")

        val negocio = lectura.obtenerNegocio(idNegocio) ?: return Resultado.Error("El negocio no existe")
        if (negocio.idDueno == usuario.id) return Resultado.Error("No puedes reseñar tu propio negocio")
        if (lectura.obtenerResenas(idNegocio).any { it.idUsuario == usuario.id }) {
            return Resultado.Error("Ya dejaste una reseña para este negocio")
        }

        val errores = buildMap {
            if (estrellas !in 1..5) put(Campo.ESTRELLAS, "Elige de 1 a 5 estrellas")
            if (comentario.trim().length > 500) put(Campo.COMENTARIO, "Máximo 500 caracteres")
        }
        if (errores.isNotEmpty()) return Resultado.Error("Revisa los campos marcados", errores)

        val resena = Resena(
            id = nuevoId(), idNegocio = idNegocio, idUsuario = usuario.id,
            nombreUsuario = usuario.nombre, estrellas = estrellas,
            comentario = comentario.trim(), fecha = ahora()
        )
        escritura.guardarResena(resena)
        return Resultado.Exito(resena)
    }
}
