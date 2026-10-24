package com.example.proyectofinal.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import proyectofinal.shared.generated.resources.Res
import proyectofinal.shared.generated.resources.nunito_bold
import proyectofinal.shared.generated.resources.nunito_extrabold
import proyectofinal.shared.generated.resources.nunito_regular
import proyectofinal.shared.generated.resources.nunito_semibold

/** Nunito: sans redondeada, cercana y legible, como en el boceto. */
@Composable
fun nunito(): FontFamily = FontFamily(
    Font(Res.font.nunito_regular, FontWeight.Normal),
    Font(Res.font.nunito_semibold, FontWeight.SemiBold),
    Font(Res.font.nunito_bold, FontWeight.Bold),
    Font(Res.font.nunito_extrabold, FontWeight.ExtraBold)
)

/**
 * Escala tipográfica 28 / 22 / 17 / 15 / 13 / 11.
 * Los títulos usan ExtraBold con interlineado corto; el texto corrido, Regular con 1.45.
 */
@Composable
fun tipografiaOficios(): Typography {
    val f = nunito()
    return Typography(
        displaySmall = TextStyle(fontFamily = f, fontWeight = FontWeight.ExtraBold, fontSize = 28.sp, lineHeight = 32.sp, color = Neutro.t500),
        headlineSmall = TextStyle(fontFamily = f, fontWeight = FontWeight.ExtraBold, fontSize = 22.sp, lineHeight = 27.sp),
        titleLarge = TextStyle(fontFamily = f, fontWeight = FontWeight.Bold, fontSize = 17.sp, lineHeight = 22.sp),
        titleMedium = TextStyle(fontFamily = f, fontWeight = FontWeight.Bold, fontSize = 15.sp, lineHeight = 20.sp),
        titleSmall = TextStyle(fontFamily = f, fontWeight = FontWeight.Bold, fontSize = 13.sp, lineHeight = 18.sp),
        bodyLarge = TextStyle(fontFamily = f, fontWeight = FontWeight.Normal, fontSize = 15.sp, lineHeight = 22.sp),
        bodyMedium = TextStyle(fontFamily = f, fontWeight = FontWeight.Normal, fontSize = 13.sp, lineHeight = 19.sp),
        bodySmall = TextStyle(fontFamily = f, fontWeight = FontWeight.Normal, fontSize = 11.sp, lineHeight = 15.sp),
        labelLarge = TextStyle(fontFamily = f, fontWeight = FontWeight.Bold, fontSize = 15.sp, lineHeight = 20.sp),
        labelMedium = TextStyle(fontFamily = f, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, lineHeight = 16.sp),
        labelSmall = TextStyle(fontFamily = f, fontWeight = FontWeight.SemiBold, fontSize = 11.sp, lineHeight = 14.sp)
    )
}
