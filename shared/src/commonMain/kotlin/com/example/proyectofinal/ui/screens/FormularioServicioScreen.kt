package com.example.proyectofinal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.proyectofinal.domain.model.Campo
import com.example.proyectofinal.domain.model.Resultado
import com.example.proyectofinal.domain.model.Servicio
import com.example.proyectofinal.domain.usecase.DatosServicio
import com.example.proyectofinal.domain.usecase.GestionarServiciosUseCase
import com.example.proyectofinal.ui.components.Aviso
import com.example.proyectofinal.ui.components.BotonPrincipal
import com.example.proyectofinal.ui.components.CampoArea
import com.example.proyectofinal.ui.components.CampoPrecio
import com.example.proyectofinal.ui.components.CampoTexto
import com.example.proyectofinal.ui.components.EncabezadoPantalla
import com.example.proyectofinal.ui.components.OpcionPastilla
import com.example.proyectofinal.ui.components.TituloCampo
import com.example.proyectofinal.ui.theme.Crema
import com.example.proyectofinal.ui.theme.Neutro
import com.example.proyectofinal.ui.theme.Terracota
import proyectofinal.shared.generated.resources.*

/** Estado del formulario: si [existente] no es null, se edita ese servicio. */
class FormularioServicioEstado(
    private val servicios: GestionarServiciosUseCase,
    private val idNegocio: String,
    private val idUsuario: String,
    val existente: Servicio?
) {
    var nombre by mutableStateOf(existente?.nombre ?: "")
    var descripcion by mutableStateOf(existente?.descripcion ?: "")
    var precio by mutableStateOf(existente?.precioAprox?.let { if (it % 1.0 == 0.0) it.toLong().toString() else it.toString() } ?: "")
    var esPrecioDesde by mutableStateOf(existente?.esPrecioDesde ?: true)
    var errores by mutableStateOf<Map<Campo, String>>(emptyMap())
        private set
    var mensaje by mutableStateOf<String?>(null)
        private set

    /** Devuelve el mensaje de éxito para mostrar, o null si hubo errores. */
    fun guardar(): String? {
        val datos = DatosServicio(nombre, descripcion, precio.toDoubleOrNull(), esPrecioDesde)
        val r = if (existente == null) servicios.agregar(idNegocio, idUsuario, datos)
        else servicios.editar(existente.id, idUsuario, datos)
        return when (r) {
            is Resultado.Exito -> if (existente == null) "Servicio publicado" else "Cambios guardados"
            is Resultado.Error -> { errores = r.errores; mensaje = if (r.errores.isEmpty()) r.mensaje else null; null }
        }
    }
}

/** Nuevo / editar servicio (boceto 04 · "Nuevo Servicio"). */
@Composable
fun FormularioServicioScreen(estado: FormularioServicioEstado, onVolver: () -> Unit, onGuardado: (String) -> Unit) {
    Column(Modifier.fillMaxSize().background(Crema).safeDrawingPadding().imePadding()) {
        EncabezadoPantalla(if (estado.existente == null) "Nuevo servicio" else "Editar servicio", onVolver)
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 8.dp)) {
            Aviso(Res.drawable.ic_info, "Precios claros, más clientes",
                "Los vecinos contactan más a los negocios que muestran un precio aproximado.", verde = false)
            Spacer(Modifier.height(20.dp))
            CampoTexto("Nombre del servicio", estado.nombre, { estado.nombre = it },
                "Ej. Restauración de comedores rústicos", Res.drawable.ic_herramienta, estado.errores[Campo.NOMBRE_SERVICIO])
            Spacer(Modifier.height(16.dp))
            CampoArea(
                "Descripción", estado.descripcion, { estado.descripcion = it },
                "Explica qué incluye el trabajo, materiales y tiempos estimados…",
                maximo = 300, error = estado.errores[Campo.DESCRIPCION],
                ayuda = "Menciona si llevas herramienta especializada o si el cliente compra los materiales."
            )
            Spacer(Modifier.height(16.dp))
            TituloCampo("Modalidad de cobro")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OpcionPastilla("Desde (precio mínimo)", estado.esPrecioDesde) { estado.esPrecioDesde = true }
                OpcionPastilla("Precio fijo", !estado.esPrecioDesde) { estado.esPrecioDesde = false }
            }
            Spacer(Modifier.height(16.dp))
            CampoPrecio("Precio aproximado", estado.precio, { estado.precio = it }, estado.errores[Campo.PRECIO], "350")
            Text(
                if (estado.esPrecioDesde) "Los clientes entienden que el costo final depende de medidas y material."
                else "El cliente pagará exactamente este precio.",
                style = MaterialTheme.typography.bodySmall, color = Neutro.t300, modifier = Modifier.padding(top = 4.dp)
            )
            estado.mensaje?.let {
                Spacer(Modifier.height(12.dp))
                Text(it, style = MaterialTheme.typography.bodyMedium, color = Terracota.t700)
            }
            Spacer(Modifier.height(16.dp))
        }
        Column(Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
            BotonPrincipal(
                if (estado.existente == null) "Publicar servicio" else "Guardar cambios",
                onClick = { estado.guardar()?.let(onGuardado) }
            )
        }
    }
}
