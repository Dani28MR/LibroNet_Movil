package movil.libronet.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import movil.libronet.modelo.LibroNetRepository
import movil.libronet.modelo.Usuario
import org.mindrot.jbcrypt.BCrypt

sealed class LoginResult {
    data class Success(val usuario: Usuario): LoginResult()
    data class Error(val mensaje: String): LoginResult()
    object Loading : LoginResult()
}

class LoginViewModel():ViewModel () {
    private val _loginState = MutableStateFlow<LoginResult>(LoginResult.Loading)
    val loginState: StateFlow<LoginResult> get() = _loginState

    var listaUsuarios:List<Usuario> = emptyList()

    fun cargarUsuarios(
        lambdaExito: () -> Unit = {},
        lambdaError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                listaUsuarios = LibroNetRepository().consultarTodosUsuarios()
                Log.d("Login", "Usuarios cargados: ${listaUsuarios.size}") // <- Log aquí
                Log.d("Login", "Ejemplo de usuario: ${listaUsuarios.firstOrNull()?.email}")
                lambdaExito()
            } catch (e: Exception) {
                Log.e("Login", "Error al cargar usuarios", e)
                lambdaError("Error al cargar usuarios: ${e.message}")
            }
        }
    }

    fun validarLogin(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginResult.Loading
            try {
                if (listaUsuarios.isEmpty()) {
                    Log.d("Login", "Recargando usuarios...")
                    listaUsuarios = LibroNetRepository().consultarTodosUsuarios()
                }
                Log.d("Login", "Emails en lista: ${listaUsuarios.joinToString { it.email }}")
                val usuarioEncontrado = listaUsuarios.find { it.email == email }
                if (usuarioEncontrado == null) {

                    Log.d("Login", "Usuario no encontrado. Emails disponibles: ${listaUsuarios.map { it.email }}")
                    _loginState.value = LoginResult.Error("Usuario no encontrado")

                } else {
                    Log.d("Login", "Usuario encontrado: ${usuarioEncontrado.email}")

                    val passEncriptada = usuarioEncontrado.contraseña
                    Log.d("Login", "Hash almacenado: $passEncriptada")
                    Log.d("Login", "Contraseña ingresada: $password")

                    val esPasswordValido = BCrypt.checkpw(password.trim(), usuarioEncontrado.contraseña)
                    Log.d("Login", "Resultado verificación: $esPasswordValido")

                    if (esPasswordValido) {
                        _loginState.value = LoginResult.Success(usuarioEncontrado)
                    } else {
                        _loginState.value = LoginResult.Error("Contraseña incorrecta")
                    }
                }
            } catch (e: Exception) {
                _loginState.value = LoginResult.Error("Error al validar login: ${e.message}")
            }
        }
    }
}