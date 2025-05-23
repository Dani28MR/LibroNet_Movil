package movil.libronet.modelo

import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class Autor(
    val idAutor: Int,
    val nombreAutor: String,
    val apellidoAutor: String,
    val nacionalidad: String,
    val imagenAutor: String,
    val biografia: String
):Serializable