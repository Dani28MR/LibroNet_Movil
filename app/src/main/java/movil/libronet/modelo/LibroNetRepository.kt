package movil.libronet.modelo

import LocalDateAdapter
import android.util.Log
import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

class LibroNetRepository {
    val libroNetApi:LibroNetApi
    init {
        val moshi = Moshi.Builder()
            .add(LocalDateAdapter())
            .build()

        libroNetApi = Retrofit.Builder()
            .baseUrl("http://34.237.81.204/ApiLibroNet/crud/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(LibroNetApi::class.java)
    }

    //LIBROS
    suspend fun consultarTodosLibros():List<Libro> =
        libroNetApi.consultarTodosLibros()
    suspend fun consultarLibro(id:Int):Libro =
        libroNetApi.consultarLibro(id)
    suspend fun actualizarLibro(libro : Libro) =
        libroNetApi.actualizarLibro(libro)

    //USUARIOS
    suspend fun consultarTodosUsuarios():List<Usuario> =
        libroNetApi.consultarTodosUsarios()
    suspend fun consultarUsuario(id:Int):Usuario =
        libroNetApi.consultarUsuario(id)

    suspend fun actualizarUsuario(usuario: Usuario) =
        libroNetApi.actualizarUsuario(usuario)


    //CATEGORIAS
    suspend fun consultarTodasCategorias():List<Categoria> =
        libroNetApi.consultarTodasCategorias()
    suspend fun consultarCategoria(id:Int): Categoria =
        libroNetApi.consultarCategoria(id)


    //EDITORIALES
    suspend fun consultarTodasEditoriales():List<Editorial> =
        libroNetApi.consultarTodasEditoriales()
    suspend fun consultarEditorial(id: Int): Editorial =
        libroNetApi.consultarEditorial(id)

    //AUTORES
    suspend fun consultarTodosAutores():List<Autor> =
        libroNetApi.consultarTodosAutores()
    suspend fun consultarAutor(id: Int): Autor =
        libroNetApi.consultarAutor(id)


    //LIBRO_AUTOR
    suspend fun consultarTodosLibrosAutor():List<Libro_Autor> =
        libroNetApi.consultarTodosLbrosAutor()


    //RESERVAS
    suspend fun consultarTodasReservas():List<Reserva> =
        libroNetApi.consultarTodasReservas()

    suspend fun tieneReservaActiva(idUsuario: Int, idLibro: Int): Boolean {
        return try {
            val respuesta = libroNetApi.tieneReservaActiva(idUsuario, idLibro)
            respuesta.tieneReserva
        } catch (e: Exception) {
            Log.e("Repository", "Error verificando reserva", e)
            false
        }
    }
    suspend fun insertarReserva(reserva: Reserva): Respuesta =
        libroNetApi.insertarReserva(reserva)


    suspend fun actualizarReserva(reserva:Reserva):Respuesta =
        libroNetApi.actualizarReserva(reserva)


}