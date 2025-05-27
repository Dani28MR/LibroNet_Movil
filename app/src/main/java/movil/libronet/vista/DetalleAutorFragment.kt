package movil.libronet.vista

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import movil.libronet.databinding.FragmentDetalleAutorBinding
import movil.libronet.modelo.Autor
import movil.libronet.viewmodel.DetalleAutorViewModel

class DetalleAutorFragment : Fragment(), NavegadorError {
    var autor:Autor ?= null
    lateinit var viewModel:DetalleAutorViewModel
    var _binding: FragmentDetalleAutorBinding ?= null
    val binding: FragmentDetalleAutorBinding
        get() = checkNotNull(_binding)

    override val navController: NavController
        get() = findNavController()

    override fun getFlecha(mensaje: String): NavDirections =
        DetalleAutorFragmentDirections.actionDetalleAutorFragmentToErrorFragment(mensaje)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        iniciarBinding(inflater,container)
        inicializarViewModel()
        inicializarInterfaz()

        return binding.root
    }

    private fun iniciarBinding(inflater: LayoutInflater,container: ViewGroup?){
        _binding = FragmentDetalleAutorBinding.inflate(inflater,container,false)

    }

    private fun inicializarViewModel(){
        viewModel = ViewModelProvider(this).get(DetalleAutorViewModel::class.java)
    }

    fun inicializarInterfaz(){
        val a = DetalleAutorFragmentArgs.fromBundle(requireArguments()).autorSeleccionado
        autor = a
        binding.txtNombre.text = a.nombreAutor
        binding.txtApellidos.setText(a.apellidoAutor)
        binding.txtBiografia.setText(a.biografia)
        binding.txtNacionalidad.setText(a.nacionalidad)
        try {
            val imageBytes = Base64.decode(a.imagenAutor, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.size)
            binding.imgAutor.setImageBitmap(bitmap)
        }catch (e:Exception){
            navegarError("Error en la lectura de la imágen")
        }
    }


}