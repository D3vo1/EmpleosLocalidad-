package com.example.proyectofinal.ui.screens

import com.example.proyectofinal.data.AppContainer
import com.example.proyectofinal.data.DatosSemilla
import com.example.proyectofinal.domain.model.Campo
import com.example.proyectofinal.domain.model.Categoria
import com.example.proyectofinal.domain.model.DiaSemana
import com.example.proyectofinal.domain.model.Rol
import com.example.proyectofinal.ui.navigation.Navegador
import com.example.proyectofinal.ui.navigation.Pantalla
import kotlin.test.*

class RegistroNegocioEstadoTest {
    private val c = AppContainer()
    private val e = RegistroNegocioEstado(c.registrarNegocio, c.validar)

    private fun llenarHastaPaso4() {
        e.nombreNegocio = "Herrería El Yunque"; e.oficio = "Herrero"; e.categoria = Categoria.HERRERIA
        assertNull(e.continuar()); assertEquals(2, e.paso)
        e.direccion = "Calle Morelos 12"; e.colonia = "Col. Centro"
        assertNull(e.continuar()); assertEquals(3, e.paso)
        e.descripcion = "Portones y barandales"
        assertNull(e.continuar()); assertEquals(4, e.paso)
    }

    @Test fun noAvanzaSinNombre() {
        assertNull(e.continuar())
        assertEquals(1, e.paso)
        assertTrue(Campo.NOMBRE_NEGOCIO in e.errores)
    }

    @Test fun registroCompletoCreaNegocioVisibleEnExplorar() {
        llenarHastaPaso4()
        e.telefono = "353 555 9999"; e.nombrePersona = "Pedro Ruiz"; e.correo = "pedro@ejemplo.com"
        e.contrasena = "yunque123"; e.confirmacion = "yunque123"
        val (dueno, negocio) = assertNotNull(e.continuar())
        assertEquals(Rol.NEGOCIO, dueno.rol)
        assertEquals("Col. Centro", negocio.colonia)
        assertEquals(6, negocio.horarios.size) // L-V + sábado
        assertEquals(negocio, c.negocioDelDueno(dueno))
        assertTrue(c.obtenerNegocios().any { it.negocio.id == negocio.id })
    }

    @Test fun correoRepetidoQuedaEnPaso4ConError() {
        llenarHastaPaso4()
        e.telefono = "3535559999"; e.nombrePersona = "Pedro"; e.correo = DatosSemilla.CLIENTE_DEMO
        e.contrasena = "yunque123"; e.confirmacion = "yunque123"
        assertNull(e.continuar())
        assertEquals(4, e.paso)
        assertEquals("Ya existe una cuenta con este correo", e.errores[Campo.CORREO])
    }

    @Test fun horario24HorasYCierreAntesDeApertura() {
        e.veinticuatroHoras = true
        assertEquals(7, e.horarios().size)
        assertTrue(e.horarios().all { it.apertura == 0 && it.cierre == 1440 })
        e.veinticuatroHoras = false; e.domingo = true
        assertTrue(e.horarios().any { it.dia == DiaSemana.DOMINGO })
    }

    @Test fun regresarDesdePaso1Sale() {
        assertFalse(e.regresar())
    }
}

class ExplorarYNavegacionTest {
    private val c = AppContainer()

    @Test fun coloniaPreferidaVaPrimeroRespetandoOrden() {
        val e = ExplorarEstado(c)
        e.coloniaPreferida = "Col. Revolución"
        val r = e.resultados
        val primeros = r.takeWhile { it.negocio.colonia == "Col. Revolución" }
        assertEquals(r.count { it.negocio.colonia == "Col. Revolución" }, primeros.size)
        assertTrue(primeros.isNotEmpty())
    }

    @Test fun coloniasSinRepetir() {
        assertEquals(c.colonias.size, c.colonias.toSet().size)
        assertTrue("Col. Centro" in c.colonias)
    }

    @Test fun navegadorReiniciaYVuelve() {
        val n = Navegador()
        n.reiniciarEn(Pantalla.Principal)
        n.ir(Pantalla.PermisoUbicacion(primeraVez = true))
        assertTrue(n.puedeVolver)
        assertTrue(n.volver())
        assertEquals(Pantalla.Principal, n.actual)
        assertFalse(n.volver())
    }
}
