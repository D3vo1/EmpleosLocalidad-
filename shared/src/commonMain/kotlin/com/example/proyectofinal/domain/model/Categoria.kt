package com.example.proyectofinal.domain.model

/**
 * Categorías de oficios disponibles en la app.
 * [nombreVisible] es el texto que se muestra en chips y filtros.
 */
enum class Categoria(val nombreVisible: String) {
    PLOMERIA("Plomería"),
    ELECTRICIDAD("Electricidad"),
    CLASES("Clases"),
    FOTOGRAFIA("Fotografía"),
    CARPINTERIA("Carpintería"),
    MECANICA("Mecánica"),
    BELLEZA("Belleza"),
    ALBANILERIA("Albañilería"),
    HERRERIA("Herrería"),
    JARDINERIA("Jardinería")
}
