package com.example.proyectofinal.domain.usecase

import com.example.proyectofinal.domain.model.*
import com.example.proyectofinal.domain.usecase.DatosPrueba.ingles
import com.example.proyectofinal.domain.usecase.DatosPrueba.luz
import com.example.proyectofinal.domain.usecase.DatosPrueba.plomeria
import com.example.proyectofinal.domain.usecase.DatosPrueba.resena
import com.example.proyectofinal.domain.usecase.DatosPrueba.resumen
import kotlin.test.*

class CalcularCalificacionTest {
    private val calcular = CalcularCalificacionUseCase()

    @Test fun promedioRedondeadoAUnDecimal() {
        val c = calcular(listOf(resena("1", 5), resena("1", 4), resena("1", 4)))
        assertEquals(4.3, c.promedio)
        assertEquals(3, c.total)
        assertEquals(2, c.distribucion[4])
    }

    @Test fun sinResenasDevuelveVacia() {
        assertEquals(Calificacion.VACIA, calcular(emptyList()))
    }

    @Test fun ignoraEstrellasInvalidas() {
        assertEquals(1, calcular(listOf(resena("1", 5), resena("1", 9))).total)
    }
}

class BuscarNegociosTest {
    private val buscar = BuscarNegociosUseCase()
    private val lista = listOf(resumen(plomeria, 4.5, 10, 300.0), resumen(luz, 4.0, 5, 500.0),
        resumen(ingles, 5.0, 2, 150.0))

    @Test fun ignoraAcentosYMayusculas() {
        assertEquals(listOf(plomeria), buscar(lista, "PLOMERIA").map { it.negocio })
    }

    @Test fun todasLasPalabrasDebenAparecer() {
        assertEquals(listOf(ingles), buscar(lista, "clases ingles").map { it.negocio })
    }

    @Test fun textoVacioDevuelveTodo() {
        assertEquals(3, buscar(lista, "   ").size)
    }

    @Test fun sinCoincidenciasDevuelveVacia() {
        assertTrue(buscar(lista, "fotografo").isEmpty())
    }
}

class FiltrarYOrdenarTest {
    private val filtrar = FiltrarNegociosUseCase()
    private val ordenar = OrdenarNegociosUseCase()
    private val lista = listOf(resumen(plomeria, 4.5, 10, 300.0), resumen(luz, 4.5, 20, null),
        resumen(ingles, 3.0, 2, 150.0))

    @Test fun filtraPorCategoria() {
        val r = filtrar(lista, FiltrosNegocio(categorias = setOf(Categoria.CLASES)))
        assertEquals(listOf(ingles), r.map { it.negocio })
    }

    @Test fun filtraPorCalificacionYPrecio() {
        val r = filtrar(lista, FiltrosNegocio(calificacionMinima = 4.0, precioMaximo = 400.0))
        // Electro Rápido no tiene precio, así que queda fuera
        assertEquals(listOf(plomeria), r.map { it.negocio })
    }

    @Test fun mejorCalificadosDesempataPorNumeroDeResenas() {
        assertEquals(listOf(luz, plomeria, ingles),
            ordenar(lista, Orden.MEJOR_CALIFICADOS).map { it.negocio })
    }

    @Test fun menorPrecioDejaSinPrecioAlFinal() {
        assertEquals(listOf(ingles, plomeria, luz),
            ordenar(lista, Orden.MENOR_PRECIO).map { it.negocio })
    }

    @Test fun porNombreIgnoraAcentos() {
        assertEquals(listOf(luz, plomeria, ingles), ordenar(lista, Orden.NOMBRE).map { it.negocio })
    }
}

class EstaAbiertoAhoraTest {
    private val semana = listOf(
        Horario.de(DiaSemana.LUNES, 9, 0, 18, 0),
        Horario.de(DiaSemana.SABADO, 20, 0, 2, 0) // cruza medianoche
    )
    private val caso = EstaAbiertoAhoraUseCase()

    @Test fun abiertoDentroDelHorario() = assertTrue(caso.estaAbierto(semana, DiaSemana.LUNES, 10 * 60))
    @Test fun cerradoALaHoraDeCierre() = assertFalse(caso.estaAbierto(semana, DiaSemana.LUNES, 18 * 60))
    @Test fun cerradoDiaSinHorario() = assertFalse(caso.estaAbierto(semana, DiaSemana.MARTES, 10 * 60))
    @Test fun abiertoSabadoNoche() = assertTrue(caso.estaAbierto(semana, DiaSemana.SABADO, 23 * 60))
    @Test fun abiertoDomingoMadrugadaPorHorarioDelSabado() =
        assertTrue(caso.estaAbierto(semana, DiaSemana.DOMINGO, 60))
    @Test fun cerradoDomingoDespuesDeLasDos() =
        assertFalse(caso.estaAbierto(semana, DiaSemana.DOMINGO, 3 * 60))

    @Test fun usaElRelojInyectado() {
        val fijo = EstaAbiertoAhoraUseCase { MomentoActual(DiaSemana.LUNES, 12 * 60) }
        assertTrue(fijo(semana))
    }
}

class ObtenerNegociosYDetalleTest {
    private val repo = DatosPrueba.RepositorioFalso(
        negocios = listOf(plomeria.copy(horarios = listOf(Horario.de(DiaSemana.LUNES, 9, 0, 18, 0)))),
        servicios = listOf(
            Servicio("s1", "1", "Fuga", "", 300.0),
            Servicio("s2", "1", "Tinaco", "", 200.0, activo = false),
            Servicio("s3", "1", "Boiler", "", 450.0)
        ),
        resenas = listOf(resena("1", 5, fecha = 1L), resena("1", 3, fecha = 2L))
    )

    @Test fun resumenUsaPrecioMinimoDeServiciosActivos() {
        val r = ObtenerNegociosUseCase(repo)().single()
        assertEquals(300.0, r.precioDesde) // el de 200 está inactivo
        assertEquals(4.0, r.calificacion.promedio)
    }

    @Test fun detalleOrdenaResenasYOcultaInactivos() {
        val caso = ObtenerDetalleNegocioUseCase(repo,
            estaAbierto = EstaAbiertoAhoraUseCase { MomentoActual(DiaSemana.LUNES, 10 * 60) })
        val d = assertNotNull(caso("1"))
        assertEquals(listOf("s1", "s3"), d.servicios.map { it.id })
        assertEquals(listOf(2L, 1L), d.resenas.map { it.fecha })
        assertTrue(d.abiertoAhora)
    }

    @Test fun detalleDeIdInexistenteEsNull() {
        assertNull(ObtenerDetalleNegocioUseCase(repo)("999"))
    }
}
