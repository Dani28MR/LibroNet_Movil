package movil.libronet.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import movil.libronet.modelo.Autor
import movil.libronet.modelo.LibroNetRepository

class AutorViewModel(): ViewModel()  {
    var listaAutores:List<Autor> = emptyList()

    suspend fun rellenarListaAutores(
        lambdaExito:() -> Unit = {},
        lambdaError:(String) -> Unit = {
                mensaje -> Log.d("Error", "Error al consultar los autores: ${mensaje}")
        }
    ){
        try {
            listaAutores = LibroNetRepository().consultarTodosAutores()
            lambdaExito()

        }catch (e:Exception){
            lambdaError("Error al consultar la lista de autores")
        }

        suspend fun cargarAutores(){
            if (listaAutores.isEmpty()){
                listaAutores = LibroNetRepository().consultarTodosAutores()
            }
        }
    }
}