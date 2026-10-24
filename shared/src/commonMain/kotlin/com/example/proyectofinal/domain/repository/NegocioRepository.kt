package com.example.proyectofinal.domain.repository

import com.example.proyectofinal.domain.model.Negocio
import com.example.proyectofinal.domain.model.Resena
import com.example.proyectofinal.domain.model.Servicio
import com.example.proyectofinal.domain.model.Vacante

/**
 * Contrato de lectura de negocios (lado cliente, Persona A).
 * La implementación en memoria va en la capa data (Fase 2).
 * Las escrituras están en GestionNegocioRepository (Persona B).
 */
interface NegocioRepository {
    fun obtenerNegocios(): List<Negocio>
    fun obtenerNegocio(id: String): Negocio?
    fun obtenerServicios(idNegocio: String): List<Servicio>
    fun obtenerResenas(idNegocio: String): List<Resena>
    /** Agregada por la Persona B para la pestaña Vacantes del detalle. */
    fun obtenerVacantes(idNegocio: String): List<Vacante>
}
