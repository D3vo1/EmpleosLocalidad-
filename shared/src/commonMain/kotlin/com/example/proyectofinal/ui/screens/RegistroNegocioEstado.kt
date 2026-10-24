package com.example.proyectofinal.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.proyectofinal.domain.model.Campo
import com.example.proyectofinal.domain.model.Categoria
import com.example.proyectofinal.domain.model.DiaSemana
import com.example.proyectofinal.domain.model.Horario
import com.example.proyectofinal.domain.model.Negocio
import com.example.proyectofinal.domain.model.Resultado
import com.example.proyectofinal.domain.model.Usuario
import com.example.proyectofinal.domain.usecase.DatosCuenta
import com.example.proyectofinal.domain.usecase.DatosNegocio
import com.example.proyectofinal.domain.usecase.RegistrarNegocioUseCase
import com.example.proyectofinal.domain.usecase.ValidarFormularioUseCase

/**
 * Estado del registro de negocio en 4 pasos (boceto 02).
 * Cada paso revisa sus campos antes de avanzar; al final el caso de uso valida todo
 * y, si algo falla, se regresa al primer paso que tiene error.
 */
class RegistroNegocioEstado(
    private val registrar: RegistrarNegocioUseCase,
    private val validar: ValidarFormularioUseCase
) {
    var paso by mutableStateOf(1)
        private set

    // Paso 1 · Datos básicos
    var nombreNegocio by mutableStateOf("")
    var oficio by mutableStateOf("")
    var categoria by mutableStateOf(Categoria.CARPINTERIA)

    // Paso 2 · Ubicación
    var direccion by mutableStateOf("")
    var colonia by mutableStateOf("")

    // Paso 3 · Descripción
    var descripcion by mutableStateOf("")

    // Paso 4 · Horario y contacto
    var lunesAViernes by mutableStateOf(true)
    var abreSemana by mutableStateOf(9)
    var cierraSemana by mutableStateOf(18)
    var sabado by mutableStateOf(true)
    var abreSabado by mutableStateOf(9)
    var cierraSabado by mutableStateOf(14)
    var domingo by mutableStateOf(false)
    var veinticuatroHoras by mutableStateOf(false)
    var telefono by mutableStateOf("")
    var whatsappIgual by mutableStateOf(true)
    var whatsapp by mutableStateOf("")
    var nombrePersona by mutableStateOf("")
    var correo by mutableStateOf("")
    var contrasena by mutableStateOf("")
    var confirmacion by mutableStateOf("")

    var errores by mutableStateOf<Map<Campo, String>>(emptyMap())
        private set

    val nombrePaso: String
        get() = when (paso) { 1 -> "Datos básicos"; 2 -> "Ubicación"; 3 -> "Descripción"; else -> "Horario y contacto" }

    fun regresar(): Boolean {
        if (paso == 1) return false
        paso--
        return true
    }

    /** Avanza si el paso actual está bien. En el paso 4 registra y devuelve dueño y negocio. */
    fun continuar(): Pair<Usuario, Negocio>? {
        val delPaso = erroresDelPaso()
        errores = delPaso
        if (delPaso.isNotEmpty()) return null
        if (paso < 4) { paso++; return null }

        val cuenta = DatosCuenta(nombrePersona, correo, telefono, contrasena, confirmacion)
        val datos = DatosNegocio(
            nombre = nombreNegocio, oficio = oficio, categoria = categoria, descripcion = descripcion,
            direccion = direccion, whatsapp = if (whatsappIgual) "" else whatsapp,
            fotos = emptyList(), horarios = horarios(), colonia = colonia
        )
        return when (val r = registrar(cuenta, datos)) {
            is Resultado.Exito -> r.dato
            is Resultado.Error -> {
                errores = r.errores
                paso = r.errores.keys.minOfOrNull { pasoDe(it) } ?: 4
                null
            }
        }
    }

    fun horarios(): List<Horario> {
        if (veinticuatroHoras) return DiaSemana.entries.map { Horario(it, 0, 1440) }
        val semana = listOf(DiaSemana.LUNES, DiaSemana.MARTES, DiaSemana.MIERCOLES, DiaSemana.JUEVES, DiaSemana.VIERNES)
        return buildList {
            if (lunesAViernes) semana.forEach { add(Horario(it, abreSemana * 60, cierraSemana * 60)) }
            if (sabado) add(Horario(DiaSemana.SABADO, abreSabado * 60, cierraSabado * 60))
            if (domingo) add(Horario(DiaSemana.DOMINGO, abreSabado * 60, cierraSabado * 60))
        }
    }

    private fun erroresDelPaso(): Map<Campo, String> = buildMap {
        when (paso) {
            1 -> {
                validar.nombre(nombreNegocio, "el nombre del negocio")?.let { put(Campo.NOMBRE_NEGOCIO, it) }
                validar.nombre(oficio, "tu oficio")?.let { put(Campo.OFICIO, it) }
            }
            2 -> validar.textoRequerido(direccion, "la dirección", 150)?.let { put(Campo.DIRECCION, it) }
            4 -> {
                if (horarios().isEmpty()) put(Campo.HORARIOS, "Elige al menos un día de atención")
                if (!veinticuatroHoras && lunesAViernes && abreSemana >= cierraSemana) put(Campo.HORARIOS, "La hora de cierre debe ser después de la apertura")
                if (!veinticuatroHoras && (sabado || domingo) && abreSabado >= cierraSabado) put(Campo.HORARIOS, "La hora de cierre debe ser después de la apertura")
            }
        }
    }

    private fun pasoDe(campo: Campo): Int = when (campo) {
        Campo.NOMBRE_NEGOCIO, Campo.OFICIO -> 1
        Campo.DIRECCION -> 2
        Campo.DESCRIPCION -> 3
        else -> 4
    }

    companion object {
        val HORAS_APERTURA = listOf(7, 8, 9, 10, 11)
        val HORAS_CIERRE = listOf(13, 14, 15, 17, 18, 19, 20)
    }
}
