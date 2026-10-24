package com.example.proyectofinal.data

import com.example.proyectofinal.domain.model.Categoria
import com.example.proyectofinal.domain.model.Resultado
import com.example.proyectofinal.domain.model.Rol
import kotlin.test.*

class DatosSemillaTest {
    private val semilla = DatosSemilla.crear()
    private val container = AppContainer()

    @Test fun idsUnicos() {
        assertEquals(semilla.negocios.size, semilla.negocios.map { it.id }.toSet().size)
        assertEquals(semilla.servicios.size, semilla.servicios.map { it.id }.toSet().size)
        assertEquals(semilla.resenas.size, semilla.resenas.map { it.id }.toSet().size)
    }

    @Test fun cadaNegocioTieneServicioResenaYColonia() {
        val tarjetas = container.obtenerNegocios()
        assertEquals(16, tarjetas.size)
        tarjetas.forEach {
            assertNotNull(it.precioDesde, it.negocio.nombre)
            assertTrue(it.calificacion.total > 0, it.negocio.nombre)
            assertTrue(it.negocio.colonia.isNotBlank(), it.negocio.nombre)
        }
    }

    @Test fun todasLasCategoriasTienenAlMenosUnNegocio() {
        assertEquals(Categoria.entries.toSet(), semilla.negocios.map { it.categoria }.toSet())
    }

    @Test fun loginDemoFunciona() {
        val cliente = container.iniciarSesion(DatosSemilla.CLIENTE_DEMO, DatosSemilla.CONTRASENA_DEMO, Rol.CLIENTE)
        assertIs<Resultado.Exito<*>>(cliente)
        val dueno = container.iniciarSesion(DatosSemilla.NEGOCIO_DEMO, DatosSemilla.CONTRASENA_DEMO, Rol.NEGOCIO)
        assertIs<Resultado.Exito<*>>(dueno)
    }

    @Test fun detalleIncluyeVacantes() {
        val detalle = assertNotNull(container.obtenerDetalle("n01"))
        assertEquals(1, detalle.vacantes.size)
        assertEquals(3, detalle.servicios.size)
    }

    @Test fun busquedaSinAcentoEncuentraPlomerias() {
        val r = container.buscarNegocios(container.obtenerNegocios(), "plomeria")
        assertEquals(setOf("n03", "n11"), r.map { it.negocio.id }.toSet())
    }
}
