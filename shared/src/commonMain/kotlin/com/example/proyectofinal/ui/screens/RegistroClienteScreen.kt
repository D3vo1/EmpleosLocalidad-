package com.example.proyectofinal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.proyectofinal.domain.model.Campo
import com.example.proyectofinal.domain.model.Resultado
import com.example.proyectofinal.domain.model.Usuario
import com.example.proyectofinal.domain.usecase.DatosCuenta
import com.example.proyectofinal.domain.usecase.RegistrarClienteUseCase
import com.example.proyectofinal.domain.usecase.ValidarFormularioUseCase
import com.example.proyectofinal.ui.components.Aviso
import com.example.proyectofinal.ui.components.BotonPrincipal
import com.example.proyectofinal.ui.components.CampoTexto
import com.example.proyectofinal.ui.components.EncabezadoPantalla
import com.example.proyectofinal.ui.components.Etiqueta
import com.example.proyectofinal.ui.theme.Crema
import com.example.proyectofinal.ui.theme.Neutro
import com.example.proyectofinal.ui.theme.Oliva
import com.example.proyectofinal.ui.theme.Terracota
import proyectofinal.shared.generated.resources.*

/** Estado del formulario de registro de cliente. */
class RegistroClienteEstado(
    private val registrar: RegistrarClienteUseCase,
    private val validar: ValidarFormularioUseCase
) {
    var nombre by mutableStateOf("")
    var correo by mutableStateOf("")
    var telefono by mutableStateOf("")
    var contrasena by mutableStateOf("")
    var confirmacion by mutableStateOf("")
    var aceptaTerminos by mutableStateOf(false)
    var errores by mutableStateOf<Map<Campo, String>>(emptyMap())
        private set
    var errorTerminos by mutableStateOf<String?>(null)
        private set

    fun enviar(): Usuario? {
        if (!aceptaTerminos) {
            // Sin términos no se guarda nada: solo se revisan los campos para mostrar errores.
            errorTerminos = "Acepta los términos para crear tu cuenta"
            errores = buildMap {
                validar.nombre(nombre)?.let { put(Campo.NOMBRE, it) }
                validar.correo(correo)?.let { put(Campo.CORREO, it) }
                validar.telefono(telefono)?.let { put(Campo.TELEFONO, it) }
                validar.contrasena(contrasena)?.let { put(Campo.CONTRASENA, it) }
                validar.confirmacion(contrasena, confirmacion)?.let { put(Campo.CONFIRMACION, it) }
            }
            return null
        }
        errorTerminos = null
        return when (val r = registrar(DatosCuenta(nombre, correo, telefono, contrasena, confirmacion))) {
            is Resultado.Exito -> { errores = emptyMap(); r.dato }
            is Resultado.Error -> { errores = r.errores; null }
        }
    }
}

/** Registro de cliente (boceto 01 · "Crea tu cuenta"). */
@Composable
fun RegistroClienteScreen(
    estado: RegistroClienteEstado,
    onVolver: () -> Unit,
    onCreada: (Usuario) -> Unit,
    onIniciarSesion: () -> Unit,
    onTerminos: () -> Unit
) {
    Column(Modifier.fillMaxSize().background(Crema).safeDrawingPadding().imePadding()) {
        EncabezadoPantalla("", onVolver) { Etiqueta("Cuenta de cliente · Jiquilpan", Oliva.t100, Oliva.t700, Res.drawable.ic_usuario) }
        Column(Modifier.verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 8.dp)) {
            Aviso(Res.drawable.ic_tienda, "Red comunitaria de oficios", "Conectamos vecinos con maestros carpinteros, herreros y plomeros locales.", verde = false)
            Spacer(Modifier.height(20.dp))
            Text("Crea tu cuenta", style = MaterialTheme.typography.displaySmall, color = Neutro.t500)
            Spacer(Modifier.height(6.dp))
            Text(
                "Guarda tus oficios de confianza y deja reseñas a los negocios de tu colonia.",
                style = MaterialTheme.typography.bodyLarge, color = Neutro.t400
            )
            Spacer(Modifier.height(20.dp))
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                CampoTexto("Nombre completo", estado.nombre, { estado.nombre = it }, "Ej. Laura Gómez Navarro",
                    Res.drawable.ic_usuario, estado.errores[Campo.NOMBRE])
                CampoTexto("Correo electrónico", estado.correo, { estado.correo = it }, "laura@ejemplo.com",
                    Res.drawable.ic_correo, estado.errores[Campo.CORREO], KeyboardType.Email)
                CampoTexto("Teléfono celular (WhatsApp)", estado.telefono, { estado.telefono = it }, "353 123 4567",
                    Res.drawable.ic_telefono, estado.errores[Campo.TELEFONO], KeyboardType.Phone)
                CampoTexto("Contraseña", estado.contrasena, { estado.contrasena = it }, "Mínimo 8 caracteres, con números",
                    Res.drawable.ic_candado, estado.errores[Campo.CONTRASENA], esContrasena = true)
                CampoTexto("Confirmar contraseña", estado.confirmacion, { estado.confirmacion = it }, "Repite tu contraseña",
                    Res.drawable.ic_candado, estado.errores[Campo.CONFIRMACION], esContrasena = true)
            }
            Spacer(Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = estado.aceptaTerminos,
                    onCheckedChange = { estado.aceptaTerminos = it },
                    colors = CheckboxDefaults.colors(checkedColor = Terracota.t500)
                )
                Text("Acepto los ", style = MaterialTheme.typography.bodyMedium, color = Neutro.t500)
                Text(
                    "términos y la convivencia vecinal", style = MaterialTheme.typography.labelMedium, color = Terracota.t600,
                    modifier = Modifier.clip(RoundedCornerShape(6.dp)).clickable(onClick = onTerminos).padding(vertical = 6.dp)
                )
            }
            estado.errorTerminos?.let { Text(it, style = MaterialTheme.typography.bodySmall, color = Terracota.t700) }
            Spacer(Modifier.height(16.dp))
            BotonPrincipal("Crear cuenta", onClick = { estado.enviar()?.let(onCreada) }, iconoFinal = Res.drawable.ic_adelante)
            Spacer(Modifier.height(16.dp))
            Aviso(Res.drawable.ic_verificado, "Vecinos de confianza", "Protegemos la seguridad de cada hogar y oficio registrado en Jiquilpan.")
            Spacer(Modifier.height(16.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                Text("¿Ya tienes cuenta? ", style = MaterialTheme.typography.bodyMedium, color = Neutro.t400)
                Text(
                    "Inicia sesión", style = MaterialTheme.typography.labelMedium, color = Terracota.t600,
                    modifier = Modifier.clickable(onClick = onIniciarSesion).padding(vertical = 8.dp)
                )
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}
