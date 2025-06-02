package movil.libronet.modelo

import com.squareup.moshi.JsonClass
import java.io.Serializable
import java.time.LocalDate

@JsonClass(generateAdapter = true)
data class Reserva(
    val idReserva: Int,
    val idUsuario: Int,
    val idLibro: Int,
    val fechaReserva: LocalDate,
    val fechaExpiracion: LocalDate,
    val estado: EstadoReserva
):Serializable