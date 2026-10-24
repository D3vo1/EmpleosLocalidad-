package com.example.proyectofinal.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.proyectofinal.data.AppContainer
import com.example.proyectofinal.domain.model.Categoria
import com.example.proyectofinal.domain.model.NegocioResumen
import com.example.proyectofinal.domain.usecase.EstaAbiertoAhoraUseCase
import com.example.proyectofinal.domain.usecase.FiltrosNegocio
import com.example.proyectofinal.domain.usecase.Orden

/**
 * Estado de Explorar. Vive fuera de la pantalla para que la búsqueda y los filtros
 * sigan ahí al regresar del detalle. La UI solo lee [resultados]; la lógica es del domain.
 */
class ExplorarEstado(private val c: AppContainer) {
    var texto by mutableStateOf("")
    var filtros by mutableStateOf(FiltrosNegocio())
    var orden by mutableStateOf(Orden.MEJOR_CALIFICADOS)

    /** Colonia elegida en "Mi colonia"; sus negocios aparecen primero. */
    var coloniaPreferida by mutableStateOf<String?>(null)

    private val estaAbierto = EstaAbiertoAhoraUseCase()

    val resultados: List<NegocioResumen>
        get() = calcular(filtros)

    /** Cuántos resultados habría con otros filtros (para "Ver 8 resultados" en la hoja). */
    fun contar(otros: FiltrosNegocio): Int = calcular(otros).size

    // Se piden los negocios cada vez para ver los que se registren o editen durante la sesión.
    private fun calcular(f: FiltrosNegocio): List<NegocioResumen> {
        val ordenados = c.ordenarNegocios(c.filtrarNegocios(c.buscarNegocios(c.obtenerNegocios(), texto), f), orden)
        val colonia = coloniaPreferida ?: return ordenados
        // sortedBy es estable: dentro de cada grupo se respeta el orden elegido
        return ordenados.sortedBy { if (it.negocio.colonia == colonia) 0 else 1 }
    }

    fun abierto(resumen: NegocioResumen): Boolean = estaAbierto(resumen.negocio.horarios)

    /** Chip rápido: una sola categoría, o todas con null. */
    fun elegirCategoria(categoria: Categoria?) {
        filtros = filtros.copy(categorias = categoria?.let { setOf(it) } ?: emptySet())
    }

    val categoriaRapida: Categoria? get() = filtros.categorias.singleOrNull()

    val filtrosActivos: Int
        get() = filtros.categorias.size +
            (if (filtros.calificacionMinima > 0.0) 1 else 0) +
            (if (filtros.precioMaximo != null) 1 else 0)

    fun limpiar() {
        texto = ""
        filtros = FiltrosNegocio()
    }

    val tituloLista: String
        get() = when {
            texto.isNotBlank() || filtrosActivos > 0 -> "Resultados"
            orden == Orden.MENOR_PRECIO -> "Más accesibles en Jiquilpan"
            orden == Orden.NOMBRE -> "Oficios de la A a la Z"
            else -> "Mejor calificados en Jiquilpan"
        }
}

val Orden.nombreVisible: String
    get() = when (this) {
        Orden.MEJOR_CALIFICADOS -> "Mejor calificados"
        Orden.MENOR_PRECIO -> "Menor precio"
        Orden.NOMBRE -> "Nombre A–Z"
    }
