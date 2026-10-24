package com.example.proyectofinal.domain.usecase

import com.example.proyectofinal.domain.model.*
import com.example.proyectofinal.domain.repository.FavoritosRepository
import com.example.proyectofinal.domain.repository.GestionNegocioRepository
import com.example.proyectofinal.domain.repository.NegocioRepository
import com.example.proyectofinal.domain.repository.UsuarioRepository

/**
 * Repositorio falso de lectura y escritura para las pruebas de la Persona B.
 * Es casi lo que hará la capa data, pero solo para pruebas.
 */
class MemoriaFalsa : NegocioRepository, GestionNegocioRepository, UsuarioRepository, FavoritosRepository {
    val usuarios = mutableMapOf<String, Usuario>()
    val negocios = mutableMapOf<String, Negocio>()
    val servicios = mutableMapOf<String, Servicio>()
    val vacantes = mutableMapOf<String, Vacante>()
    val resenas = mutableMapOf<String, Resena>()
    val favoritos = mutableMapOf<String, MutableSet<String>>()

    // UsuarioRepository
    override fun buscarPorCorreo(correo: String) = usuarios.values.find { it.correo == correo }
    override fun buscarPorId(id: String) = usuarios[id]
    override fun guardar(usuario: Usuario) { usuarios[usuario.id] = usuario }

    // NegocioRepository (lectura)
    override fun obtenerNegocios() = negocios.values.toList()
    override fun obtenerNegocio(id: String) = negocios[id]
    override fun obtenerServicios(idNegocio: String) = servicios.values.filter { it.idNegocio == idNegocio }
    override fun obtenerResenas(idNegocio: String) = resenas.values.filter { it.idNegocio == idNegocio }
    override fun obtenerVacantes(idNegocio: String) = vacantes.values.filter { it.idNegocio == idNegocio }

    // GestionNegocioRepository (escritura)
    override fun guardarNegocio(negocio: Negocio) { negocios[negocio.id] = negocio }
    override fun obtenerServicio(id: String) = servicios[id]
    override fun guardarServicio(servicio: Servicio) { servicios[servicio.id] = servicio }
    override fun obtenerVacante(id: String) = vacantes[id]
    override fun guardarVacante(vacante: Vacante) { vacantes[vacante.id] = vacante }
    override fun guardarResena(resena: Resena) { resenas[resena.id] = resena }

    // FavoritosRepository
    override fun obtenerFavoritos(idUsuario: String): Set<String> = favoritos[idUsuario].orEmpty()
    override fun agregar(idUsuario: String, idNegocio: String) { favoritos.getOrPut(idUsuario) { mutableSetOf() } += idNegocio }
    override fun quitar(idUsuario: String, idNegocio: String) { favoritos[idUsuario]?.remove(idNegocio) }
}

/** Generador de ids predecible para las pruebas: "id1", "id2", ... */
class IdsSecuenciales {
    private var n = 0
    fun siguiente(): String = "id${++n}"
}
