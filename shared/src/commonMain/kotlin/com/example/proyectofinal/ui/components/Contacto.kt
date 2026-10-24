package com.example.proyectofinal.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import com.example.proyectofinal.domain.model.Negocio

/**
 * Abre el marcador, WhatsApp o Google Maps con enlaces normales.
 * LocalUriHandler es parte de Compose y funciona igual en Android e iOS.
 * Cada función devuelve null si se abrió, o un mensaje para mostrar si no se pudo.
 */
class Contacto(private val uri: UriHandler) {

    fun llamar(n: Negocio): String? = abrir("tel:${n.telefono}", "No se pudo abrir el teléfono")

    fun whatsapp(n: Negocio): String? {
        val texto = codificarUrl("Hola, vi ${n.nombre} en Oficios Jiquilpan y me interesa un servicio.")
        return abrir("https://wa.me/52${n.whatsapp}?text=$texto", "No se pudo abrir WhatsApp")
    }

    fun comoLlegar(n: Negocio): String? {
        val destino = codificarUrl("${n.direccion}, Jiquilpan, Michoacán")
        return abrir("https://www.google.com/maps/search/?api=1&query=$destino", "No se pudo abrir el mapa")
    }

    private fun abrir(url: String, error: String): String? =
        try { uri.openUri(url); null } catch (e: Exception) { error }
}

@Composable
fun rememberContacto(): Contacto {
    val uri = LocalUriHandler.current
    return remember(uri) { Contacto(uri) }
}
