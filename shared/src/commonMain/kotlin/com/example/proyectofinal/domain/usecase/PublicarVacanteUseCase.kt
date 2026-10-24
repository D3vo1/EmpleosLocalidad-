package com.example.proyectofinal.domain.usecase

import com.example.proyectofinal.domain.model.Campo
import com.example.proyectofinal.domain.model.Resultado
import com.example.proyectofinal.domain.model.Vacante
import com.example.proyectofinal.domain.model.ahoraMillis
import com.example.proyectofinal.domain.model.generarId
import com.example.proyectofinal.domain.repository.GestionNegocioRepository
import com.example.proyectofinal.domain.repository.NegocioRepository

/** Datos del formulario de vacante. [requisitos] llega como texto, un requisito por línea. */
data class DatosVacante(
    val puesto: String,
    val descripcion: String,
    val horario: String,
    val sueldoAprox: Double?,
    val requisitos: String
)

/** Pantallas "Mis vacantes" y "Formulario de vacante" del lado negocio. */
class PublicarVacanteUseCase(
    private val lectura: NegocioRepository,
    private val escritura: GestionNegocioRepository,
    private val validar: ValidarFormularioUseCase = ValidarFormularioUseCase(),
    private val nuevoId: () -> String = ::generarId,
    private val ahora: () -> Long = ::ahoraMillis
) {
    private val verificarDueno = VerificarDuenoUseCase(lectura)

    operator fun invoke(idNegocio: String, idUsuario: String, datos: DatosVacante): Resultado<Vacante> {
        val dueno = verificarDueno(idNegocio, idUsuario)
        if (dueno is Resultado.Error) return dueno

        val errores = buildMap {
            validar.nombre(datos.puesto, "el puesto")?.let { put(Campo.PUESTO, it) }
            validar.textoRequerido(datos.descripcion, "la descripción")?.let { put(Campo.DESCRIPCION, it) }
            datos.sueldoAprox?.let { s -> if (s <= 0.0) put(Campo.SUELDO, "El sueldo debe ser mayor a 0") }
        }
        if (errores.isNotEmpty()) return Resultado.Error("Revisa los campos marcados", errores)

        val vacante = Vacante(
            id = nuevoId(),
            idNegocio = idNegocio,
            puesto = datos.puesto.trim(),
            descripcion = datos.descripcion.trim(),
            horario = datos.horario.trim(),
            sueldoAprox = datos.sueldoAprox,
            requisitos = datos.requisitos.lines().map { it.trim().removePrefix("-").trim() }.filter { it.isNotEmpty() },
            activa = true,
            fecha = ahora()
        )
        escritura.guardarVacante(vacante)
        return Resultado.Exito(vacante)
    }

    /** Cerrar o reabrir una vacante sin borrarla. */
    fun cambiarActiva(idVacante: String, idUsuario: String, activa: Boolean): Resultado<Vacante> {
        val actual = escritura.obtenerVacante(idVacante) ?: return Resultado.Error("La vacante no existe")
        val dueno = verificarDueno(actual.idNegocio, idUsuario)
        if (dueno is Resultado.Error) return dueno
        val cambiada = actual.copy(activa = activa)
        escritura.guardarVacante(cambiada)
        return Resultado.Exito(cambiada)
    }

    /** Todas las vacantes del negocio para el dueño, la más nueva primero. */
    fun listar(idNegocio: String): List<Vacante> =
        lectura.obtenerVacantes(idNegocio).sortedByDescending { it.fecha }
}
