package com.example.proyectofinal.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.proyectofinal.domain.model.Rol
import com.example.proyectofinal.ui.components.Etiqueta
import com.example.proyectofinal.ui.components.Icono
import com.example.proyectofinal.ui.theme.Blanco
import com.example.proyectofinal.ui.theme.Borde
import com.example.proyectofinal.ui.theme.Crema
import com.example.proyectofinal.ui.theme.Neutro
import com.example.proyectofinal.ui.theme.Oliva
import com.example.proyectofinal.ui.theme.Terracota
import org.jetbrains.compose.resources.DrawableResource
import proyectofinal.shared.generated.resources.*

/** Selector de rol (boceto 01): cliente, negocio o explorar como invitado. */
@Composable
fun SelectorRolScreen(onElegirRol: (Rol) -> Unit, onInvitado: () -> Unit) {
    Column(
        Modifier.fillMaxSize().background(Crema).safeDrawingPadding()
            .verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(40.dp).clip(RoundedCornerShape(12.dp)).background(Terracota.t500),
                contentAlignment = Alignment.Center
            ) { Icono(Res.drawable.ic_herramienta, null, Blanco, Modifier.size(22.dp)) }
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text("Oficios Jiquilpan", style = MaterialTheme.typography.titleMedium, color = Neutro.t500)
                Text("Jiquilpan, Mich.", style = MaterialTheme.typography.bodySmall, color = Neutro.t400)
            }
            Etiqueta("Comunidad activa", Oliva.t100, Oliva.t700, Res.drawable.ic_check)
        }

        Spacer(Modifier.height(28.dp))
        Etiqueta("Bienvenida vecinal", Terracota.t100, Terracota.t700)
        Spacer(Modifier.height(10.dp))
        Text("¿Cómo deseas participar hoy?", style = MaterialTheme.typography.displaySmall, color = Neutro.t500)
        Spacer(Modifier.height(8.dp))
        Text(
            "Elige tu perfil para personalizar tu experiencia y conectar con tu barrio.",
            style = MaterialTheme.typography.bodyLarge, color = Neutro.t400
        )

        Spacer(Modifier.height(24.dp))
        TarjetaRol(
            publico = "Para vecinos y familias",
            colorPublico = Oliva.t100, tintaPublico = Oliva.t700,
            icono = Res.drawable.ic_buscar,
            titulo = "Busco servicios",
            descripcion = "Encuentra plomeros, electricistas, carpinteros y talleres de confianza recomendados por gente de Jiquilpan.",
            ventajas = listOf("Reseñas reales", "Trato directo por WhatsApp", "Por colonias"),
            accion = "Continuar como cliente",
            onClick = { onElegirRol(Rol.CLIENTE) }
        )
        Spacer(Modifier.height(14.dp))
        TarjetaRol(
            publico = "Para artesanos y oficios",
            colorPublico = Terracota.t100, tintaPublico = Terracota.t700,
            icono = Res.drawable.ic_herramienta,
            titulo = "Tengo un negocio u oficio",
            descripcion = "Publica tus servicios, muestra fotos de tu trabajo y haz crecer tu clientela sin intermediarios.",
            ventajas = listOf("0% comisiones", "Perfil verificado", "Galería de fotos"),
            accion = "Continuar como negocio",
            onClick = { onElegirRol(Rol.NEGOCIO) }
        )

        Spacer(Modifier.height(14.dp))
        Row(
            Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(Terracota.t50).padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(Modifier.size(32.dp).clip(CircleShape).background(Terracota.t100), contentAlignment = Alignment.Center) {
                Icono(Res.drawable.ic_ubicacion, null, Terracota.t600, Modifier.size(18.dp))
            }
            Spacer(Modifier.width(10.dp))
            Column {
                Text("Comercio 100% jiquilpense", style = MaterialTheme.typography.titleSmall, color = Neutro.t500)
                Text("Fortalecemos la economía de nuestras colonias y barrios.", style = MaterialTheme.typography.bodySmall, color = Neutro.t400)
            }
        }

        Spacer(Modifier.height(20.dp))
        Row(
            Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).clickable(onClick = onInvitado).padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icono(Res.drawable.ic_explorar, null, Terracota.t600, Modifier.size(18.dp))
            Spacer(Modifier.width(6.dp))
            Text("Explorar como invitado", style = MaterialTheme.typography.labelLarge, color = Terracota.t600)
        }
        Text(
            "Descubre oficios y catálogos locales sin registrarte aún.",
            style = MaterialTheme.typography.bodySmall, color = Neutro.t300,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TarjetaRol(
    publico: String,
    colorPublico: Color,
    tintaPublico: Color,
    icono: DrawableResource,
    titulo: String,
    descripcion: String,
    ventajas: List<String>,
    accion: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        color = Blanco,
        border = BorderStroke(1.dp, Borde),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Etiqueta(publico, colorPublico, tintaPublico, modifier = Modifier.weight(1f, fill = false))
                Spacer(Modifier.weight(1f))
                Box(Modifier.size(40.dp).clip(RoundedCornerShape(12.dp)).background(colorPublico), contentAlignment = Alignment.Center) {
                    Icono(icono, null, tintaPublico, Modifier.size(20.dp))
                }
            }
            Spacer(Modifier.height(10.dp))
            Text(titulo, style = MaterialTheme.typography.headlineSmall, color = Neutro.t500)
            Spacer(Modifier.height(6.dp))
            Text(descripcion, style = MaterialTheme.typography.bodyMedium, color = Neutro.t400)
            Spacer(Modifier.height(10.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                ventajas.forEach { Etiqueta(it, Crema, Neutro.t400, Res.drawable.ic_check) }
            }
            Spacer(Modifier.height(14.dp))
            Row(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(Neutro.t50).padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(accion, style = MaterialTheme.typography.labelLarge, color = Neutro.t500, modifier = Modifier.weight(1f))
                Icono(Res.drawable.ic_adelante, null, Neutro.t500, Modifier.size(18.dp))
            }
        }
    }
}
