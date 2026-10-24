package com.example.proyectofinal.domain.usecase

import com.example.proyectofinal.domain.model.Negocio
import com.example.proyectofinal.domain.model.Resultado
import com.example.proyectofinal.domain.repository.NegocioRepository

/**
 * Regla de permisos: un negocio solo lo modifica su dueño.
 * La usan GestionarServicios y PublicarVacante.
 */
class VerificarDuenoUseCase(private val lectura: NegocioRepository) {

    operator fun invoke(idNegocio: String, idUsuario: String): Resultado<Negocio> {
        val negocio = lectura.obtenerNegocio(idNegocio)
            ?: return Resultado.Error("El negocio no existe")
        if (negocio.idDueno != idUsuario) return Resultado.Error("Solo el dueño puede modificar este negocio")
        return Resultado.Exito(negocio)
    }
}
