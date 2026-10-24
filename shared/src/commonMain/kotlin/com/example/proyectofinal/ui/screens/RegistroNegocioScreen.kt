package com.example.proyectofinal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.proyectofinal.domain.model.Campo
import com.example.proyectofinal.domain.model.Categoria
import com.example.proyectofinal.domain.model.Negocio
import com.example.proyectofinal.domain.model.Usuario
import com.example.proyectofinal.ui.components.Aviso
import com.example.proyectofinal.ui.components.BotonPrincipal
import com.example.proyectofinal.ui.components.CampoArea
import com.example.proyectofinal.ui.components.CampoTexto
import com.example.proyectofinal.ui.components.EncabezadoPantalla
import com.example.proyectofinal.ui.components.FilaSwitch
import com.example.proyectofinal.ui.components.IndicadorPasos
import com.example.proyectofinal.ui.components.OpcionPastilla
import com.example.proyectofinal.ui.components.TituloCampo
import com.example.proyectofinal.ui.components.formatearHora
import com.example.proyectofinal.ui.components.icono
import com.example.proyectofinal.ui.theme.Crema
import com.example.proyectofinal.ui.theme.Neutro
import com.example.proyectofinal.ui.theme.Terracota
import proyectofinal.shared.generated.resources.*

/** Registro de negocio en 4 pasos (boceto 02). */
@Composable
fun RegistroNegocioScreen(
    estado: RegistroNegocioEstado,
    colonias: List<String>,
    onSalir: () -> Unit,
    onRegistrado: (Pair<Usuario, Negocio>) -> Unit
) {
    val scroll = rememberScrollState()
    LaunchedEffect(estado.paso) { scroll.scrollTo(0) }
    val volver = { if (!estado.regresar()) onSalir() }

    Column(Modifier.fillMaxSize().background(Crema).safeDrawingPadding().imePadding()) {
        EncabezadoPantalla("Registra tu negocio", volver)
        Column(Modifier.padding(horizontal = 20.dp)) { IndicadorPasos(estado.paso, 4, estado.nombrePaso) }
        Column(Modifier.weight(1f).verticalScroll(scroll).padding(horizontal = 20.dp, vertical = 16.dp)) {
            when (estado.paso) {
                1 -> PasoDatos(estado)
                2 -> PasoUbicacion(estado, colonias)
                3 -> PasoDescripcion(estado)
                else -> PasoHorarioContacto(estado)
            }
        }
        Row(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp)) {
            OutlinedButton(onClick = volver, modifier = Modifier.height(52.dp), shape = MaterialTheme.shapes.small) {
                Text(if (estado.paso == 1) "Cancelar" else "Regresar", style = MaterialTheme.typography.labelLarge, color = Neutro.t500)
            }
            Spacer(Modifier.width(10.dp))
            BotonPrincipal(
                if (estado.paso == 4) "Publicar mi negocio" else "Continuar",
                onClick = { estado.continuar()?.let(onRegistrado) },
                modifier = Modifier.weight(1f),
                iconoFinal = Res.drawable.ic_adelante
            )
        }
    }
}

@Composable
private fun Encabezado(titulo: String, texto: String) {
    Text(titulo, style = MaterialTheme.typography.headlineSmall, color = Neutro.t500)
    Spacer(Modifier.height(6.dp))
    Text(texto, style = MaterialTheme.typography.bodyMedium, color = Neutro.t400)
    Spacer(Modifier.height(20.dp))
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PasoDatos(e: RegistroNegocioEstado) {
    Encabezado(
        "Cuéntanos sobre tu oficio o negocio",
        "Esta información ayudará a los vecinos de Jiquilpan a reconocerte y contactarte para sus encargos."
    )
    CampoTexto("Nombre del negocio o taller", e.nombreNegocio, { e.nombreNegocio = it },
        "Ej. Taller de Carpintería Don Chema", Res.drawable.ic_tienda, e.errores[Campo.NOMBRE_NEGOCIO])
    Text("Así aparecerá tu letrero en las búsquedas.", style = MaterialTheme.typography.bodySmall, color = Neutro.t300,
        modifier = Modifier.padding(top = 4.dp))
    Spacer(Modifier.height(16.dp))
    CampoTexto("Tu especialidad", e.oficio, { e.oficio = it },
        "Ej. Muebles finos de parota", Res.drawable.ic_herramienta, e.errores[Campo.OFICIO])
    Spacer(Modifier.height(20.dp))
    TituloCampo("Categoría principal")
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Categoria.entries.forEach { c -> OpcionPastilla(c.nombreVisible, e.categoria == c, c.icono()) { e.categoria = c } }
    }
    Spacer(Modifier.height(20.dp))
    Aviso(Res.drawable.ic_verificado, "Recomendaciones entre vecinos",
        "Tus datos se revisan con respeto para generar confianza directa con la comunidad.")
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PasoUbicacion(e: RegistroNegocioEstado, colonias: List<String>) {
    Encabezado(
        "¿Dónde ofreces tus servicios?",
        "Indica la dirección de tu taller o local, o la referencia desde donde atiendes a domicilio."
    )
    CampoTexto("Dirección o referencia", e.direccion, { e.direccion = it },
        "Calle Lic. Justo Sierra 142", Res.drawable.ic_ubicacion, e.errores[Campo.DIRECCION])
    Spacer(Modifier.height(20.dp))
    TituloCampo("Colonia o barrio", "Elige una o escríbela")
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        colonias.forEach { c -> OpcionPastilla(c, e.colonia == c) { e.colonia = if (e.colonia == c) "" else c } }
    }
    Spacer(Modifier.height(12.dp))
    CampoTexto("Otra colonia", if (e.colonia in colonias) "" else e.colonia, { e.colonia = it },
        "Ej. Col. Emiliano Zapata", Res.drawable.ic_ubicacion, null)
    Spacer(Modifier.height(20.dp))
    Aviso(Res.drawable.ic_mapa, "Así te encuentran",
        "Las tarjetas muestran tu colonia y el botón \"Cómo llegar\" abre Google Maps con tu dirección.")
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PasoDescripcion(e: RegistroNegocioEstado) {
    Encabezado(
        "Muestra tu trabajo y experiencia",
        "Cuenta qué haces, desde cuándo y qué te distingue. Los vecinos leen esto antes de llamarte."
    )
    CampoArea(
        "Descripción del negocio", e.descripcion, { e.descripcion = it },
        "Soy maestro carpintero con más de 15 años de oficio en Jiquilpan. Me especializo en…",
        maximo = 500, error = e.errores[Campo.DESCRIPCION]
    )
    Spacer(Modifier.height(12.dp))
    TituloCampo("Ideas para empezar")
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf("Trabajo a domicilio", "Presupuesto sin costo", "Garantía por escrito", "Materiales de la región").forEach { idea ->
            OpcionPastilla("+ $idea", false) {
                val sep = if (e.descripcion.isBlank() || e.descripcion.endsWith(" ")) "" else ". "
                if ((e.descripcion + sep + idea).length <= 500) e.descripcion = e.descripcion + sep + idea
            }
        }
    }
    Spacer(Modifier.height(20.dp))
    Aviso(Res.drawable.ic_info, "Fotos de tus trabajos",
        "Subir fotos llega en la siguiente versión. Mientras tanto tu perfil muestra el icono de tu categoría.", verde = false)
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PasoHorarioContacto(e: RegistroNegocioEstado) {
    Encabezado(
        "Disponibilidad y contacto directo",
        "Configura tu horario y cómo prefieres que los vecinos te escriban. Al final crea tu cuenta para entrar a tu panel."
    )
    TituloCampo("Horario de atención")
    FilaSwitch("Servicio 24 horas", "Para emergencias de plomería, electricidad o cerrajería", e.veinticuatroHoras) { e.veinticuatroHoras = it }
    if (!e.veinticuatroHoras) {
        Spacer(Modifier.height(10.dp))
        FilaSwitch("Lunes a viernes", "${formatearHora(e.abreSemana * 60)} – ${formatearHora(e.cierraSemana * 60)}", e.lunesAViernes) { e.lunesAViernes = it }
        if (e.lunesAViernes) SelectorHoras(e.abreSemana, e.cierraSemana, { e.abreSemana = it }, { e.cierraSemana = it })
        Spacer(Modifier.height(10.dp))
        FilaSwitch("Sábado", "${formatearHora(e.abreSabado * 60)} – ${formatearHora(e.cierraSabado * 60)}", e.sabado) { e.sabado = it }
        FilaSwitch("Domingo", if (e.domingo) "Mismo horario que el sábado" else "Cerrado para descanso", e.domingo) { e.domingo = it }
        if (e.sabado || e.domingo) SelectorHoras(e.abreSabado, e.cierraSabado, { e.abreSabado = it }, { e.cierraSabado = it })
    }
    e.errores[Campo.HORARIOS]?.let {
        Text(it, style = MaterialTheme.typography.bodySmall, color = Terracota.t700, modifier = Modifier.padding(top = 6.dp))
    }

    Spacer(Modifier.height(24.dp))
    TituloCampo("Contacto")
    CampoTexto("Teléfono", e.telefono, { e.telefono = it }, "353 123 4567", Res.drawable.ic_telefono,
        e.errores[Campo.TELEFONO], KeyboardType.Phone)
    Spacer(Modifier.height(10.dp))
    FilaSwitch("WhatsApp con el mismo número", "Es el canal preferido para cotizaciones", e.whatsappIgual) { e.whatsappIgual = it }
    if (!e.whatsappIgual) {
        Spacer(Modifier.height(10.dp))
        CampoTexto("WhatsApp", e.whatsapp, { e.whatsapp = it }, "353 765 4321", Res.drawable.ic_mensaje,
            e.errores[Campo.WHATSAPP], KeyboardType.Phone)
    }

    Spacer(Modifier.height(24.dp))
    TituloCampo("Tu cuenta para entrar")
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        CampoTexto("Tu nombre", e.nombrePersona, { e.nombrePersona = it }, "Ej. José Martínez", Res.drawable.ic_usuario, e.errores[Campo.NOMBRE])
        CampoTexto("Correo electrónico", e.correo, { e.correo = it }, "taller@ejemplo.com", Res.drawable.ic_correo,
            e.errores[Campo.CORREO], KeyboardType.Email)
        CampoTexto("Contraseña", e.contrasena, { e.contrasena = it }, "Mínimo 8 caracteres, con números", Res.drawable.ic_candado,
            e.errores[Campo.CONTRASENA], esContrasena = true)
        CampoTexto("Confirmar contraseña", e.confirmacion, { e.confirmacion = it }, "Repite tu contraseña", Res.drawable.ic_candado,
            e.errores[Campo.CONFIRMACION], esContrasena = true)
    }
    Spacer(Modifier.height(20.dp))
    Aviso(Res.drawable.ic_check, "0% comisiones",
        "No cobramos porcentaje por trabajo realizado. El pago de tus cotizaciones va directo entre tú y tu cliente.")
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SelectorHoras(abre: Int, cierra: Int, onAbre: (Int) -> Unit, onCierra: (Int) -> Unit) {
    Column(Modifier.fillMaxWidth().padding(top = 10.dp, start = 4.dp)) {
        Text("Abre", style = MaterialTheme.typography.labelMedium, color = Neutro.t400)
        Spacer(Modifier.height(6.dp))
        FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            RegistroNegocioEstado.HORAS_APERTURA.forEach { h -> OpcionPastilla(formatearHora(h * 60), abre == h) { onAbre(h) } }
        }
        Spacer(Modifier.height(10.dp))
        Text("Cierra", style = MaterialTheme.typography.labelMedium, color = Neutro.t400)
        Spacer(Modifier.height(6.dp))
        FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            RegistroNegocioEstado.HORAS_CIERRE.forEach { h -> OpcionPastilla(formatearHora(h * 60), cierra == h) { onCierra(h) } }
        }
    }
}
