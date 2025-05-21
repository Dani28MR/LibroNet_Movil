package movil.libronet.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import movil.libronet.modelo.Libro
import movil.libronet.modelo.LibroNetRepository

class LibrosViewModel(): ViewModel() {
    var listaLibros:List<Libro> = emptyList()
    var mostrarSplashScreen:Boolean = true
    suspend fun rellenarListaLibros(
        lambdaExito:() -> Unit = {},
        lambdaError:(String) -> Unit = {
            mensaje -> Log.d("Error", "Error al consultar los libros: ${mensaje}")
        }
    ){
        try {
            listaLibros = LibroNetRepository().consultarTodosLibros()
            lambdaExito()
        }catch (e:Exception){
            lambdaError("Error al consultar la lista de libros: ${e.message}")
        }
    }

    suspend fun cargarLibros(){
        if (listaLibros.isEmpty()){
            listaLibros = LibroNetRepository().consultarTodosLibros()
        }
    }
}