package movil.libronet.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import movil.libronet.modelo.Editorial
import movil.libronet.modelo.LibroNetRepository

class EditorialViewModel(): ViewModel() {
    var listaEditorial:List<Editorial> = emptyList()

    suspend fun rellenarListaEditorial(
        lambdaExito:() -> Unit = {},
        lambdaError:(String) -> Unit = {
                mensaje -> Log.d("Error", "Error al consultar las editoriales: ${mensaje}")
        }
    ){
        try {
            listaEditorial = LibroNetRepository().consultarTodasEditoriales()
            lambdaExito()
        }catch (e:Exception){
            lambdaError("Error al consultar la lista de editoriales: ${e.message}")
        }
    }

    suspend fun cargarEditoriales(){
        if (listaEditorial.isEmpty()){
            listaEditorial = LibroNetRepository().consultarTodasEditoriales()
        }
    }
}