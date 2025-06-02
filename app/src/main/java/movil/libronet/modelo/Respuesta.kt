package movil.libronet.modelo

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Respuesta(
    val mensaje: String,
    val detalle: String? = null
)