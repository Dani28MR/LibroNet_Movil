package movil.libronet.modelo

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

interface LibroNetApi {
    @GET("libro/leer.php")
    suspend fun consultarTodosLibros():List<Libro>

    @GET("libro/leer.php")
    suspend fun consultarLibro(
        @Query("idLibro") id:Int
    ):Libro

    @GET("usuario/leer.php")
    suspend fun consultarTodosUsarios():List<Usuario>

    @GET("usuario/leer.php")
    suspend fun consultarUsuario(
        @Query("idUsuario") id:Int
    ):Usuario

    @PUT("usuario/actualizar.php")
    suspend fun actualizarUsuario(
        @Body usuario: Usuario
    ):Respuesta
}