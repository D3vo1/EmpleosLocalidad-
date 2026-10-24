package com.example.proyectofinal.domain.usecase

import com.example.proyectofinal.domain.model.Campo
import com.example.proyectofinal.domain.model.Resultado
import com.example.proyectofinal.domain.model.Servicio
import com.example.proyectofinal.domain.model.generarId
import com.example.proyectofinal.domain.repository.GestionNegocioRepository
import com.example.proyectofinal.domain.repository.NegocioRepository

/** Datos del formulario de servicio. [precio] es null si el campo está vacío o no es número. */
data class DatosServicio(
    val nombre: String,
    val descripcion: String,
    val precio: Double?,
    val esPrecioDesde: Boolean
)

/** Pantallas "Mis servicios" y "Formulario de servicio" del lado negocio. */
class GestionarServiciosUseCase(
    private val lectura: NegocioRepository,
    private val escritura: GestionNegocioRepository,
    private val validar: ValidarFormularioUseCase = ValidarFormularioUseCase(),
    private val nuevoId: () -> String = ::generarId
) {
    private val verificarDueno = VerificarDuenoUseCase(lectura)

    /** Todos los servicios del negocio, activos e inactivos, para el dueño. */
    fun listar(idNegocio: String): List<Servicio> =
        lectura.obtenerServicios(idNegocio).sortedBy { it.nombre.lowercase() }

    fun agregar(idNegocio: String, idUsuario: String, datos: DatosServicio): Resultado<Servicio> {
        val dueno = verificarDueno(idNegocio, idUsuario)
        if (dueno is Resultado.Error) return dueno
        validarDatos(datos)?.let { return it }
        if (lectura.obtenerServicios(idNegocio).size >= MAX_SERVICIOS) {
            return Resultado.Error("Máximo $MAX_SERVICIOS servicios por negocio")
        }
        val servicio = Servicio(
            id = nuevoId(), idNegocio = idNegocio, nombre = datos.nombre.trim(),
            descripcion = datos.descripcion.trim(), precioAprox = datos.precio!!,
            esPrecioDesde = datos.esPrecioDesde, activo = true
        )
        escritura.guardarServicio(servicio)
        return Resultado.Exito(servicio)
    }

    fun editar(idServicio: String, idUsuario: String, datos: DatosServicio): Resultado<Servicio> {
        val actual = escritura.obtenerServicio(idServicio) ?: return Resultado.Error("El servicio no existe")
        val dueno = verificarDueno(actual.idNegocio, idUsuario)
        if (dueno is Resultado.Error) return dueno
        validarDatos(datos)?.let { return it }
        val editado = actual.copy(
            nombre = datos.nombre.trim(), descripcion = datos.descripcion.trim(),
            precioAprox = datos.precio!!, esPrecioDesde = datos.esPrecioDesde
        )
        escritura.guardarServicio(editado)
        return Resultado.Exito(editado)
    }

    /** El switch activo/inactivo de la lista. */
    fun cambiarActivo(idServicio: String, idUsuario: String, activo: Boolean): Resultado<Servicio> {
        val actual = escritura.obtenerServicio(idServicio) ?: return Resultado.Error("El servicio no existe")
        val dueno = verificarDueno(actual.idNegocio, idUsuario)
        if (dueno is Resultado.Error) return dueno
        val cambiado = actual.copy(activo = activo)
        escritura.guardarServicio(cambiado)
        return Resultado.Exito(cambiado)
    }

    private fun validarDatos(d: DatosServicio): Resultado.Error? {
        val errores = buildMap {
            validar.nombre(d.nombre, "el nombre del servicio")?.let { put(Campo.NOMBRE_SERVICIO, it) }
            validar.precio(d.precio)?.let { put(Campo.PRECIO, it) }
            if (d.descripcion.length > 300) put(Campo.DESCRIPCION, "Máximo 300 caracteres")
        }
        return if (errores.isEmpty()) null else Resultado.Error("Revisa los campos marcados", errores)
    }

    companion object {
        const val MAX_SERVICIOS = 20
    }
}
