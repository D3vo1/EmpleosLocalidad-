package com.example.proyectofinal.data

import com.example.proyectofinal.domain.model.Negocio
import com.example.proyectofinal.domain.model.Resena
import com.example.proyectofinal.domain.model.Servicio
import com.example.proyectofinal.domain.model.Usuario
import com.example.proyectofinal.domain.model.Vacante
import com.example.proyectofinal.domain.repository.FavoritosRepository
import com.example.proyectofinal.domain.repository.GestionNegocioRepository
import com.example.proyectofinal.domain.repository.NegocioRepository
import com.example.proyectofinal.domain.repository.UsuarioRepository


class RepositorioMemoria(semilla: DatosSemilla.Contenido = DatosSemilla.crear()) :
    NegocioRepository, GestionNegocioRepository, UsuarioRepository, FavoritosRepository {

    private val usuarios = semilla.usuarios.associateBy { it.id }.toMutableMap()
    private val negocios = semilla.negocios.associateBy { it.id }.toMutableMap()
    private val servicios = semilla.servicios.associateBy { it.id }.toMutableMap()
    private val vacantes = semilla.vacantes.associateBy { it.id }.toMutableMap()
    private val resenas = semilla.resenas.associateBy { it.id }.toMutableMap()
    private val favoritos = mutableMapOf<String, MutableSet<String>>()

    // UsuarioRepository
    override fun buscarPorCorreo(correo: String): Usuario? = usuarios.values.find { it.correo == correo }
    override fun buscarPorId(id: String): Usuario? = usuarios[id]
    override fun guardar(usuario: Usuario) { usuarios[usuario.id] = usuario }

    // NegocioRepository (lectura)
    override fun obtenerNegocios(): List<Negocio> = negocios.values.toList()
    override fun obtenerNegocio(id: String): Negocio? = negocios[id]
    override fun obtenerServicios(idNegocio: String): List<Servicio> = servicios.values.filter { it.idNegocio == idNegocio }
    override fun obtenerResenas(idNegocio: String): List<Resena> = resenas.values.filter { it.idNegocio == idNegocio }
    override fun obtenerVacantes(idNegocio: String): List<Vacante> = vacantes.values.filter { it.idNegocio == idNegocio }

    // GestionNegocioRepository (escritura)
    override fun guardarNegocio(negocio: Negocio) { negocios[negocio.id] = negocio }
    override fun obtenerServicio(id: String): Servicio? = servicios[id]
    override fun guardarServicio(servicio: Servicio) { servicios[servicio.id] = servicio }
    override fun obtenerVacante(id: String): Vacante? = vacantes[id]
    override fun guardarVacante(vacante: Vacante) { vacantes[vacante.id] = vacante }
    override fun guardarResena(resena: Resena) { resenas[resena.id] = resena }

    // FavoritosRepository
    override fun obtenerFavoritos(idUsuario: String): Set<String> = favoritos[idUsuario].orEmpty().toSet()
    override fun agregar(idUsuario: String, idNegocio: String) { favoritos.getOrPut(idUsuario) { mutableSetOf() } += idNegocio }
    override fun quitar(idUsuario: String, idNegocio: String) { favoritos[idUsuario]?.remove(idNegocio) }
}
