package ec.edu.tallerapi.controller

import ec.edu.tallerapi.model.Cliente
import ec.edu.tallerapi.model.NuevaReparacion
import ec.edu.tallerapi.model.Reparacion
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/reparaciones")
class ReparacionController {

    // Lista en memoria inicializada con dos reparaciones por defecto
    private val reparaciones = mutableListOf(
        Reparacion(1, "Bicicleta urbana", Cliente("Rosa Vera", "0961112233"), listOf("timbre"), 6.5, false),
        Reparacion(2, "Bicicleta de montaña", Cliente("Juan Pérez", "0998877665"), listOf("cadena", "frenos"), 25.0, true)
    )

    // GET /reparaciones - Devolver la lista completa en JSON
    @GetMapping
    fun obtenerTodas(): List<Reparacion> {
        return reparaciones
    }

    // GET /reparaciones/{id} - Devolver la reparación en JSON, o 404 Not Found si no existe
    @GetMapping("/{id}")
    fun obtenerPorId(@PathVariable id: Int): ResponseEntity<Reparacion> {
        val reparacion = reparaciones.find { it.id == id }
        return if (reparacion != null) {
            ResponseEntity.ok(reparacion)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    // POST /reparaciones - Recibir una NuevaReparacion, asignar id y responder 201 Created con cabecera Location
    @PostMapping
    fun crearReparacion(@RequestBody nueva: NuevaReparacion): ResponseEntity<Reparacion> {
        val nuevoId = (reparaciones.maxOfOrNull { it.id } ?: 0) + 1
        val reparacionCreada = Reparacion(
            id = nuevoId,
            bicicleta = nueva.bicicleta,
            cliente = nueva.cliente,
            repuestos = nueva.repuestos,
            costo = nueva.costo,
            lista = false
        )
        reparaciones.add(reparacionCreada)

        return ResponseEntity
            .created(URI.create("/reparaciones/$nuevoId"))
            .body(reparacionCreada)
    }
}