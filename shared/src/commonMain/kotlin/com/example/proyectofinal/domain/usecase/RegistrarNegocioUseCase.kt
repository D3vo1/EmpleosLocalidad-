package com.example.proyectofinal.domain.usecase

import com.example.proyectofinal.domain.model.Campo
import com.example.proyectofinal.domain.model.Categoria
import com.example.proyectofinal.domain.model.Horario
import com.example.proyectofinal.domain.model.Negocio
import com.example.proyectofinal.domain.model.Resultado
import com.example.proyectofinal.domain.model.Rol
import com.example.proyectofinal.domain.model.Usuario
import com.example.proyectofinal.domain.model.generarId
import com.example.proyectofinal.domain.repository.GestionNegocioRepository
import com.example.proyectofinal.domain.repository.UsuarioRepository
import com.example.proyectofinal.domain.usecase.ValidarFormularioUseCase.Companion.soloDigitos

/** Los 4 pasos del registro de negocio, juntos. Si [whatsapp] está vacío se usa el teléfono. */
data class DatosNegocio(
    val nombre: String,
    val oficio: String,
    val categoria: Categoria,
    val descripcion: String,
    val direccion: String,
    val whatsapp: String,
    val fotos: List<String>,
    val horarios: List<Horario>,
    val colonia: String = ""
)

/**
 * Crea la cuenta del dueño (rol NEGOCIO) y su negocio en un solo paso.
 * Devuelve ambos para que la UI entre directo al Panel.
 */
class RegistrarNegocioUseCase(
    private val usuarios: UsuarioRepository,
    private val negocios: GestionNegocioRepository,
    private val validar: ValidarFormularioUseCase = ValidarFormularioUseCase(),
    private val nuevoId: () -> String = ::generarId
) {
    operator fun invoke(cuenta: DatosCuenta, datos: DatosNegocio): Resultado<Pair<Usuario, Negocio>> {
        val errores = validarCuenta(cuenta, validar, usuarios) + validarNegocio(datos)
        if (errores.isNotEmpty()) return Resultado.Error("Revisa los campos marcados", errores)

        val dueno = crearUsuario(cuenta, Rol.NEGOCIO, nuevoId)
        val negocio = Negocio(
            id = nuevoId(),
            nombre = datos.nombre.trim(),
            oficio = datos.oficio.trim(),
            categoria = datos.categoria,
            descripcion = datos.descripcion.trim(),
            direccion = datos.direccion.trim(),
            telefono = dueno.telefono,
            whatsapp = soloDigitos(datos.whatsapp).ifEmpty { dueno.telefono },
            fotos = datos.fotos.take(MAX_FOTOS),
            horarios = datos.horarios,
            idDueno = dueno.id,
            colonia = datos.colonia.trim()
        )
        usuarios.guardar(dueno)
        negocios.guardarNegocio(negocio)
        return Resultado.Exito(dueno to negocio)
    }

    private fun validarNegocio(d: DatosNegocio): Map<Campo, String> = buildMap {
        validar.nombre(d.nombre, "el nombre del negocio")?.let { put(Campo.NOMBRE_NEGOCIO, it) }
        validar.nombre(d.oficio, "tu oficio")?.let { put(Campo.OFICIO, it) }
        validar.textoRequerido(d.direccion, "la dirección", 150)?.let { put(Campo.DIRECCION, it) }
        if (d.whatsapp.isNotBlank()) validar.telefono(d.whatsapp)?.let { put(Campo.WHATSAPP, it) }
        if (d.descripcion.length > 500) put(Campo.DESCRIPCION, "Máximo 500 caracteres")
        when {
            d.horarios.isEmpty() -> put(Campo.HORARIOS, "Agrega al menos un día de atención")
            d.horarios.any { it.apertura == it.cierre } -> put(Campo.HORARIOS, "La apertura y el cierre no pueden ser iguales")
            d.horarios.groupBy { it.dia }.any { it.value.size > 1 } -> put(Campo.HORARIOS, "Hay un día repetido")
        }
    }

    companion object {
        const val MAX_FOTOS = 6
    }
}
