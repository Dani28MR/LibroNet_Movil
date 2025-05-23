package movil.libronet.modelo

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

interface LibroNetApi {

    // LIBRO
    @GET("libro/leer.php")
    suspend fun consultarTodosLibros():List<Libro>

    @GET("libro/leer.php")
    suspend fun consultarLibro(
        @Query("idLibro") id:Int
    ):Libro


    //USUARIOS
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


    //CATEGORIAS
    @GET("categoria/leer.php")
    suspend fun consultarTodasCategorias():List<Categoria>

    @GET("categoria/leer.php")
    suspend fun consultarCategoria(
        @Query("idCategoria") id:Int
    ):Categoria


    //EDITORIALES

    @GET("editorial/leer.php")
    suspend fun consultarTodasEditoriales():List<Editorial>

    @GET("editorial/leer.php")
    suspend fun consultarEditorial(
        @Query("idEditorial") id:Int
    ):Editorial

    //AUTORES
    @GET("autor/leer.php")
    suspend fun consultarTodosAutores():List<Autor>

    @GET("autor/leer.php")
    suspend fun consultarAutor(
        @Query("idEditorial") id:Int
    ):Autor


    //RESERVAS
    @GET("reserva/leer.php")
    suspend fun consultarTodasReservas(): List<Reserva>

    @GET("reserva/leer.php")
    suspend fun consultarReserva(
        @Query("idReserva") id: Int
    ): Reserva

    @PUT("reserva/actualizar.php")
    suspend fun actualizarReserva(
        @Body reserva: Reserva
    ): Respuesta
}