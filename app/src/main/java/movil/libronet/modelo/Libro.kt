package movil.libronet.modelo

import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class Libro(
    val idLibro:Int,
    val titulo: String,
    val descripcion:String,
    val ISBN: String,
    val totalCopias: Int,
    val copiasDisponibles: Int,
    val idCategoria: Int,
    val idEditorial: Int
):Serializable