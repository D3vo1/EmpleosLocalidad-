package com.example.proyectofinal.data

import com.example.proyectofinal.domain.usecase.AgregarResenaUseCase
import com.example.proyectofinal.domain.usecase.BuscarNegociosUseCase
import com.example.proyectofinal.domain.usecase.FiltrarNegociosUseCase
import com.example.proyectofinal.domain.usecase.GestionarFavoritosUseCase
import com.example.proyectofinal.domain.usecase.GestionarServiciosUseCase
import com.example.proyectofinal.domain.usecase.IniciarSesionUseCase
import com.example.proyectofinal.domain.usecase.ObtenerDetalleNegocioUseCase
import com.example.proyectofinal.domain.usecase.ObtenerNegocioDelDuenoUseCase
import com.example.proyectofinal.domain.usecase.ObtenerNegociosUseCase
import com.example.proyectofinal.domain.usecase.OrdenarNegociosUseCase
import com.example.proyectofinal.domain.usecase.PublicarVacanteUseCase
import com.example.proyectofinal.domain.usecase.RegistrarClienteUseCase
import com.example.proyectofinal.domain.usecase.RegistrarNegocioUseCase
import com.example.proyectofinal.domain.usecase.ValidarFormularioUseCase


class AppContainer(private val repositorio: RepositorioMemoria = RepositorioMemoria()) {
    // Descubrimiento (Persona A)
    val obtenerNegocios = ObtenerNegociosUseCase(repositorio)
    val buscarNegocios = BuscarNegociosUseCase()
    val filtrarNegocios = FiltrarNegociosUseCase()
    val ordenarNegocios = OrdenarNegociosUseCase()
    val obtenerDetalle = ObtenerDetalleNegocioUseCase(repositorio)

    // Cuentas y lado negocio (Persona B)
    val validar = ValidarFormularioUseCase()
    val iniciarSesion = IniciarSesionUseCase(repositorio)
    val registrarCliente = RegistrarClienteUseCase(repositorio)
    val registrarNegocio = RegistrarNegocioUseCase(repositorio, repositorio)
    val negocioDelDueno = ObtenerNegocioDelDuenoUseCase(repositorio)
    val servicios = GestionarServiciosUseCase(repositorio, repositorio)
    val vacantes = PublicarVacanteUseCase(repositorio, repositorio)
    val resenas = AgregarResenaUseCase(repositorio, repositorio)
    val favoritos = GestionarFavoritosUseCase(repositorio, repositorio)

    /** Colonias que ya existen en los datos, para elegir en registro y en "Mi colonia". */
    val colonias: List<String>
        get() = repositorio.obtenerNegocios().map { it.colonia }.filter { it.isNotBlank() }.distinct().sorted()
}
