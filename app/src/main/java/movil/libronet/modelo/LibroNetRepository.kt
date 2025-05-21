package movil.libronet.modelo

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

    suspend fun consultarTodosLibros():List<Libro> =
        libroNetApi.consultarTodosLibros()
    suspend fun consultarLibro(id:Int):Libro =
        libroNetApi.consultarLibro(id)

    suspend fun consultarTodosUsuarios():List<Usuario> =
        libroNetApi.consultarTodosUsarios()
    suspend fun consultarUsuario(id:Int):Usuario =
        libroNetApi.consultarUsuario(id)

    suspend fun actualizarUsuario(usuario: Usuario) =
        libroNetApi.actualizarUsuario(usuario)
}