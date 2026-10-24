package com.example.proyectofinal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.proyectofinal.domain.model.Rol
import com.example.proyectofinal.domain.model.Usuario
import com.example.proyectofinal.ui.components.BotonPrincipal
import com.example.proyectofinal.ui.components.Etiqueta
import com.example.proyectofinal.ui.components.Icono
import com.example.proyectofinal.ui.components.formatearTelefono
import com.example.proyectofinal.ui.theme.Blanco
import com.example.proyectofinal.ui.theme.Neutro
import com.example.proyectofinal.ui.theme.Oliva
import com.example.proyectofinal.ui.theme.Terracota
import org.jetbrains.compose.resources.DrawableResource
import proyectofinal.shared.generated.resources.*

private const val TEXTO_ACERCA =
    "Oficios Jiquilpan es un directorio comunitario de negocios y oficios de Jiquilpan, Michoacán. " +
    "Los vecinos encuentran carpinteros, plomeros, electricistas y más, y los contactan directo por teléfono o WhatsApp. " +
    "La plataforma no cobra comisiones ni interviene en los pagos.\n\nVersión 1.0 · Proyecto escolar del TecNM campus Jiquilpan."

internal const val TEXTO_TERMINOS =
    "1. Publica solo información real de tu negocio, tus precios y tus horarios.\n" +
    "2. Las reseñas deben venir de un servicio que recibiste; sin insultos ni datos personales.\n" +
    "3. Los acuerdos de precio, anticipo y visita son entre cliente y negocio.\n" +
    "4. Podemos ocultar perfiles o reseñas que engañen o falten al respeto a la comunidad."

/** Ajustes (boceto 04 · "Ajustes"), compartido por clientes, negocios e invitados. */
@Composable
fun AjustesScreen(
    usuario: Usuario?,
    colonia: String?,
    onCambiarColonia: (() -> Unit)?,
    onVerSinConexion: () -> Unit,
    onRegistrarNegocio: () -> Unit,
    onIniciarSesion: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    var dialogo by remember { mutableStateOf<Pair<String, String>?>(null) }

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp)) {
        Text("Ajustes", style = MaterialTheme.typography.displaySmall, color = Neutro.t500)
        Spacer(Modifier.height(16.dp))
        Row(
            Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(Terracota.t50).padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(Modifier.size(56.dp).clip(CircleShape).background(Terracota.t100), contentAlignment = Alignment.Center) {
                if (usuario != null) Text(usuario.nombre.take(1).uppercase(), style = MaterialTheme.typography.headlineSmall, color = Terracota.t700)
                else Icono(Res.drawable.ic_usuario, null, Terracota.t600, Modifier.size(26.dp))
            }
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(usuario?.nombre ?: "Invitado", style = MaterialTheme.typography.titleLarge, color = Neutro.t500)
                Text(usuario?.correo ?: "Explorando sin cuenta", style = MaterialTheme.typography.bodySmall, color = Neutro.t400)
                if (usuario != null) {
                    Spacer(Modifier.height(6.dp))
                    Etiqueta(
                        if (usuario.rol == Rol.NEGOCIO) "Cuenta de negocio" else "Cuenta de cliente",
                        if (usuario.rol == Rol.NEGOCIO) Terracota.t100 else Oliva.t100,
                        if (usuario.rol == Rol.NEGOCIO) Terracota.t700 else Oliva.t700
                    )
                }
            }
        }

        if (usuario != null) {
            Grupo("Cuenta")
            Opcion(Res.drawable.ic_telefono, "Teléfono", formatearTelefono(usuario.telefono), null)
            Opcion(Res.drawable.ic_correo, "Correo", usuario.correo, null)
        }

        Grupo("Directorio local")
        if (onCambiarColonia != null) {
            Opcion(Res.drawable.ic_ubicacion, "Mi colonia", colonia ?: "Sin elegir · toca para elegir", onCambiarColonia)
        }
        Opcion(Res.drawable.ic_info, "Acerca de Oficios Jiquilpan", null) { dialogo = "Acerca de Oficios Jiquilpan" to TEXTO_ACERCA }
        Opcion(Res.drawable.ic_documento, "Términos y convivencia vecinal", null) { dialogo = "Términos y convivencia vecinal" to TEXTO_TERMINOS }

        Grupo("Estados de la app")
        Opcion(Res.drawable.ic_sin_conexion, "Ver pantalla sin conexión", "Muestra cómo se ve la app sin internet", onVerSinConexion)

        if (usuario?.rol != Rol.NEGOCIO) {
            Spacer(Modifier.height(20.dp))
            Column(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(Terracota.t500)
                    .clickable(onClick = onRegistrarNegocio).padding(16.dp)
            ) {
                Text("¿Ofreces un oficio o taller?", style = MaterialTheme.typography.titleMedium, color = Blanco)
                Spacer(Modifier.height(4.dp))
                Text(
                    "Registra tu carpintería, plomería o servicio y conecta con familias de Jiquilpan.",
                    style = MaterialTheme.typography.bodySmall, color = Terracota.t100
                )
                Spacer(Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Registrar mi negocio", style = MaterialTheme.typography.labelLarge, color = Blanco)
                    Spacer(Modifier.width(6.dp))
                    Icono(Res.drawable.ic_adelante, null, Blanco, Modifier.size(16.dp))
                }
            }
        }

        Spacer(Modifier.height(24.dp))
        if (usuario != null) {
            Row(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).clickable(onClick = onCerrarSesion).padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icono(Res.drawable.ic_salir, null, Terracota.t700, Modifier.size(20.dp))
                Spacer(Modifier.width(10.dp))
                Text("Cerrar sesión", style = MaterialTheme.typography.labelLarge, color = Terracota.t700)
            }
        } else {
            BotonPrincipal("Iniciar sesión", onIniciarSesion)
        }
        Spacer(Modifier.height(16.dp))
        Text(
            "Hecho con orgullo en Jiquilpan, Michoacán",
            style = MaterialTheme.typography.bodySmall, color = Neutro.t300,
            textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()
        )
    }

    dialogo?.let { (titulo, texto) ->
        AlertDialog(
            onDismissRequest = { dialogo = null },
            confirmButton = { TextButton(onClick = { dialogo = null }) { Text("Entendido", color = Terracota.t600) } },
            title = { Text(titulo, style = MaterialTheme.typography.titleLarge) },
            text = { Text(texto, style = MaterialTheme.typography.bodyMedium) },
            containerColor = Blanco
        )
    }
}

@Composable
private fun Grupo(titulo: String) {
    Spacer(Modifier.height(22.dp))
    Text(titulo, style = MaterialTheme.typography.titleSmall, color = Neutro.t400)
    Spacer(Modifier.height(6.dp))
}

@Composable
private fun Opcion(icono: DrawableResource, titulo: String, detalle: String?, onClick: (() -> Unit)?) {
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(Blanco)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(34.dp).clip(CircleShape).background(Terracota.t50), contentAlignment = Alignment.Center) {
            Icono(icono, null, Terracota.t600, Modifier.size(18.dp))
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(titulo, style = MaterialTheme.typography.titleSmall, color = Neutro.t500)
            if (detalle != null) Text(detalle, style = MaterialTheme.typography.bodySmall, color = Neutro.t400)
        }
        if (onClick != null) Icono(Res.drawable.ic_adelante, null, Neutro.t300, Modifier.size(16.dp))
    }
    Spacer(Modifier.height(6.dp))
}
