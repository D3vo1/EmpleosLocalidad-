package com.example.proyectofinal.domain.model

/**
 * Cuenta de la app (cliente o dueño de negocio).
 * [correo] se guarda siempre en minúsculas y sin espacios.
 * [contrasena] se guarda en texto plano porque los datos viven en memoria
 * para la demo; con un backend real se guardaría solo un hash.
 */
data class Usuario(
    val id: String,
    val nombre: String,
    val correo: String,
    val telefono: String,
    val contrasena: String,
    val rol: Rol
)
