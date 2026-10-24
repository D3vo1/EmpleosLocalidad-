package com.example.proyectofinal.domain.usecase

import com.example.proyectofinal.domain.model.NegocioResumen
import com.example.proyectofinal.domain.repository.NegocioRepository

/**
 * Arma la lista de tarjetas de Explorar: cada negocio con su calificación
 * y el precio más bajo de sus servicios activos.
 * Después la UI le aplica Buscar → Filtrar → Ordenar.
 */
class ObtenerNegociosUseCase(
    private val repositorio: NegocioRepository,
    private val calcularCalificacion: CalcularCalificacionUseCase = CalcularCalificacionUseCase()
) {
    operator fun invoke(): List<NegocioResumen> =
        repositorio.obtenerNegocios().map { negocio ->
            val activos = repositorio.obtenerServicios(negocio.id).filter { it.activo }
            NegocioResumen(
                negocio = negocio,
                calificacion = calcularCalificacion(repositorio.obtenerResenas(negocio.id)),
                precioDesde = activos.minOfOrNull { it.precioAprox }
            )
        }
}
