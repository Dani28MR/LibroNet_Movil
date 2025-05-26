package movil.libronet.vista

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import movil.libronet.R
import movil.libronet.databinding.FragmentDetalleEditorialBinding
import movil.libronet.databinding.FragmentDetalleLibrosBinding
import movil.libronet.modelo.Editorial
import movil.libronet.viewmodel.DetalleEditorialViewModel
import movil.libronet.viewmodel.DetalleLibrosViewModel

class DetalleEditorialFragment : Fragment(), NavegadorError {
    var editorial: Editorial?= null
    lateinit var viewModel:DetalleEditorialViewModel
    var _binding: FragmentDetalleEditorialBinding ?= null
    val binding: FragmentDetalleEditorialBinding
        get() = checkNotNull(_binding)

    override val navController: NavController
        get() = findNavController()

    override fun getFlecha (mensaje:String) :NavDirections =
        DetalleEditorialFragmentDirections.actionDetalleEditorialFragmentToErrorFragment(mensaje)

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
        _binding = FragmentDetalleEditorialBinding.inflate(inflater,container,false)
    }
    private fun inicializarViewModel(){
        viewModel = ViewModelProvider(this).get(DetalleEditorialViewModel::class.java)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun inicializarInterfaz(){
        val e = DetalleEditorialFragmentArgs.fromBundle(requireArguments()).editorialSeleccionada
        editorial = e
        binding.txtNombreEditorial.setText(e.nombreEditorial)
        binding.txtTelefonoEditorial.setText(e.telefono)
        binding.txtDireccionEditorial.setText(e.direccion)
    }

}