package com.example.proyectofinal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import com.example.proyectofinal.domain.model.Negocio
import com.example.proyectofinal.domain.model.Resultado
import com.example.proyectofinal.domain.usecase.DatosVacante
import com.example.proyectofinal.domain.usecase.PublicarVacanteUseCase
import com.example.proyectofinal.ui.components.Aviso
import com.example.proyectofinal.ui.components.BotonPrincipal
import com.example.proyectofinal.ui.components.CampoArea
import com.example.proyectofinal.ui.components.CampoPrecio
import com.example.proyectofinal.ui.components.CampoTexto
import com.example.proyectofinal.ui.components.EncabezadoPantalla
import com.example.proyectofinal.ui.components.FilaSwitch
import com.example.proyectofinal.ui.components.OpcionPastilla
import com.example.proyectofinal.ui.components.TituloCampo
import com.example.proyectofinal.ui.theme.Crema
import com.example.proyectofinal.ui.theme.Neutro
import com.example.proyectofinal.ui.theme.Terracota
import proyectofinal.shared.generated.resources.*

class PublicarVacanteEstado(
    private val vacantes: PublicarVacanteUseCase,
    val negocio: Negocio,
    private val idUsuario: String
) {
    var puesto by mutableStateOf("")
    var descripcion by mutableStateOf("")
    var horario by mutableStateOf("")
    var sueldo by mutableStateOf("")
    var sueldoATratar by mutableStateOf(false)
    var requisitos by mutableStateOf("")
    var errores by mutableStateOf<Map<Campo, String>>(emptyMap())
        private set
    var mensaje by mutableStateOf<String?>(null)
        private set

    /** Agrega un requisito rápido como renglón nuevo, sin repetirlo. */
    fun agregarRequisito(texto: String) {
        if (requisitos.lines().any { it.trim() == texto }) return
        requisitos = if (requisitos.isBlank()) texto else requisitos.trimEnd() + "\n" + texto
    }

    fun publicar(): Boolean {
        if (!sueldoATratar && sueldo.isBlank()) {
            errores = mapOf(Campo.SUELDO to "Escribe un sueldo o activa \"Sueldo a tratar\"")
            return false
        }
        val datos = DatosVacante(
            puesto = puesto, descripcion = descripcion, horario = horario,
            sueldoAprox = if (sueldoATratar) null else sueldo.toDoubleOrNull(),
            requisitos = requisitos
        )
        return when (val r = vacantes(negocio.id, idUsuario, datos)) {
            is Resultado.Exito -> true
            is Resultado.Error -> { errores = r.errores; mensaje = if (r.errores.isEmpty()) r.mensaje else null; false }
        }
    }
}

/** Publicar vacante (boceto 04 · "Publicar Vacante"). */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PublicarVacanteScreen(estado: PublicarVacanteEstado, onVolver: () -> Unit, onPublicada: () -> Unit) {
    Column(Modifier.fillMaxSize().background(Crema).safeDrawingPadding().imePadding()) {
        EncabezadoPantalla("Publicar vacante", onVolver)
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 8.dp)) {
            Text("Atrae talento de confianza", style = MaterialTheme.typography.headlineSmall, color = Neutro.t500)
            Text(
                "Publica lo que necesita tu oficio. Los vecinos interesados te escribirán por WhatsApp.",
                style = MaterialTheme.typography.bodyMedium, color = Neutro.t400
            )
            Spacer(Modifier.height(20.dp))
            CampoTexto("Puesto", estado.puesto, { estado.puesto = it }, "Ej. Ayudante de carpintería",
                Res.drawable.ic_maletin, estado.errores[Campo.PUESTO])
            Text("Sé específico con el nivel: aprendiz, oficial o maestro.", style = MaterialTheme.typography.bodySmall,
                color = Neutro.t300, modifier = Modifier.padding(top = 4.dp))
            Spacer(Modifier.height(16.dp))
            CampoArea("Descripción del empleo", estado.descripcion, { estado.descripcion = it },
                "Qué hará la persona día a día…", maximo = 500, error = estado.errores[Campo.DESCRIPCION])
            Spacer(Modifier.height(16.dp))
            CampoTexto("Horario y jornada", estado.horario, { estado.horario = it }, "Días y horas de entrada y salida",
                Res.drawable.ic_reloj, null)
            Spacer(Modifier.height(8.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Lunes a viernes, 9:00 a 18:00", "Medio tiempo", "Fines de semana").forEach { op ->
                    OpcionPastilla(op, estado.horario == op) { estado.horario = op }
                }
            }
            Spacer(Modifier.height(20.dp))
            TituloCampo("Sueldo")
            FilaSwitch("Sueldo a tratar", "Se acuerda en la entrevista", estado.sueldoATratar) { estado.sueldoATratar = it }
            if (!estado.sueldoATratar) {
                Spacer(Modifier.height(10.dp))
                CampoPrecio("Sueldo aproximado", estado.sueldo, { estado.sueldo = it }, estado.errores[Campo.SUELDO], "2500")
                Text("Aclara en la descripción si es semanal o quincenal.", style = MaterialTheme.typography.bodySmall,
                    color = Neutro.t300, modifier = Modifier.padding(top = 4.dp))
            }
            Spacer(Modifier.height(20.dp))
            CampoArea("Requisitos", estado.requisitos, { estado.requisitos = it },
                "Un requisito por renglón", maximo = 300)
            Spacer(Modifier.height(8.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Mayor de edad", "Puntualidad", "Experiencia de 1 año", "Herramienta propia").forEach { req ->
                    OpcionPastilla("+ $req", false) { estado.agregarRequisito(req) }
                }
            }
            Spacer(Modifier.height(20.dp))
            Aviso(Res.drawable.ic_ubicacion, "Lugar de trabajo", "${estado.negocio.nombre} · ${estado.negocio.direccion}")
            estado.mensaje?.let {
                Spacer(Modifier.height(12.dp))
                Text(it, style = MaterialTheme.typography.bodyMedium, color = Terracota.t700)
            }
            Spacer(Modifier.height(16.dp))
        }
        Column(Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
            BotonPrincipal("Publicar vacante", onClick = { if (estado.publicar()) onPublicada() })
        }
    }
}
