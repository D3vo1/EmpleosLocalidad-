package com.example.proyectofinal.ui.components

import kotlin.test.Test
import kotlin.test.assertEquals

class FormatoTest {
    @Test fun precios() {
        assertEquals("$80", formatearPrecio(80.0))
        assertEquals("$1,800", formatearPrecio(1800.0))
        assertEquals("$35,000", formatearPrecio(35000.0))
        assertEquals("$1,250,000", formatearPrecio(1_250_000.0))
        assertEquals("$99.50", formatearPrecio(99.5))
    }

    @Test fun horas() {
        assertEquals("12:00 AM", formatearHora(0))
        assertEquals("9:30 AM", formatearHora(570))
        assertEquals("12:00 PM", formatearHora(720))
        assertEquals("6:00 PM", formatearHora(1080))
        assertEquals("12:00 AM", formatearHora(1440))
    }

    @Test fun urlConAcentosYEspacios() {
        assertEquals("Jiquilpan%2C+Michoac%C3%A1n", codificarUrl("Jiquilpan, Michoacán"))
        assertEquals("Calle+5+de+Mayo", codificarUrl("Calle 5 de Mayo"))
    }
}
