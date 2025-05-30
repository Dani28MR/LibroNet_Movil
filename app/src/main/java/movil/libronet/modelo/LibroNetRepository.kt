package movil.libronet.modelo

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

class LibroNetRepository {
    val libroNetApi:LibroNetApi
    init {
        libroNetApi = Retrofit.Builder()
            .baseUrl("http://10.0.2.2/ApiLibroNet/crud/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
    }

    //LIBROS
    suspend fun consultarTodosLibros():List<Libro> =
        libroNetApi.consultarTodosLibros()
    suspend fun consultarLibro(id:Int):Libro =
        libroNetApi.consultarLibro(id)

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
    suspend fun insertarReserva(reserva: Reserva): Respuesta {
        return libroNetApi.insertarReserva(reserva)
    }


}