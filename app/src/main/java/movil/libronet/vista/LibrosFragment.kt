package movil.libronet.vista

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import movil.libronet.databinding.FragmentLibrosBinding
import movil.libronet.viewmodel.LibrosViewModel
import kotlin.properties.Delegates


class LibrosFragment : Fragment(), NavegadorError{
    lateinit var viewModel: LibrosViewModel
    var _binding: FragmentLibrosBinding?=null
    val binding: FragmentLibrosBinding
        get() = checkNotNull(_binding)

    var idUser by Delegates.notNull<Int>()

    override val navController: NavController
        get() = findNavController()

    override fun getFlecha(mensaje: String): NavDirections =
        LibrosFragmentDirections.actionLibrosFragmentToErrorFragment(mensaje)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        iniciarBinding(inflater,container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inicializarViewModel()
    }

    private fun iniciarBinding(inflater: LayoutInflater,container: ViewGroup?){
        _binding = FragmentLibrosBinding.inflate(inflater,container,false)
    }
    private fun inicializarViewModel(){
        viewModel = ViewModelProvider(this).get(LibrosViewModel::class.java)
        lifecycleScope.launch {
            viewModel.rellenarListaLibros (
                lambdaExito = { inicializarRecycleView()},
                lambdaError = { m -> navegarError(m)}
            )
        }
    }

    private fun actualizarEstadoVacio(estaVacio: Boolean) {
        binding.tvEmptyState.isVisible = estaVacio
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    private fun inicializarRecycleView(){
        val adapter = LibroAdapter(viewModel.listaLibros){ holder ->
            val flecha = LibrosFragmentDirections.
            actionLibrosFragmentToDetalleLibrosFragment(holder.libro)
            findNavController().navigate(flecha)
        }
        binding.lstLibros.adapter = adapter
        actualizarEstadoVacio(viewModel.listaLibros.isEmpty())
    }



}