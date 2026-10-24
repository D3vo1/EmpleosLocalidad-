package com.example.proyectofinal.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.proyectofinal.ui.theme.Blanco
import com.example.proyectofinal.ui.theme.Borde
import com.example.proyectofinal.ui.theme.Neutro
import com.example.proyectofinal.ui.theme.Oliva
import com.example.proyectofinal.ui.theme.Terracota
import org.jetbrains.compose.resources.DrawableResource
import proyectofinal.shared.generated.resources.*

/** Barra superior de formularios y pantallas internas: volver + título + acción opcional. */
@Composable
fun EncabezadoPantalla(titulo: String, onVolver: () -> Unit, accion: (@Composable () -> Unit)? = null) {
    Row(Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onVolver) { Icono(Res.drawable.ic_atras, "Volver", Neutro.t500, Modifier.size(22.dp)) }
        Text(titulo, style = MaterialTheme.typography.titleLarge, color = Neutro.t500, modifier = Modifier.weight(1f))
        accion?.invoke()
    }
}

/** Pasos del registro de negocio: barra de progreso + "Paso 2 de 4 · Ubicación". */
@Composable
fun IndicadorPasos(actual: Int, total: Int, nombre: String) {
    Column(Modifier.fillMaxWidth()) {
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            repeat(total) { i ->
                Box(
                    Modifier.weight(1f).height(6.dp).clip(CircleShape)
                        .background(if (i < actual) Terracota.t500 else Terracota.t100)
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        Text("Paso $actual de $total · $nombre", style = MaterialTheme.typography.labelMedium, color = Terracota.t600)
    }
}

/** Título de un grupo de campos, con ayuda opcional a la derecha. */
@Composable
fun TituloCampo(titulo: String, ayuda: String? = null) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom) {
        Text(titulo, style = MaterialTheme.typography.titleSmall, color = Neutro.t500, modifier = Modifier.weight(1f))
        if (ayuda != null) Text(ayuda, style = MaterialTheme.typography.bodySmall, color = Neutro.t300)
    }
    Spacer(Modifier.height(6.dp))
}

/** Área de texto de varias líneas con contador de caracteres y error. */
@Composable
fun CampoArea(
    etiqueta: String,
    valor: String,
    onCambio: (String) -> Unit,
    placeholder: String,
    maximo: Int,
    error: String? = null,
    ayuda: String? = null
) {
    Column(Modifier.fillMaxWidth()) {
        TituloCampo(etiqueta, "${valor.length}/$maximo")
        OutlinedTextField(
            value = valor,
            onValueChange = { if (it.length <= maximo) onCambio(it) },
            modifier = Modifier.fillMaxWidth().heightIn(min = 112.dp),
            placeholder = { Text(placeholder) },
            isError = error != null,
            shape = MaterialTheme.shapes.small,
            textStyle = MaterialTheme.typography.bodyLarge,
            colors = coloresCampo()
        )
        val nota = error ?: ayuda
        if (nota != null) {
            Spacer(Modifier.height(4.dp))
            Text(nota, style = MaterialTheme.typography.bodySmall, color = if (error != null) Terracota.t700 else Neutro.t300)
        }
    }
}

/** Campo numérico de precio con prefijo "$" y sufijo "MXN". */
@Composable
fun CampoPrecio(etiqueta: String, valor: String, onCambio: (String) -> Unit, error: String?, placeholder: String = "0") {
    Column(Modifier.fillMaxWidth()) {
        TituloCampo(etiqueta)
        OutlinedTextField(
            value = valor,
            onValueChange = { nuevo -> if (nuevo.all { it.isDigit() || it == '.' } && nuevo.count { it == '.' } <= 1) onCambio(nuevo) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = error != null,
            placeholder = { Text(placeholder) },
            prefix = { Text("$ ", color = Neutro.t400) },
            suffix = { Text("MXN", color = Neutro.t400) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            shape = MaterialTheme.shapes.small,
            textStyle = MaterialTheme.typography.bodyLarge,
            colors = coloresCampo()
        )
        if (error != null) {
            Spacer(Modifier.height(4.dp))
            Text(error, style = MaterialTheme.typography.bodySmall, color = Terracota.t700)
        }
    }
}

/** Pastilla seleccionable (categorías, colonias, horarios rápidos). */
@Composable
fun OpcionPastilla(texto: String, elegida: Boolean, icono: DrawableResource? = null, onClick: () -> Unit) {
    Row(
        Modifier.clip(CircleShape)
            .background(if (elegida) Terracota.t50 else Blanco)
            .border(1.dp, if (elegida) Terracota.t500 else Borde, CircleShape)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val ic = if (elegida) Res.drawable.ic_check else icono
        if (ic != null) {
            Icono(ic, null, if (elegida) Terracota.t600 else Neutro.t400, Modifier.size(15.dp))
            Spacer(Modifier.width(6.dp))
        }
        Text(texto, style = MaterialTheme.typography.labelMedium, color = if (elegida) Terracota.t700 else Neutro.t500)
    }
}

/** Fila con texto y switch ("Precio desde", "Sueldo a tratar", "Activo"). */
@Composable
fun FilaSwitch(titulo: String, descripcion: String?, activo: Boolean, onCambio: (Boolean) -> Unit) {
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(Blanco)
            .border(1.dp, Borde, RoundedCornerShape(14.dp)).clickable { onCambio(!activo) }
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(titulo, style = MaterialTheme.typography.titleSmall, color = Neutro.t500)
            if (descripcion != null) Text(descripcion, style = MaterialTheme.typography.bodySmall, color = Neutro.t400)
        }
        Spacer(Modifier.width(10.dp))
        Switch(checked = activo, onCheckedChange = onCambio, colors = coloresSwitch())
    }
}

@Composable
fun coloresSwitch() = SwitchDefaults.colors(
    checkedThumbColor = Blanco,
    checkedTrackColor = Oliva.t500,
    uncheckedThumbColor = Neutro.t200,
    uncheckedTrackColor = Neutro.t50,
    uncheckedBorderColor = Neutro.t100
)

/** Aviso de color suave con icono (consejos, confianza, comisiones). */
@Composable
fun Aviso(icono: DrawableResource, titulo: String, texto: String, verde: Boolean = true) {
    val fondo = if (verde) Oliva.t50 else Terracota.t50
    val tinta = if (verde) Oliva.t700 else Terracota.t700
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(fondo).padding(14.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icono(icono, null, tinta, Modifier.size(20.dp))
        Spacer(Modifier.width(10.dp))
        Column {
            Text(titulo, style = MaterialTheme.typography.titleSmall, color = tinta)
            Text(texto, style = MaterialTheme.typography.bodySmall, color = tinta)
        }
    }
}

/** Pestañas de la barra inferior del dueño de negocio (boceto 04). */
enum class PestanaNegocio(val titulo: String, val icono: DrawableResource) {
    PANEL("Panel", Res.drawable.ic_panel),
    SERVICIOS("Servicios", Res.drawable.ic_lista),
    VACANTES("Vacantes", Res.drawable.ic_maletin),
    AJUSTES("Ajustes", Res.drawable.ic_ajustes)
}

@Composable
fun BarraInferiorNegocio(actual: PestanaNegocio, onSeleccion: (PestanaNegocio) -> Unit) {
    NavigationBar(containerColor = Blanco, tonalElevation = 0.dp) {
        PestanaNegocio.entries.forEach { p ->
            val activa = p == actual
            NavigationBarItem(
                selected = activa,
                onClick = { onSeleccion(p) },
                icon = { Icono(p.icono, null, if (activa) Terracota.t500 else Neutro.t300, Modifier.size(22.dp)) },
                label = { Text(p.titulo, style = MaterialTheme.typography.labelSmall) },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Terracota.t50,
                    selectedTextColor = Terracota.t500,
                    unselectedTextColor = Neutro.t300
                )
            )
        }
    }
}
