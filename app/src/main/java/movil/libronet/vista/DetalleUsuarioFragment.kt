package movil.libronet.vista

import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import movil.libronet.R
import movil.libronet.databinding.FragmentDetalleLibrosBinding
import movil.libronet.databinding.FragmentDetalleUsuarioBinding
import movil.libronet.modelo.LibroNetRepository
import movil.libronet.modelo.Usuario
import movil.libronet.viewmodel.DetalleLibrosViewModel
import movil.libronet.viewmodel.DetalleUsuarioViewModel
import movil.libronet.viewmodel.SharedViewModel
import org.mindrot.jbcrypt.BCrypt
import java.util.regex.Pattern

class DetalleUsuarioFragment : Fragment(), NavegadorError {
    lateinit var usuario: Usuario
    lateinit var viewModel: DetalleUsuarioViewModel
    var _binding: FragmentDetalleUsuarioBinding?=null
    val binding: FragmentDetalleUsuarioBinding
        get() = checkNotNull(_binding)

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inicializarBinding(inflater,container)
        inicializarViewModel()
        inicializarInterfaz()
        configurarBotones()
        configurarValidacionTiempoReal()

        return binding.root
    }

    private fun configurarBotones(){
        binding.btnGuardar.setOnClickListener {
            actualizarUsuario()
        }
    }

    private fun actualizarUsuario() {
        if (!validarCampos()) return

        val passwordTexto = binding.txtPassword.text.toString()
        val hashedPassword = if (passwordTexto.isNotBlank()){
            BCrypt.hashpw(passwordTexto, BCrypt.gensalt())
        }else{
            usuario.contraseña
        }

        val nuevoUsuario = Usuario(
            idUsuario = usuario.idUsuario,
            nombreUsuario = binding.txtNombre.text.toString(),
            apellidoUsuario = binding.txtApellidos.text.toString(),
            imagenUsuario = usuario.imagenUsuario,
            email = usuario.email,
            contraseña = hashedPassword,
            telefono = binding.txtTelefono.text.toString(),
            direccion = binding.txtDireccion.text.toString(),
            rol = usuario.rol
        )

        lifecycleScope.launch {
            try {
                LibroNetRepository().actualizarUsuario(nuevoUsuario)
                Toast.makeText(requireContext(), "Usuario actualizado correctamente", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                navegarError("Error al actualizar usuario: ${e.message}")
            }
        }
    }

    private fun configurarValidacionTiempoReal() {
        val campos = listOf(
            binding.txtNombre,
            binding.txtApellidos,
            binding.txtEmail,
            binding.txtTelefono,
            binding.txtDireccion,
            binding.txtPassword,
            binding.txtConfirmacionPassword
        )

        campos.forEach { campo ->
            campo.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    when (campo) {
                        binding.txtNombre -> campo.validarObligatorio("El nombre es obligatorio")
                        binding.txtApellidos -> campo.validarObligatorio("Los apellidos son obligatorios")
                        binding.txtEmail -> campo.validarEmail("Email inválido")
                        binding.txtTelefono -> campo.validarTelefono("Teléfono inválido")
                        binding.txtDireccion -> campo.validarObligatorio("Dirección obligatoria")
                        binding.txtPassword -> {
                            campo.validarPassword("Mínimo 6 caracteres con mayúscula")
                            binding.txtConfirmacionPassword.validarConfirmacion(
                                binding.txtPassword,
                                "Las contraseñas no coinciden"
                            )
                        }
                        binding.txtConfirmacionPassword -> campo.validarConfirmacion(
                            binding.txtPassword,
                            "Las contraseñas no coinciden"
                        )
                    }
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }
    }

    private fun inicializarBinding(inflater: LayoutInflater,container: ViewGroup?){
        _binding = FragmentDetalleUsuarioBinding.inflate(inflater,container,false)
    }
    private fun inicializarViewModel(){
        viewModel = ViewModelProvider(this).get(DetalleUsuarioViewModel::class.java)
    }

    fun inicializarInterfaz(){
        lifecycleScope.launch {
            usuario = LibroNetRepository().consultarUsuario(sharedViewModel.getIdUsuario())

            binding.txtNombre.setText(usuario.nombreUsuario)
            binding.txtApellidos.setText(usuario.apellidoUsuario)
            binding.txtEmail.setText(usuario.email)
            binding.txtRol.setText(usuario.rol)
            binding.txtTelefono.setText(usuario.telefono)
            binding.txtDireccion.setText(usuario.direccion)
            binding.txtPassword.setText(usuario.contraseña)
            binding.txtConfirmacionPassword.setText(usuario.contraseña)
            try {
                val imageBytes = Base64.decode(usuario.imagenUsuario, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.size)
                binding.imgUsuario.setImageBitmap(bitmap)
            }catch (e:Exception){
                navegarError("Error en la lectura de la imágen")
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override val navController: NavController
        get() = findNavController()


    override fun getFlecha(mensaje: String): NavDirections =
        DetalleUsuarioFragmentDirections.actionDetalleUsuarioFragmentToErrorFragment(mensaje)

    private fun validarCampos(): Boolean {
        var valido = true

        // Validar nombre
        valido = binding.txtNombre.validarObligatorio("El nombre es obligatorio") && valido

        // Validar apellidos
        valido = binding.txtApellidos.validarObligatorio("Los apellidos son obligatorios") && valido

        // Validar email
        valido = binding.txtEmail.validarEmail("Introduce un email válido (ejemplo: usuario@correo.com)") && valido

        // Validar teléfono
        valido = binding.txtTelefono.validarTelefono("El teléfono es obligatorio y debe tener 9 dígitos") && valido

        // Validar dirección
        valido = binding.txtDireccion.validarObligatorio("La dirección es obligatoria") && valido

        // Validar contraseña
        valido = binding.txtPassword.validarPassword("La contraseña debe tener al menos 6 caracteres y una mayúscula") && valido

        // Validar confirmación
        valido = binding.txtConfirmacionPassword.validarConfirmacion(
            binding.txtPassword,
            "La confirmación de contraseña es obligatoria"
        ) && valido

        return valido
    }

    fun EditText.validarObligatorio(mensajeError: String): Boolean {
        if (text.isNullOrBlank()) {
            error = mensajeError
            return false
        }
        error = null
        return true
    }

    fun EditText.validarEmail(mensajeError: String): Boolean {
        val pattern = Pattern.compile("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")
        if (!text.isNullOrBlank() && pattern.matcher(text).matches()) {
            error = null
            return true
        }
        error = mensajeError
        return false
    }

    fun EditText.validarTelefono(mensajeError: String): Boolean {
        if (!text.isNullOrBlank() && text.length == 9 && text.matches("\\d+".toRegex())) {
            error = null
            return true
        }
        error = mensajeError
        return false
    }

    fun EditText.validarPassword(mensajeError: String): Boolean {
        if (!text.isNullOrBlank() && text.length >= 6 && text.any { it.isUpperCase() }) {
            error = null
            return true
        }
        error = mensajeError
        return false
    }

    fun EditText.validarConfirmacion(password: EditText, mensajeError: String): Boolean {
        return when {
            text.isNullOrBlank() -> {
                error = mensajeError
                false
            }
            text.toString() != password.text.toString() -> {
                error = "Las contraseñas no coinciden"
                false
            }
            else -> {
                error = null
                true
            }
        }
    }
}