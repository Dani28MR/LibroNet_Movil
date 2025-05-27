package movil.libronet.vista

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        return binding.root
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

}