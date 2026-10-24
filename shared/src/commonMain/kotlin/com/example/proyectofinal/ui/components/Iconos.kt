package com.example.proyectofinal.ui.components

import com.example.proyectofinal.domain.model.Categoria
import org.jetbrains.compose.resources.DrawableResource
import proyectofinal.shared.generated.resources.*

fun Categoria.icono(): DrawableResource = when (this) {
    Categoria.PLOMERIA -> Res.drawable.ic_cat_plomeria
    Categoria.ELECTRICIDAD -> Res.drawable.ic_cat_electricidad
    Categoria.CLASES -> Res.drawable.ic_cat_clases
    Categoria.FOTOGRAFIA -> Res.drawable.ic_cat_fotografia
    Categoria.CARPINTERIA -> Res.drawable.ic_cat_carpinteria
    Categoria.MECANICA -> Res.drawable.ic_cat_mecanica
    Categoria.BELLEZA -> Res.drawable.ic_cat_belleza
    Categoria.ALBANILERIA -> Res.drawable.ic_cat_albanileria
    Categoria.HERRERIA -> Res.drawable.ic_cat_herreria
    Categoria.JARDINERIA -> Res.drawable.ic_cat_jardineria
}
