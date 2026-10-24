package com.example.proyectofinal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.proyectofinal.data.AppContainer
import com.example.proyectofinal.ui.navigation.AppNavegacion
import com.example.proyectofinal.ui.navigation.Navegador
import com.example.proyectofinal.ui.theme.OficiosTheme

/**
 * Punto de entrada compartido por Android e iOS.
 * Android pasa su [Navegador] para conectar el botón atrás del sistema (ver MainActivity).
 */
@Composable
fun App(navegador: Navegador = remember { Navegador() }) {
    val container = remember { AppContainer() }
    OficiosTheme {
        AppNavegacion(navegador, container)
    }
}
