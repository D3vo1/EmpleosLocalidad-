package com.example.proyectofinal.domain.usecase

import com.example.proyectofinal.domain.model.Campo
import com.example.proyectofinal.domain.model.Resultado
import com.example.proyectofinal.domain.model.Rol
import com.example.proyectofinal.domain.model.Usuario
import com.example.proyectofinal.domain.model.generarId
import com.example.proyectofinal.domain.repository.UsuarioRepository
import com.example.proyectofinal.domain.usecase.ValidarFormularioUseCase.Companion.normalizarCorreo
import com.example.proyectofinal.domain.usecase.ValidarFormularioUseCase.Companion.soloDigitos

/** Datos de la pantalla Registro de cliente (también los usa el registro de negocio). */
data class DatosCuenta(
    val nombre: String,
    val correo: String,
    val telefono: String,
    val contrasena: String,
    val confirmacion: String
)

/** Valida los datos de cuenta y el correo único. Lo comparten ambos registros. */
internal fun validarCuenta(
    datos: DatosCuenta,
    validar: ValidarFormularioUseCase,
    usuarios: UsuarioRepository
): Map<Campo, String> = buildMap {
    validar.nombre(datos.nombre)?.let { put(Campo.NOMBRE, it) }
    validar.correo(datos.correo)?.let { put(Campo.CORREO, it) }
    validar.telefono(datos.telefono)?.let { put(Campo.TELEFONO, it) }
    validar.contrasena(datos.contrasena)?.let { put(Campo.CONTRASENA, it) }
    validar.confirmacion(datos.contrasena, datos.confirmacion)?.let { put(Campo.CONFIRMACION, it) }
    if (Campo.CORREO !in this && usuarios.buscarPorCorreo(normalizarCorreo(datos.correo)) != null) {
        put(Campo.CORREO, "Ya existe una cuenta con este correo")
    }
}

internal fun crearUsuario(datos: DatosCuenta, rol: Rol, generarId: () -> String) = Usuario(
    id = generarId(),
    nombre = datos.nombre.trim(),
    correo = normalizarCorreo(datos.correo),
    telefono = soloDigitos(datos.telefono),
    contrasena = datos.contrasena,
    rol = rol
)

class RegistrarClienteUseCase(
    private val usuarios: UsuarioRepository,
    private val validar: ValidarFormularioUseCase = ValidarFormularioUseCase(),
    private val nuevoId: () -> String = ::generarId
) {
    operator fun invoke(datos: DatosCuenta): Resultado<Usuario> {
        val errores = validarCuenta(datos, validar, usuarios)
        if (errores.isNotEmpty()) return Resultado.Error("Revisa los campos marcados", errores)

        val usuario = crearUsuario(datos, Rol.CLIENTE, nuevoId)
        usuarios.guardar(usuario)
        return Resultado.Exito(usuario)
    }
}
