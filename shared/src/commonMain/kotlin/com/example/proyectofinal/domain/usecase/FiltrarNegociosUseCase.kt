package com.example.proyectofinal.domain.usecase

import com.example.proyectofinal.domain.model.Categoria
import com.example.proyectofinal.domain.model.NegocioResumen

/**
 * Filtros de la hoja de filtros de Explorar.
 * Un set de categorías vacío significa "todas".
 */
data class FiltrosNegocio(
    val categorias: Set<Categoria> = emptySet(),
    val calificacionMinima: Double = 0.0,
    val precioMaximo: Double? = null
)

class FiltrarNegociosUseCase {

    operator fun invoke(negocios: List<NegocioResumen>, filtros: FiltrosNegocio): List<NegocioResumen> =
        negocios.filter { r ->
            val okCategoria = filtros.categorias.isEmpty() || r.negocio.categoria in filtros.categorias
            val okCalificacion = r.calificacion.promedio >= filtros.calificacionMinima
            // Si hay precio máximo, se excluyen negocios sin servicios activos (no tienen precio)
            val okPrecio = filtros.precioMaximo == null ||
                (r.precioDesde != null && r.precioDesde <= filtros.precioMaximo)
            okCategoria && okCalificacion && okPrecio
        }
}
