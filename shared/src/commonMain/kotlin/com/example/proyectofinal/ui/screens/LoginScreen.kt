package com.example.proyectofinal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.proyectofinal.data.DatosSemilla
import com.example.proyectofinal.domain.model.Campo
import com.example.proyectofinal.domain.model.Resultado
import com.example.proyectofinal.domain.model.Rol
import com.example.proyectofinal.domain.model.Usuario
import com.example.proyectofinal.domain.usecase.IniciarSesionUseCase
import com.example.proyectofinal.ui.components.BotonPrincipal
import com.example.proyectofinal.ui.components.CampoTexto
import com.example.proyectofinal.ui.components.Icono
import com.example.proyectofinal.ui.theme.Blanco
import com.example.proyectofinal.ui.theme.Crema
import com.example.proyectofinal.ui.theme.Neutro
import com.example.proyectofinal.ui.theme.Oliva
import com.example.proyectofinal.ui.theme.Terracota
import proyectofinal.shared.generated.resources.*

/** Estado del formulario de login: lo que se escribe y los errores que regresa el caso de uso. */
class LoginEstado(private val iniciarSesion: IniciarSesionUseCase, val rol: Rol) {
    var correo by mutableStateOf("")
    var contrasena by mutableStateOf("")
    var errores by mutableStateOf<Map<Campo, String>>(emptyMap())
        private set
    var mensaje by mutableStateOf<String?>(null)
        private set

    fun enviar(): Usuario? = when (val r = iniciarSesion(correo, contrasena, rol)) {
        is Resultado.Exito -> r.dato
        is Resultado.Error -> {
            errores = r.errores
            mensaje = if (r.errores.isEmpty()) r.mensaje else null
            null
        }
    }

    /** Llena la cuenta de prueba de los datos semilla, útil para presentar la app. */
    fun usarCuentaDemo() {
        correo = if (rol == Rol.CLIENTE) DatosSemilla.CLIENTE_DEMO else DatosSemilla.NEGOCIO_DEMO
        contrasena = DatosSemilla.CONTRASENA_DEMO
        errores = emptyMap(); mensaje = null
    }
}

/** Login (boceto 01): solo correo y contraseña, con el rol elegido visible arriba. */
@Composable
fun LoginScreen(
    estado: LoginEstado,
    onVolver: () -> Unit,
    onEntrar: (Usuario) -> Unit,
    onRegistro: () -> Unit
) {
    Column(
        Modifier.fillMaxSize().background(Crema).safeDrawingPadding().imePadding()
            .verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onVolver) { Icono(Res.drawable.ic_atras, "Volver", Neutro.t500, Modifier.size(22.dp)) }
        }

        Spacer(Modifier.height(4.dp))
        Row(
            Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(Blanco).clickable(onClick = onVolver)
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(Modifier.size(30.dp).clip(CircleShape).background(Terracota.t50), contentAlignment = Alignment.Center) {
                Icono(Res.drawable.ic_usuario, null, Terracota.t500, Modifier.size(16.dp))
            }
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text("Iniciando como", style = MaterialTheme.typography.bodySmall, color = Neutro.t400)
                Text(
                    if (estado.rol == Rol.CLIENTE) "Busco servicios (cliente)" else "Tengo un negocio u oficio",
                    style = MaterialTheme.typography.titleSmall, color = Neutro.t500
                )
            }
            Text("Cambiar rol", style = MaterialTheme.typography.labelMedium, color = Terracota.t600)
        }

        Spacer(Modifier.height(28.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icono(Res.drawable.ic_herramienta, null, Terracota.t500, Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text("Oficios Jiquilpan", style = MaterialTheme.typography.titleSmall, color = Terracota.t600)
        }
        Spacer(Modifier.height(6.dp))
        Text("¡Hola de nuevo!", style = MaterialTheme.typography.displaySmall, color = Neutro.t500)
        Spacer(Modifier.height(6.dp))
        Text(
            if (estado.rol == Rol.CLIENTE)
                "Ingresa a tu cuenta para contactar a los mejores plomeros, herreros y artesanos de nuestra comunidad."
            else "Ingresa para administrar tus servicios, vacantes y reseñas.",
            style = MaterialTheme.typography.bodyLarge, color = Neutro.t400
        )

        Spacer(Modifier.height(24.dp))
        CampoTexto(
            etiqueta = "Correo electrónico", valor = estado.correo, onCambio = { estado.correo = it },
            placeholder = "ejemplo@correo.com", icono = Res.drawable.ic_correo,
            error = estado.errores[Campo.CORREO], tipoTeclado = KeyboardType.Email
        )
        Spacer(Modifier.height(16.dp))
        CampoTexto(
            etiqueta = "Contraseña", valor = estado.contrasena, onCambio = { estado.contrasena = it },
            placeholder = "Tu contraseña", icono = Res.drawable.ic_candado,
            error = estado.errores[Campo.CONTRASENA], esContrasena = true
        )

        estado.mensaje?.let {
            Spacer(Modifier.height(14.dp))
            Text(
                it, style = MaterialTheme.typography.bodyMedium, color = Terracota.t800,
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(Terracota.t50).padding(12.dp)
            )
        }

        Spacer(Modifier.height(22.dp))
        BotonPrincipal("Iniciar sesión", onClick = { estado.enviar()?.let(onEntrar) }, iconoFinal = Res.drawable.ic_adelante)
        Spacer(Modifier.height(8.dp))
        Text(
            "Usar cuenta de prueba",
            style = MaterialTheme.typography.labelMedium, color = Neutro.t400,
            modifier = Modifier.align(Alignment.CenterHorizontally).clip(RoundedCornerShape(8.dp))
                .clickable { estado.usarCuentaDemo() }.padding(horizontal = 10.dp, vertical = 8.dp)
        )

        Spacer(Modifier.height(18.dp))
        Row(
            Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(Oliva.t50).padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icono(Res.drawable.ic_verificado, null, Oliva.t500, Modifier.size(22.dp))
            Spacer(Modifier.width(10.dp))
            Column {
                Text("Red segura de confianza", style = MaterialTheme.typography.titleSmall, color = Oliva.t800)
                Text(
                    "Los prestadores se registran con identificación y domicilio en Jiquilpan y zonas cercanas.",
                    style = MaterialTheme.typography.bodySmall, color = Oliva.t700
                )
            }
        }

        Spacer(Modifier.height(20.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            Text("¿Aún no tienes cuenta? ", style = MaterialTheme.typography.bodyMedium, color = Neutro.t400)
            Text(
                "Regístrate aquí", style = MaterialTheme.typography.labelMedium, color = Terracota.t600,
                modifier = Modifier.clickable(onClick = onRegistro).padding(vertical = 8.dp)
            )
        }
    }
}
