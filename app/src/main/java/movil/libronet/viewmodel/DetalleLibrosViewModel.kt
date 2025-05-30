package movil.libronet.viewmodel

import androidx.lifecycle.ViewModel
import movil.libronet.modelo.LibroNetRepository
import movil.libronet.modelo.Reserva
import movil.libronet.modelo.Respuesta

class DetalleLibrosViewModel(): ViewModel() {
    suspend fun tieneReservaActiva(idUsuario: Int, idLibro: Int): Boolean {
        return LibroNetRepository().tieneReservaActiva(idUsuario, idLibro)
    }

    suspend fun crearReserva(reserva: Reserva): Respuesta {
        return LibroNetRepository().insertarReserva(reserva)
    }
}