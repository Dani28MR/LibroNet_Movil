package movil.libronet.modelo

import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class Categoria(
    val idCategoria:Int,
    val nombreCategoria:String
): Serializable