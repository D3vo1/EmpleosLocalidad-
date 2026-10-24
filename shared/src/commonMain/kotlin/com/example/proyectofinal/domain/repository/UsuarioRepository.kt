package com.example.proyectofinal.domain.repository

import com.example.proyectofinal.domain.model.Usuario

/** Contrato de cuentas (Persona B). La implementación en memoria va en la capa data. */
interface UsuarioRepository {
    /** [correo] ya llega normalizado (minúsculas, sin espacios). */
    fun buscarPorCorreo(correo: String): Usuario?
    fun buscarPorId(id: String): Usuario?
    /** Inserta o reemplaza por id. */
    fun guardar(usuario: Usuario)
}
