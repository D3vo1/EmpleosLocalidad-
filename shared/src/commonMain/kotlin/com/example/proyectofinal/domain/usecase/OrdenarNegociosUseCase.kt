package com.example.proyectofinal.domain.usecase

import com.example.proyectofinal.domain.model.NegocioResumen

enum class Orden {
    MEJOR_CALIFICADOS,  // mayor promedio primero; empate → más reseñas
    MENOR_PRECIO,       // menor precio primero; sin precio al final
    NOMBRE              // A → Z, sin importar acentos
}

class OrdenarNegociosUseCase {

    operator fun invoke(negocios: List<NegocioResumen>, orden: Orden): List<NegocioResumen> =
        when (orden) {
            Orden.MEJOR_CALIFICADOS -> negocios.sortedWith(
                compareByDescending<NegocioResumen> { it.calificacion.promedio }
                    .thenByDescending { it.calificacion.total }
            )
            Orden.MENOR_PRECIO -> negocios.sortedWith(
                compareBy(nullsLast()) { it.precioDesde }
            )
            Orden.NOMBRE -> negocios.sortedBy {
                BuscarNegociosUseCase.normalizar(it.negocio.nombre)
            }
        }
}
