package com.example.proyectofinal.domain.usecase

import com.example.proyectofinal.domain.model.Campo
import com.example.proyectofinal.domain.model.Resultado
import com.example.proyectofinal.domain.model.Rol
import com.example.proyectofinal.domain.model.Usuario
import com.example.proyectofinal.domain.repository.UsuarioRepository
import com.example.proyectofinal.domain.usecase.ValidarFormularioUseCase.Companion.normalizarCorreo

/**
 * Login. [rol] es el que eligió el usuario en el selector de rol.
 * Por seguridad, correo inexistente y contraseña incorrecta dan el mismo mensaje.
 */
class IniciarSesionUseCase(
    private val usuarios: UsuarioRepository,
    private val validar: ValidarFormularioUseCase = ValidarFormularioUseCase()
) {
    operator fun invoke(correo: String, contrasena: String, rol: Rol): Resultado<Usuario> {
        val errores = buildMap {
            validar.correo(correo)?.let { put(Campo.CORREO, it) }
            if (contrasena.isEmpty()) put(Campo.CONTRASENA, "Escribe tu contraseña")
        }
        if (errores.isNotEmpty()) return Resultado.Error("Revisa los campos marcados", errores)

        val usuario = usuarios.buscarPorCorreo(normalizarCorreo(correo))
        if (usuario == null || usuario.contrasena != contrasena) {
            return Resultado.Error("Correo o contraseña incorrectos")
        }
        if (usuario.rol != rol) {
            val otro = if (usuario.rol == Rol.NEGOCIO) "\"Tengo un negocio\"" else "\"Busco servicios\""
            return Resultado.Error("Esta cuenta es de ${usuario.rol.nombreVisible.lowercase()}. Entra por $otro")
        }
        return Resultado.Exito(usuario)
    }
}
