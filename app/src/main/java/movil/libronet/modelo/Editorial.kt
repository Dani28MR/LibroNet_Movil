package movil.libronet.modelo

import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass (generateAdapter = true)
data class Editorial (
    val idEditorial: Int,
    val nombreEditorial: String,
    val direccion: String,
    val telefono: String
):Serializable