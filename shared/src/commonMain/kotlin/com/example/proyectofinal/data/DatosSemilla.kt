package com.example.proyectofinal.data

import com.example.proyectofinal.domain.model.Categoria
import com.example.proyectofinal.domain.model.Categoria.*
import com.example.proyectofinal.domain.model.DiaSemana
import com.example.proyectofinal.domain.model.Horario
import com.example.proyectofinal.domain.model.Negocio
import com.example.proyectofinal.domain.model.Resena
import com.example.proyectofinal.domain.model.Rol
import com.example.proyectofinal.domain.model.Servicio
import com.example.proyectofinal.domain.model.Usuario
import com.example.proyectofinal.domain.model.Vacante


object DatosSemilla {

    data class Contenido(
        val usuarios: List<Usuario>,
        val negocios: List<Negocio>,
        val servicios: List<Servicio>,
        val vacantes: List<Vacante>,
        val resenas: List<Resena>
    )

    const val CLIENTE_DEMO = "cliente@demo.mx"
    const val NEGOCIO_DEMO = "negocio@demo.mx"
    const val CONTRASENA_DEMO = "demo1234"


    private const val BASE_FECHA = 1_790_812_800_000L
    private const val DIA = 86_400_000L

    private val LUN_VIE = listOf(DiaSemana.LUNES, DiaSemana.MARTES, DiaSemana.MIERCOLES, DiaSemana.JUEVES, DiaSemana.VIERNES)

    private fun horario(dias: List<DiaSemana>, abre: Int, cierra: Int, sabado: Pair<Int, Int>? = null): List<Horario> =
        dias.map { Horario(it, abre * 60, cierra * 60) } +
            (sabado?.let { listOf(Horario(DiaSemana.SABADO, it.first * 60, it.second * 60)) } ?: emptyList())

    private val todosLosDias = DiaSemana.entries.map { Horario(it, 0, 1440) }

    private class Ficha(
        val id: String, val nombre: String, val oficio: String, val categoria: Categoria,
        val descripcion: String, val colonia: String, val direccion: String, val telefono: String,
        val horarios: List<Horario>,
        val servicios: List<Triple<String, String, Double>>,
        val estrellas: List<Int>
    )

    private val fichas = listOf(
        Ficha("n01", "Taller Don Chema", "Carpintería y muebles finos", CARPINTERIA,
            "Muebles de parota y pino hechos a mano. Más de 25 años trabajando la madera en Jiquilpan.",
            "Col. Centro", "Calle Lic. Justo Sierra 142, Col. Centro", "3535550101",
            horario(LUN_VIE, 9, 18, 9 to 14),
            listOf(Triple("Fabricación de muebles a medida", "Comedores, alacenas y libreros en madera maciza.", 1800.0),
                Triple("Reparación y laqueado de puertas", "Cepillado, ajuste de bisagras y laqueado.", 450.0),
                Triple("Cocinas integrales artesanales", "Diseño, medición e instalación en tu casa.", 6500.0)),
            listOf(5, 5, 5, 4, 5, 5, 4)),
        Ficha("n02", "Instalaciones Ruiz", "Electricidad y reparaciones", ELECTRICIDAD,
            "Instalaciones residenciales, cambio de cableado y atención de emergencias las 24 horas.",
            "Col. San Rafael", "Av. Lázaro Cárdenas 310, Col. San Rafael", "3535550102",
            todosLosDias,
            listOf(Triple("Revisión de instalación eléctrica", "Diagnóstico de cortos y fugas de corriente.", 250.0),
                Triple("Cambio de centro de carga", "Incluye pastillas termomagnéticas básicas.", 1200.0)),
            listOf(5, 5, 4, 5, 5)),
        Ficha("n03", "Plomería El Águila", "Plomería e instalación de gas", PLOMERIA,
            "Fugas, tinacos, boilers e instalaciones de gas con garantía por escrito.",
            "Col. Las Lomas", "Calle Morelos 58, Col. Las Lomas", "3535550103",
            horario(LUN_VIE, 8, 19, 8 to 15),
            listOf(Triple("Reparación de fugas", "Detección y reparación en cocina y baño.", 280.0),
                Triple("Instalación de boiler", "Boiler de paso o depósito, mano de obra.", 900.0),
                Triple("Limpieza de tinaco", "Lavado y desinfección de tinacos hasta 1100 L.", 350.0)),
            listOf(5, 4, 5, 5, 4, 5)),
        Ficha("n04", "Mecánica Rápida Jiquilpan", "Afinación y frenos", MECANICA,
            "Afinaciones, frenos y suspensión con diagnóstico por computadora.",
            "Calzada Zaragoza", "Calzada Zaragoza 1020, Jiquilpan", "3535550104",
            horario(LUN_VIE, 9, 19, 9 to 15),
            listOf(Triple("Afinación mayor", "Bujías, filtros y limpieza de inyectores.", 1400.0),
                Triple("Cambio de balatas", "Delanteras o traseras, mano de obra.", 400.0)),
            listOf(5, 4, 4, 5)),
        Ficha("n05", "Luz & Memoria", "Fotografía de eventos", FOTOGRAFIA,
            "Bodas, XV años y sesiones familiares en el Bosque Cuauhtémoc y el centro histórico.",
            "Centro Histórico", "Portal Hidalgo 12, Centro Histórico", "3535550105",
            horario(LUN_VIE, 10, 20, 10 to 20),
            listOf(Triple("Sesión familiar", "1 hora, 20 fotos editadas.", 900.0),
                Triple("Cobertura de evento", "6 horas, álbum digital.", 4500.0)),
            listOf(5, 5, 5, 5)),
        Ficha("n06", "Herrería Los Portales", "Herrería y soldadura", HERRERIA,
            "Portones, protecciones y barandales a la medida, con pintura incluida.",
            "Col. Revolución", "Calle Revolución 230, Col. Revolución", "3535550106",
            horario(LUN_VIE, 8, 18, 8 to 13),
            listOf(Triple("Protecciones para ventana", "Diseño sencillo, por metro cuadrado.", 850.0),
                Triple("Portón de cochera", "Fabricación e instalación.", 9500.0)),
            listOf(4, 5, 4)),
        Ficha("n07", "Clases con la Maestra Lupita", "Clases de inglés y regularización", CLASES,
            "Inglés para niños y adolescentes, regularización de primaria y secundaria.",
            "Col. Centro", "Calle Allende 77, Col. Centro", "3535550107",
            horario(LUN_VIE, 15, 20),
            listOf(Triple("Clase de inglés", "Clase grupal de 1 hora.", 150.0),
                Triple("Regularización", "Clase individual de 1 hora.", 200.0)),
            listOf(5, 5, 4, 5, 5)),
        Ficha("n08", "Estética Mary", "Corte, color y peinado", BELLEZA,
            "Cortes para dama y caballero, tintes y peinados para eventos.",
            "Col. Lázaro Cárdenas", "Calle Hidalgo 15, Col. Lázaro Cárdenas", "3535550108",
            horario(LUN_VIE, 10, 19, 9 to 17),
            listOf(Triple("Corte de cabello", "Dama o caballero.", 120.0),
                Triple("Tinte completo", "Incluye lavado y secado.", 650.0),
                Triple("Peinado para evento", "Recogido o semirrecogido.", 400.0)),
            listOf(4, 5, 5, 4)),
        Ficha("n09", "Construcciones Ochoa", "Albañilería y remodelación", ALBANILERIA,
            "Bardas, firmes, aplanados y remodelación de baños y cocinas.",
            "Col. Las Lomas", "Calle Galeana 410, Col. Las Lomas", "3535550109",
            horario(LUN_VIE, 8, 17, 8 to 13),
            listOf(Triple("Aplanado de muros", "Por metro cuadrado.", 180.0),
                Triple("Remodelación de baño", "Presupuesto según medidas.", 12000.0)),
            listOf(4, 4, 5)),
        Ficha("n10", "Jardines del Lago", "Jardinería y poda", JARDINERIA,
            "Mantenimiento de jardines, poda de árboles y diseño de áreas verdes.",
            "Col. San Rafael", "Calle Juárez 88, Col. San Rafael", "3535550110",
            horario(LUN_VIE, 7, 15, 7 to 12),
            listOf(Triple("Mantenimiento de jardín", "Corte de pasto y deshierbe.", 300.0),
                Triple("Poda de árbol", "Árbol mediano, retiro de ramas.", 700.0)),
            listOf(5, 4, 5)),
        Ficha("n11", "Plomería Hermanos Pulido", "Plomería residencial", PLOMERIA,
            "Destapes, cambio de llaves y reparaciones rápidas a domicilio.",
            "Col. Revolución", "Calle 5 de Mayo 301, Col. Revolución", "3535550111",
            horario(LUN_VIE, 9, 18),
            listOf(Triple("Destape de drenaje", "Con equipo de guía.", 400.0),
                Triple("Cambio de llave mezcladora", "Mano de obra.", 250.0)),
            listOf(4, 3, 4, 4)),
        Ficha("n12", "Electro Cárdenas", "Electricista y paneles solares", ELECTRICIDAD,
            "Instalación de paneles solares, contactos y luminarias.",
            "Col. Lázaro Cárdenas", "Av. Emiliano Zapata 520, Col. Lázaro Cárdenas", "3535550112",
            horario(LUN_VIE, 9, 18, 9 to 13),
            listOf(Triple("Instalación de luminaria", "Por pieza.", 200.0),
                Triple("Sistema solar residencial", "Cotización con visita.", 35000.0)),
            listOf(5, 4, 4, 5)),
        Ficha("n13", "Carpintería La Ciénega", "Puertas y closets", CARPINTERIA,
            "Puertas de tambor y madera sólida, closets y cocinas.",
            "Col. Centro", "Calle Nicolás Bravo 64, Col. Centro", "3535550113",
            horario(LUN_VIE, 9, 18, 9 to 14),
            listOf(Triple("Puerta de tambor", "Medida estándar, instalada.", 2200.0),
                Triple("Closet de melamina", "Por metro lineal.", 3200.0)),
            listOf(4, 4)),
        Ficha("n14", "Taller Mecánico Don Beto", "Mecánica general", MECANICA,
            "Motor, transmisión y sistema eléctrico automotriz.",
            "Col. San Rafael", "Calle Abasolo 45, Col. San Rafael", "3535550114",
            horario(LUN_VIE, 8, 18, 8 to 14),
            listOf(Triple("Diagnóstico general", "Revisión de motor y suspensión.", 300.0),
                Triple("Cambio de aceite", "Incluye filtro.", 650.0)),
            listOf(4, 5, 3)),
        Ficha("n15", "Foto Estudio Jiquilpan", "Fotografía de estudio", FOTOGRAFIA,
            "Fotos para documentos, retratos y productos.",
            "Centro Histórico", "Av. Lázaro Cárdenas 9, Centro Histórico", "3535550115",
            horario(LUN_VIE, 10, 19, 10 to 14),
            listOf(Triple("Fotos para documentos", "6 fotos tamaño infantil.", 80.0),
                Triple("Sesión de retrato", "30 minutos, 10 fotos editadas.", 600.0)),
            listOf(4, 5)),
        Ficha("n16", "Uñas y Pestañas Valeria", "Uñas, pestañas y cejas", BELLEZA,
            "Uñas acrílicas, gelish, extensión de pestañas y diseño de cejas.",
            "Col. Las Lomas", "Calle Ocampo 120, Col. Las Lomas", "3535550116",
            horario(LUN_VIE, 11, 20, 10 to 18),
            listOf(Triple("Uñas acrílicas", "Juego completo con diseño sencillo.", 350.0),
                Triple("Extensión de pestañas", "Pelo por pelo.", 500.0)),
            listOf(5, 5, 4))
    )

    private val nombresResenas = listOf("Ana", "Luis", "Rosa", "Jorge", "Carmen", "Miguel", "Sofía")
    private val comentarios = mapOf(
        5 to "Muy cumplido y puntual, lo recomiendo.",
        4 to "Buen trabajo, solo tardó un poco más de lo acordado.",
        3 to "El trabajo quedó bien pero fue difícil coordinar la cita."
    )

    fun crear(): Contenido {
        val cliente = Usuario("u-cliente", "Ana López", CLIENTE_DEMO, "3535550001", CONTRASENA_DEMO, Rol.CLIENTE)
        val dueno = Usuario("u-dueno", "José Martínez", NEGOCIO_DEMO, "3535550101", CONTRASENA_DEMO, Rol.NEGOCIO)

        val negocios = fichas.map { f ->
            Negocio(
                id = f.id, nombre = f.nombre, oficio = f.oficio, categoria = f.categoria,
                descripcion = f.descripcion, direccion = f.direccion, telefono = f.telefono,
                whatsapp = f.telefono, fotos = emptyList(), horarios = f.horarios,
                idDueno = if (f.id == "n01") dueno.id else "dueno-${f.id}", colonia = f.colonia
            )
        }
        val servicios = fichas.flatMap { f ->
            f.servicios.mapIndexed { i, (nombre, descripcion, precio) ->
                Servicio("${f.id}-s${i + 1}", f.id, nombre, descripcion, precio, esPrecioDesde = true)
            }
        }
        val resenas = fichas.flatMap { f ->
            f.estrellas.mapIndexed { i, estrellas ->
                Resena(
                    id = "${f.id}-r${i + 1}", idNegocio = f.id, idUsuario = "vecino-${i + 1}",
                    nombreUsuario = nombresResenas[i % nombresResenas.size], estrellas = estrellas,
                    comentario = comentarios[estrellas] ?: "", fecha = BASE_FECHA - (i + 1) * 3 * DIA
                )
            }
        }
        val vacantes = listOf(
            Vacante("n01-v1", "n01", "Ayudante de carpintería", "Apoyo en corte, lijado y armado de muebles.",
                "Lunes a viernes, 9:00 a 18:00", 2200.0, listOf("Mayor de edad", "Ganas de aprender"), fecha = BASE_FECHA - 2 * DIA),
            Vacante("n02-v1", "n02", "Electricista con experiencia", "Instalaciones residenciales y guardias de fin de semana.",
                "Turnos rotativos", 3000.0, listOf("2 años de experiencia", "Herramienta propia"), fecha = BASE_FECHA - 5 * DIA),
            Vacante("n08-v1", "n08", "Estilista", "Cortes y color para dama y caballero.",
                "Martes a sábado", null, listOf("Experiencia en colorimetría"), fecha = BASE_FECHA - DIA)
        )
        return Contenido(listOf(cliente, dueno), negocios, servicios, vacantes, resenas)
    }
}
