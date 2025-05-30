package movil.libronet.modelo

import com.squareup.moshi.JsonClass
import java.io.Serializable


@JsonClass(generateAdapter = true)
data class Usuario(
    val idUsuario:Int,
    var nombreUsuario: String,
    val apellidoUsuario:String,
    val imagenUsuario: String,
    val email: String,
    val contraseña: String,
    val telefono: String,
    val direccion: String,
    val rol: String
):Serializable