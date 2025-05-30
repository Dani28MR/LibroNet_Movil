package movil.libronet.modelo

import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
class Libro_Autor (
    val idLibro: Int,
    val idAutor: Int
):Serializable