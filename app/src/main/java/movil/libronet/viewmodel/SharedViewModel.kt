package movil.libronet.viewmodel

import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private var _idUsuarioLogeado:Int ?= null


    fun getIdUsuario(): Int {
        return _idUsuarioLogeado
            ?: throw IllegalStateException("ID de usuario no disponible")
    }

    fun setIdUsuario(id:Int){
        _idUsuarioLogeado  = id
    }

    fun clear(){
        _idUsuarioLogeado = null
    }

}