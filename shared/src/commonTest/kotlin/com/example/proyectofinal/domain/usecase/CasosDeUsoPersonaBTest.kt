package com.example.proyectofinal.domain.usecase

import com.example.proyectofinal.domain.model.*
import kotlin.test.*

private val cuentaValida = DatosCuenta("Ana López", " Ana@Correo.com ", "353 123-4567", "secreta123", "secreta123")

private val negocioValido = DatosNegocio(
    nombre = "Plomería Don Juan", oficio = "Plomero", categoria = Categoria.PLOMERIA,
    descripcion = "Fugas y tinacos", direccion = "Calle Hidalgo 10, Jiquilpan",
    whatsapp = "", fotos = List(8) { "foto$it" },
    horarios = listOf(Horario.de(DiaSemana.LUNES, 9, 0, 18, 0))
)

private fun Resultado<*>.error(): Resultado.Error = assertIs<Resultado.Error>(this)
private fun <T> Resultado<T>.exito(): T = assertIs<Resultado.Exito<T>>(this).dato

class ValidarFormularioTest {
    private val v = ValidarFormularioUseCase()

    @Test fun correo() {
        assertNull(v.correo("  Ana@Correo.COM "))
        assertNotNull(v.correo("ana@correo"))
        assertNotNull(v.correo(""))
    }

    @Test fun telefonoDe10DigitosConEspaciosYGuiones() {
        assertNull(v.telefono("353 123-4567"))
        assertEquals("Debe tener 10 dígitos", v.telefono("353123"))
        assertEquals("Solo números", v.telefono("353abc4567"))
    }

    @Test fun contrasenaConLetrasYNumeros() {
        assertNull(v.contrasena("secreta123"))
        assertEquals("Mínimo 8 caracteres", v.contrasena("abc1"))
        assertEquals("Usa letras y números", v.contrasena("solotexto"))
    }

    @Test fun precio() {
        assertNull(v.precio(250.0))
        assertNotNull(v.precio(0.0))
        assertNotNull(v.precio(null))
    }
}

class CuentasTest {
    private val repo = MemoriaFalsa()
    private val ids = IdsSecuenciales()
    private val registrar = RegistrarClienteUseCase(repo, nuevoId = ids::siguiente)
    private val registrarNegocio = RegistrarNegocioUseCase(repo, repo, nuevoId = ids::siguiente)
    private val login = IniciarSesionUseCase(repo)

    @Test fun registroNormalizaCorreoYTelefono() {
        val u = registrar(cuentaValida).exito()
        assertEquals("ana@correo.com", u.correo)
        assertEquals("3531234567", u.telefono)
        assertEquals(Rol.CLIENTE, u.rol)
        assertEquals(u, repo.usuarios[u.id])
    }

    @Test fun registroJuntaTodosLosErroresPorCampo() {
        val e = registrar(DatosCuenta("", "malo", "12", "corta", "otra")).error()
        assertEquals(setOf(Campo.NOMBRE, Campo.CORREO, Campo.TELEFONO, Campo.CONTRASENA, Campo.CONFIRMACION), e.errores.keys)
        assertTrue(repo.usuarios.isEmpty())
    }

    @Test fun correoRepetidoSinImportarMayusculas() {
        registrar(cuentaValida)
        val e = registrar(cuentaValida.copy(correo = "ANA@correo.com")).error()
        assertEquals("Ya existe una cuenta con este correo", e.errores[Campo.CORREO])
    }

    @Test fun loginCorrecto() {
        registrar(cuentaValida)
        assertEquals("Ana López", login("ANA@correo.com", "secreta123", Rol.CLIENTE).exito().nombre)
    }

    @Test fun loginMismoMensajeParaCorreoOContrasenaMal() {
        registrar(cuentaValida)
        assertEquals(login("ana@correo.com", "otra12345", Rol.CLIENTE).error().mensaje,
            login("nadie@correo.com", "secreta123", Rol.CLIENTE).error().mensaje)
    }

    @Test fun loginConRolEquivocado() {
        registrar(cuentaValida)
        assertEquals("Esta cuenta es de cliente. Entra por \"Busco servicios\"",
            login("ana@correo.com", "secreta123", Rol.NEGOCIO).error().mensaje)
    }

    @Test fun registroNegocioCreaDuenoYNegocio() {
        val (dueno, negocio) = registrarNegocio(cuentaValida, negocioValido).exito()
        assertEquals(Rol.NEGOCIO, dueno.rol)
        assertEquals(dueno.id, negocio.idDueno)
        assertEquals("3531234567", negocio.whatsapp) // sin WhatsApp usa el teléfono
        assertEquals(RegistrarNegocioUseCase.MAX_FOTOS, negocio.fotos.size)
        assertEquals(negocio, repo.negocios[negocio.id])
    }

    @Test fun registroNegocioValidaHorarios() {
        val sinHorario = registrarNegocio(cuentaValida, negocioValido.copy(horarios = emptyList())).error()
        assertTrue(Campo.HORARIOS in sinHorario.errores)
        val repetido = negocioValido.copy(horarios = List(2) { Horario.de(DiaSemana.LUNES, 9, 0, 14, 0) })
        assertEquals("Hay un día repetido", registrarNegocio(cuentaValida, repetido).error().errores[Campo.HORARIOS])
        assertTrue(repo.usuarios.isEmpty()) // no se guarda nada si algo falla
    }
}

class LadoNegocioTest {
    private val repo = MemoriaFalsa()
    private val ids = IdsSecuenciales()
    private val servicios = GestionarServiciosUseCase(repo, repo, nuevoId = ids::siguiente)
    private val vacantes = PublicarVacanteUseCase(repo, repo, nuevoId = ids::siguiente, ahora = { 1000L })
    private val dueno: Usuario
    private val negocio: Negocio

    init {
        val r = RegistrarNegocioUseCase(repo, repo, nuevoId = ids::siguiente)(cuentaValida, negocioValido).exito()
        dueno = r.first; negocio = r.second
    }

    private val datosServicio = DatosServicio("  Reparar fuga ", "Incluye material básico", 350.0, true)

    @Test fun agregarYEditarServicio() {
        val s = servicios.agregar(negocio.id, dueno.id, datosServicio).exito()
        assertEquals("Reparar fuga", s.nombre)
        val editado = servicios.editar(s.id, dueno.id, datosServicio.copy(precio = 400.0)).exito()
        assertEquals(400.0, repo.servicios[s.id]?.precioAprox)
        assertEquals(s.id, editado.id)
    }

    @Test fun soloElDuenoModifica() {
        val e = servicios.agregar(negocio.id, "otro-usuario", datosServicio).error()
        assertEquals("Solo el dueño puede modificar este negocio", e.mensaje)
        assertTrue(repo.servicios.isEmpty())
    }

    @Test fun servicioSinPrecioEsError() {
        assertTrue(Campo.PRECIO in servicios.agregar(negocio.id, dueno.id, datosServicio.copy(precio = null)).error().errores)
    }

    @Test fun desactivarServicioLoOcultaDelDetalle() {
        val s = servicios.agregar(negocio.id, dueno.id, datosServicio).exito()
        servicios.cambiarActivo(s.id, dueno.id, false).exito()
        assertEquals(1, servicios.listar(negocio.id).size) // el dueño lo sigue viendo
        assertTrue(ObtenerDetalleNegocioUseCase(repo)(negocio.id)!!.servicios.isEmpty())
    }

    @Test fun publicarVacanteSeparaRequisitos() {
        val v = vacantes(negocio.id, dueno.id,
            DatosVacante("Ayudante", "Apoyo en instalaciones", "L-V 9 a 14", null, "- Mayor de edad\n\nSaber manejar\n")).exito()
        assertEquals(listOf("Mayor de edad", "Saber manejar"), v.requisitos)
        assertEquals(1000L, v.fecha)
        assertNull(v.sueldoAprox)
    }

    @Test fun vacanteCerradaNoApareceEnDetalle() {
        val v = vacantes(negocio.id, dueno.id, DatosVacante("Ayudante", "Apoyo", "", 2500.0, "")).exito()
        assertEquals(1, ObtenerDetalleNegocioUseCase(repo)(negocio.id)!!.vacantes.size)
        vacantes.cambiarActiva(v.id, dueno.id, false).exito()
        assertTrue(ObtenerDetalleNegocioUseCase(repo)(negocio.id)!!.vacantes.isEmpty())
    }

    @Test fun vacanteConSueldoNegativo() {
        assertTrue(Campo.SUELDO in vacantes(negocio.id, dueno.id, DatosVacante("Ayudante", "Apoyo", "", -5.0, "")).error().errores)
    }
}

class ResenasYFavoritosTest {
    private val repo = MemoriaFalsa()
    private val ids = IdsSecuenciales()
    private val resenar = AgregarResenaUseCase(repo, repo, nuevoId = ids::siguiente, ahora = { 5L })
    private val favoritos = GestionarFavoritosUseCase(repo, repo)
    private val dueno: Usuario
    private val negocio: Negocio
    private val cliente: Usuario

    init {
        val r = RegistrarNegocioUseCase(repo, repo, nuevoId = ids::siguiente)(cuentaValida, negocioValido).exito()
        dueno = r.first; negocio = r.second
        cliente = RegistrarClienteUseCase(repo, nuevoId = ids::siguiente)(
            cuentaValida.copy(nombre = "Luis", correo = "luis@correo.com")).exito()
    }

    @Test fun clienteResenaUnaSolaVez() {
        val r = resenar(negocio.id, cliente, 5, "  Muy cumplido ").exito()
        assertEquals("Muy cumplido", r.comentario)
        assertEquals("Luis", r.nombreUsuario)
        assertEquals("Ya dejaste una reseña para este negocio", resenar(negocio.id, cliente, 4, "").error().mensaje)
    }

    @Test fun invitadoYDuenoNoResenan() {
        assertEquals("Inicia sesión para dejar una reseña", resenar(negocio.id, null, 5, "").error().mensaje)
        assertEquals("Solo los clientes pueden dejar reseñas", resenar(negocio.id, dueno, 5, "").error().mensaje)
    }

    @Test fun estrellasFueraDeRango() {
        assertTrue(Campo.ESTRELLAS in resenar(negocio.id, cliente, 0, "").error().errores)
        assertTrue(repo.resenas.isEmpty())
    }

    @Test fun alternarFavorito() {
        assertTrue(favoritos.alternar(cliente, negocio.id).exito())
        assertEquals(listOf(negocio), favoritos.listar(cliente))
        assertFalse(favoritos.alternar(cliente, negocio.id).exito())
        assertTrue(favoritos.listar(cliente).isEmpty())
    }

    @Test fun invitadoNoGuardaFavoritos() {
        assertEquals("Inicia sesión para guardar favoritos", favoritos.alternar(null, negocio.id).error().mensaje)
        assertFalse(favoritos.esFavorito(null, negocio.id))
    }

    @Test fun favoritoDeNegocioInexistente() {
        assertEquals("El negocio no existe", favoritos.alternar(cliente, "nope").error().mensaje)
    }
}
