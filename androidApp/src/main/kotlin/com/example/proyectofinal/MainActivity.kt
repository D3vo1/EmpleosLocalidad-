package com.example.proyectofinal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.example.proyectofinal.ui.navigation.Navegador

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val navegador = remember { Navegador() }
            // El botón atrás de Android regresa a la pantalla anterior de la app;
            // si no hay a dónde regresar, Android cierra la app como siempre.
            BackHandler(enabled = navegador.puedeVolver) { navegador.volver() }
            App(navegador)
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
