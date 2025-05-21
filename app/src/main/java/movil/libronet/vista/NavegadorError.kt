package movil.libronet.vista

import androidx.navigation.NavController
import androidx.navigation.NavDirections

interface NavegadorError {
    val navController:NavController

    fun getFlecha(mensaje:String):NavDirections

    fun navegarError(mensaje: String){
        val flecha = getFlecha(mensaje)
        navController.navigate(flecha)
    }
}