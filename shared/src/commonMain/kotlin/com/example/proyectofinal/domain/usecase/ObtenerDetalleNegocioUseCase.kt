package com.example.proyectofinal.domain.usecase

import com.example.proyectofinal.domain.model.DetalleNegocio
import com.example.proyectofinal.domain.repository.NegocioRepository

/**
 * Junta todo lo de la pantalla de detalle. Devuelve null si el id no existe.
 * Solo incluye servicios y vacantes activos; reseñas y vacantes van de la más nueva a la más vieja.
 */
class ObtenerDetalleNegocioUseCase(
    private val repositorio: NegocioRepository,
    private val calcularCalificacion: CalcularCalificacionUseCase = CalcularCalificacionUseCase(),
    private val estaAbierto: EstaAbiertoAhoraUseCase = EstaAbiertoAhoraUseCase()
) {
    operator fun invoke(idNegocio: String): DetalleNegocio? {
        val negocio = repositorio.obtenerNegocio(idNegocio) ?: return null
        val resenas = repositorio.obtenerResenas(idNegocio).sortedByDescending { it.fecha }
        return DetalleNegocio(
            negocio = negocio,
            servicios = repositorio.obtenerServicios(idNegocio).filter { it.activo },
            vacantes = repositorio.obtenerVacantes(idNegocio)
                .filter { it.activa }
                .sortedByDescending { it.fecha },
            resenas = resenas,
            calificacion = calcularCalificacion(resenas),
            abiertoAhora = estaAbierto(negocio.horarios)
        )
    }
}
