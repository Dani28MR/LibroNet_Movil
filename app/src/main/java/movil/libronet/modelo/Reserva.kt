package movil.libronet.modelo

import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class Reserva(
    val idReserva: Int,
    val idUsuario: Int,
    val idLibro: Int,
    val fechaReserva: String,
    val fechaExpiracion: String,
    val estado: String
):Serializable