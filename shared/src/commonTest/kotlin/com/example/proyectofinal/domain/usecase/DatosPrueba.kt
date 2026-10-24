package com.example.proyectofinal.domain.usecase

import com.example.proyectofinal.domain.model.*
import com.example.proyectofinal.domain.repository.NegocioRepository

/** Datos y repositorio falso compartidos por las pruebas de la Persona A. */
object DatosPrueba {

    fun negocio(id: String, nombre: String, oficio: String, categoria: Categoria,
                descripcion: String = "", horarios: List<Horario> = emptyList()) =
        Negocio(id, nombre, oficio, categoria, descripcion, "Centro, Jiquilpan",
            "3530000000", "3530000000", emptyList(), horarios, "dueno-$id")

    fun resena(idNegocio: String, estrellas: Int, fecha: Long = 0L) =
        Resena("r-$idNegocio-$estrellas-$fecha", idNegocio, "u1", "Ana", estrellas, "", fecha)

    fun resumen(negocio: Negocio, promedio: Double, total: Int, precio: Double?) =
        NegocioResumen(negocio, Calificacion(promedio, total, emptyMap()), precio)

    val plomeria = negocio("1", "Plomería Don Juan", "Plomero", Categoria.PLOMERIA,
        "Reparación de fugas y tinacos")
    val luz = negocio("2", "Electro Rápido", "Electricista", Categoria.ELECTRICIDAD,
        "Instalaciones eléctricas residenciales")
    val ingles = negocio("3", "Teacher Lupita", "Maestra", Categoria.CLASES,
        "Clases de inglés para niños")

    class RepositorioFalso(
        private val negocios: List<Negocio>,
        private val servicios: List<Servicio>,
        private val resenas: List<Resena>,
        private val vacantes: List<Vacante> = emptyList()
    ) : NegocioRepository {
        override fun obtenerNegocios() = negocios
        override fun obtenerNegocio(id: String) = negocios.find { it.id == id }
        override fun obtenerServicios(idNegocio: String) = servicios.filter { it.idNegocio == idNegocio }
        override fun obtenerResenas(idNegocio: String) = resenas.filter { it.idNegocio == idNegocio }
        override fun obtenerVacantes(idNegocio: String) = vacantes.filter { it.idNegocio == idNegocio }
    }
}
