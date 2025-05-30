package movil.libronet.modelo

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RespuestaReserva(
    val tieneReserva: Boolean
)