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
import movil.libronet.databinding.FragmentDetalleLibrosBinding
import movil.libronet.databinding.FragmentLibrosBinding
import movil.libronet.modelo.Libro
import movil.libronet.viewmodel.DetalleLibrosViewModel

class DetalleLibrosFragment : Fragment(), NavegadorError {
    var libro: Libro ?= null
    lateinit var viewModel:DetalleLibrosViewModel
    var _binding: FragmentDetalleLibrosBinding?=null
    val binding: FragmentDetalleLibrosBinding
        get() = checkNotNull(_binding)

    override val navController: NavController
        get() = findNavController()

    override fun getFlecha(mensaje: String): NavDirections =
        DetalleLibrosFragmentDirections.actionDetalleLibrosFragmentToErrorFragment(mensaje)


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

    fun inicializarInterfaz(){
        val l = DetalleLibrosFragmentArgs.fromBundle(requireArguments()).libroSeleccionado
        libro = l
        binding.txtTitulo.setText(l.titulo)
        binding.txtISBN.setText(l.ISBN)
        binding.txtDescripcion.setText(l.descripcion)
        binding.txtTotalCopias.setText(l.totalCopias.toString())
        binding.txtTotalCopiasDisponibles.setText(l.copiasDisponibles.toString())
    }


    private fun iniciarBinding(inflater: LayoutInflater,container: ViewGroup?){
        _binding = FragmentDetalleLibrosBinding.inflate(inflater,container,false)
    }
    private fun inicializarViewModel(){
        viewModel = ViewModelProvider(this).get(DetalleLibrosViewModel::class.java)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}