package movil.libronet.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import movil.libronet.modelo.Categoria
import movil.libronet.modelo.LibroNetRepository

class CategoriaViewModel(): ViewModel() {
    var listaCategoria:List<Categoria> = emptyList()

    suspend fun rellenarListaCategoria(
        lambdaExito:() -> Unit = {},
        lambdaError:(String) -> Unit = {
                mensaje -> Log.d("Error", "Error al consultar las categorías: ${mensaje}")
        }
    ){
        try {
            listaCategoria = LibroNetRepository().consultarTodasCategorias()
            lambdaExito()
        }catch (e:Exception){
            lambdaError("Error al consultar la lista de categorias: ${e.message}")
        }
    }

    suspend fun cargarCategorias(){
        if (listaCategoria.isEmpty()){
            listaCategoria = LibroNetRepository().consultarTodasCategorias()
        }
    }

}