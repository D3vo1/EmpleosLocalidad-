package com.example.proyectofinal.domain.model

/** Tipo de cuenta. Decide qué barra de navegación ve el usuario. */
enum class Rol(val nombreVisible: String) {
    CLIENTE("Cliente"),
    NEGOCIO("Negocio")
}
