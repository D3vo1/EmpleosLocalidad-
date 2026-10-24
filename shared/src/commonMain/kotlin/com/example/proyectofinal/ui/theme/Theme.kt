package com.example.proyectofinal.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val esquemaOficios = lightColorScheme(
    primary = Terracota.t500,
    onPrimary = Blanco,
    primaryContainer = Terracota.t100,
    onPrimaryContainer = Terracota.t800,
    secondary = Oliva.t500,
    onSecondary = Blanco,
    secondaryContainer = Oliva.t100,
    onSecondaryContainer = Oliva.t800,
    tertiary = Ambar.t500,
    onTertiary = Neutro.t500,
    tertiaryContainer = Ambar.t100,
    onTertiaryContainer = Ambar.t800,
    background = Crema,
    onBackground = Neutro.t500,
    surface = Blanco,
    onSurface = Neutro.t500,
    surfaceVariant = Terracota.t50,
    onSurfaceVariant = Neutro.t400,
    surfaceContainerLowest = Blanco,
    surfaceContainerLow = Crema,
    surfaceContainer = Blanco,
    surfaceContainerHigh = Blanco,
    outline = Neutro.t200,
    outlineVariant = Borde,
    error = Terracota.t700,
    onError = Blanco
)

/** Tarjetas 16 dp, campos y botones 12 dp, chips totalmente redondos (se piden con CircleShape). */
private val formasOficios = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun OficiosTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = esquemaOficios,
        typography = tipografiaOficios(),
        shapes = formasOficios,
        content = content
    )
}
