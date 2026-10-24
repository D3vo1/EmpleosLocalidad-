package com.example.proyectofinal.domain.repository

import com.example.proyectofinal.domain.model.Negocio
import com.example.proyectofinal.domain.model.Resena
import com.example.proyectofinal.domain.model.Servicio
import com.example.proyectofinal.domain.model.Vacante

/**
 * Contrato de escritura (Persona B): lo que cambia el dueño del negocio o un cliente.
 * Las lecturas siguen en NegocioRepository (Persona A); la capa data puede
 * implementar ambas interfaces en una sola clase.
 * Todos los "guardar" insertan o reemplazan por id.
 */
interface GestionNegocioRepository {
    fun guardarNegocio(negocio: Negocio)
    fun obtenerServicio(id: String): Servicio?
    fun guardarServicio(servicio: Servicio)
    fun obtenerVacante(id: String): Vacante?
    fun guardarVacante(vacante: Vacante)
    fun guardarResena(resena: Resena)
}
