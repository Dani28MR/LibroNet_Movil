package movil.libronet.viewmodel

import androidx.lifecycle.ViewModel
import movil.libronet.modelo.EstadoReserva
import movil.libronet.modelo.Libro
import movil.libronet.modelo.LibroNetRepository
import movil.libronet.modelo.Reserva
import movil.libronet.modelo.Respuesta
import org.json.JSONObject

class DetalleLibrosViewModel(): ViewModel() {
    suspend fun tieneReservaActiva(idUsuario: Int, idLibro: Int): Boolean {
        return LibroNetRepository().tieneReservaActiva(idUsuario, idLibro)
    }

    suspend fun crearReserva(reserva: Reserva): Respuesta {
        return LibroNetRepository().insertarReserva(reserva)
    }

    suspend fun actualizarLibro(libro: Libro): Respuesta {
        return LibroNetRepository().actualizarLibro(libro)
    }

    suspend fun cancelarReserva(reserva: Reserva): Pair<Respuesta, Libro> {
        val reservaCancelada = reserva.copy(estado = EstadoReserva.INACTIVO)
        val respuestaReserva = LibroNetRepository().actualizarReserva(reservaCancelada)

        if (respuestaReserva.mensaje.contains("correctamente", ignoreCase = true)) {
            val libroOriginal = LibroNetRepository().consultarLibro(reserva.idLibro)

            val libroActualizado = libroOriginal.copy(
                copiasDisponibles = libroOriginal.copiasDisponibles + 1
            )

            val respuestaActualizacion = actualizarLibro(libroActualizado)

            if (respuestaActualizacion.mensaje.contains("correctamente", ignoreCase = true)) {
                return Pair(respuestaReserva, libroActualizado)
            }
            return Pair(respuestaReserva, libroOriginal)
        }
        return Pair(respuestaReserva, LibroNetRepository().consultarLibro(reserva.idLibro))
    }

}