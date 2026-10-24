package com.example.proyectofinal.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.proyectofinal.domain.model.Categoria
import com.example.proyectofinal.ui.theme.Ambar
import com.example.proyectofinal.ui.theme.Blanco
import com.example.proyectofinal.ui.theme.Borde
import com.example.proyectofinal.ui.theme.Neutro
import com.example.proyectofinal.ui.theme.Oliva
import com.example.proyectofinal.ui.theme.Terracota
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import proyectofinal.shared.generated.resources.*

/** Icono de los recursos del proyecto, con el color que se le pida. */
@Composable
fun Icono(recurso: DrawableResource, descripcion: String?, tinte: Color, modifier: Modifier = Modifier.size(20.dp)) {
    Icon(painterResource(recurso), contentDescription = descripcion, tint = tinte, modifier = modifier)
}

/** Botón principal terracota de ancho completo ("Iniciar sesión", "Ver resultados"). */
@Composable
fun BotonPrincipal(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    habilitado: Boolean = true,
    iconoFinal: DrawableResource? = null
) {
    Button(
        onClick = onClick,
        enabled = habilitado,
        modifier = modifier.fillMaxWidth().height(52.dp),
        shape = MaterialTheme.shapes.small,
        colors = ButtonDefaults.buttonColors(containerColor = Terracota.t500, contentColor = Blanco)
    ) {
        Text(texto, style = MaterialTheme.typography.labelLarge)
        if (iconoFinal != null) {
            Spacer(Modifier.width(8.dp))
            Icono(iconoFinal, null, Blanco, Modifier.size(18.dp))
        }
    }
}

/** Botón pequeño de las tarjetas: "Llamar" (terracota) o "Mensaje" (verde claro). */
@Composable
fun BotonContacto(texto: String, icono: DrawableResource, principal: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val fondo = if (principal) Terracota.t500 else Oliva.t100
    val tinta = if (principal) Blanco else Oliva.t700
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(fondo)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icono(icono, null, tinta, Modifier.size(14.dp))
        Spacer(Modifier.width(5.dp))
        Text(texto, style = MaterialTheme.typography.labelMedium, color = tinta)
    }
}

/** Etiqueta redondeada pequeña: "Abierto ahora", "Verificado", "3 aplicados". */
@Composable
fun Etiqueta(texto: String, fondo: Color, tinta: Color, icono: DrawableResource? = null, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.clip(CircleShape).background(fondo).padding(horizontal = 9.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icono != null) {
            Icono(icono, null, tinta, Modifier.size(12.dp))
            Spacer(Modifier.width(4.dp))
        }
        Text(texto, style = MaterialTheme.typography.labelSmall, color = tinta)
    }
}

/** Chip de categoría de la fila horizontal de Explorar. */
@Composable
fun ChipCategoria(texto: String, icono: DrawableResource?, seleccionado: Boolean, onClick: () -> Unit) {
    val fondo = if (seleccionado) Terracota.t500 else Terracota.t50
    val tinta = if (seleccionado) Blanco else Terracota.t700
    Row(
        modifier = Modifier
            .clip(CircleShape)
            .background(fondo)
            .border(1.dp, if (seleccionado) Terracota.t500 else Terracota.t100, CircleShape)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icono != null) {
            Icono(icono, null, tinta, Modifier.size(15.dp))
            Spacer(Modifier.width(6.dp))
        }
        Text(texto, style = MaterialTheme.typography.labelMedium, color = tinta)
    }
}

/** ★ 4.9 (48 reseñas). Sin reseñas muestra "Nuevo". */
@Composable
fun Estrellas(promedio: Double, total: Int, conTexto: Boolean = true) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icono(Res.drawable.ic_estrella, null, Ambar.t500, Modifier.size(15.dp))
        Spacer(Modifier.width(3.dp))
        if (total == 0) {
            Text("Nuevo", style = MaterialTheme.typography.labelMedium, color = Neutro.t400)
        } else {
            Text(promedio.toString(), style = MaterialTheme.typography.titleSmall, color = Neutro.t500)
            if (conTexto) {
                Spacer(Modifier.width(4.dp))
                Text("($total ${if (total == 1) "reseña" else "reseñas"})", style = MaterialTheme.typography.bodySmall, color = Neutro.t300)
            }
        }
    }
}

/**
 * Lugar de la foto del negocio. Mientras no haya fotos reales en composeResources,
 * muestra el icono de su categoría sobre el tono claro de la paleta.
 */
@Composable
fun FotoNegocio(categoria: Categoria, modifier: Modifier, tamanoIcono: Int = 34) {
    Box(
        modifier = modifier.background(Terracota.t100),
        contentAlignment = Alignment.Center
    ) {
        Icono(categoria.icono(), categoria.nombreVisible, Terracota.t400, Modifier.size(tamanoIcono.dp))
    }
}

/** Barra de búsqueda con botón de filtros y contador de filtros activos. */
@Composable
fun BarraBusqueda(valor: String, onCambio: (String) -> Unit, filtrosActivos: Int, onFiltros: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = valor,
            onValueChange = onCambio,
            modifier = Modifier.weight(1f),
            singleLine = true,
            placeholder = { Text("Busca carpintero, fontanero, electricista…", maxLines = 1, overflow = TextOverflow.Ellipsis) },
            leadingIcon = { Icono(Res.drawable.ic_buscar, null, Neutro.t300) },
            trailingIcon = if (valor.isNotEmpty()) {
                { IconButton(onClick = { onCambio("") }) { Icono(Res.drawable.ic_cerrar, "Borrar búsqueda", Neutro.t300, Modifier.size(18.dp)) } }
            } else null,
            shape = MaterialTheme.shapes.small,
            textStyle = MaterialTheme.typography.bodyMedium,
            colors = coloresCampo()
        )
        Spacer(Modifier.width(10.dp))
        Box {
            Surface(
                onClick = onFiltros,
                shape = MaterialTheme.shapes.small,
                color = if (filtrosActivos > 0) Terracota.t500 else Blanco,
                border = BorderStroke(1.dp, if (filtrosActivos > 0) Terracota.t500 else Borde),
                modifier = Modifier.size(52.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icono(Res.drawable.ic_filtros, "Filtros", if (filtrosActivos > 0) Blanco else Neutro.t500, Modifier.size(22.dp))
                }
            }
            if (filtrosActivos > 0) {
                Box(
                    modifier = Modifier.align(Alignment.TopEnd).padding(top = 4.dp, end = 4.dp).size(16.dp)
                        .clip(CircleShape).background(Ambar.t500),
                    contentAlignment = Alignment.Center
                ) {
                    Text(filtrosActivos.toString(), style = MaterialTheme.typography.labelSmall, color = Neutro.t500)
                }
            }
        }
    }
}

@Composable
fun coloresCampo() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Terracota.t500,
    unfocusedBorderColor = Borde,
    focusedContainerColor = Blanco,
    unfocusedContainerColor = Blanco,
    cursorColor = Terracota.t500,
    errorBorderColor = Terracota.t700
)

/** Campo de formulario con etiqueta arriba y error debajo, como en el boceto. */
@Composable
fun CampoTexto(
    etiqueta: String,
    valor: String,
    onCambio: (String) -> Unit,
    placeholder: String,
    icono: DrawableResource,
    error: String?,
    tipoTeclado: KeyboardType = KeyboardType.Text,
    esContrasena: Boolean = false
) {
    var visible by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxWidth()) {
        Text(etiqueta, style = MaterialTheme.typography.titleSmall, color = Neutro.t500)
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value = valor,
            onValueChange = onCambio,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = error != null,
            placeholder = { Text(placeholder) },
            leadingIcon = { Icono(icono, null, Neutro.t300) },
            trailingIcon = if (esContrasena) {
                {
                    IconButton(onClick = { visible = !visible }) {
                        Icono(Res.drawable.ic_ojo, if (visible) "Ocultar contraseña" else "Mostrar contraseña",
                            if (visible) Terracota.t500 else Neutro.t300)
                    }
                }
            } else null,
            visualTransformation = if (esContrasena && !visible) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = if (esContrasena) KeyboardType.Password else tipoTeclado),
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

/** Pestañas de la barra inferior del cliente. */
enum class PestanaCliente(val titulo: String, val icono: DrawableResource) {
    EXPLORAR("Explorar", Res.drawable.ic_explorar),
    MAPA("Mapa", Res.drawable.ic_mapa),
    FAVORITOS("Favoritos", Res.drawable.ic_corazon),
    AJUSTES("Ajustes", Res.drawable.ic_ajustes)
}

@Composable
fun BarraInferior(actual: PestanaCliente, onSeleccion: (PestanaCliente) -> Unit) {
    NavigationBar(containerColor = Blanco, tonalElevation = 0.dp) {
        PestanaCliente.entries.forEach { p ->
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

/** Estado vacío: dice qué pasó y qué hacer, con una acción opcional. */
@Composable
fun EstadoVacio(
    icono: DrawableResource,
    titulo: String,
    mensaje: String,
    accion: String? = null,
    onAccion: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(horizontal = 32.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            Modifier.size(72.dp).clip(CircleShape).background(Terracota.t50),
            contentAlignment = Alignment.Center
        ) { Icono(icono, null, Terracota.t400, Modifier.size(32.dp)) }
        Spacer(Modifier.height(16.dp))
        Text(titulo, style = MaterialTheme.typography.titleLarge, color = Neutro.t500, textAlign = TextAlign.Center)
        Spacer(Modifier.height(6.dp))
        Text(mensaje, style = MaterialTheme.typography.bodyMedium, color = Neutro.t400, textAlign = TextAlign.Center)
        if (accion != null) {
            Spacer(Modifier.height(20.dp))
            Surface(
                onClick = onAccion,
                shape = MaterialTheme.shapes.small,
                color = Terracota.t50,
                border = BorderStroke(1.dp, Terracota.t200)
            ) {
                Text(accion, style = MaterialTheme.typography.labelLarge, color = Terracota.t700,
                    modifier = Modifier.padding(PaddingValues(horizontal = 18.dp, vertical = 10.dp)))
            }
        }
    }
}

/** Separador vertical pequeño para filas de datos. */
@Composable
fun Punto() {
    Text(" · ", style = MaterialTheme.typography.bodySmall, color = Neutro.t200, fontWeight = FontWeight.Bold)
}

/** Fila de espaciado reutilizable para listas con Arrangement. */
val Espaciado8 = Arrangement.spacedBy(8.dp)
