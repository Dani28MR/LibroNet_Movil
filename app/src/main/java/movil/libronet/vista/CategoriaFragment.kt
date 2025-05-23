package movil.libronet.vista

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import movil.libronet.databinding.FragmentCategoriaBinding
import movil.libronet.databinding.FragmentDetalleLibrosBinding
import movil.libronet.modelo.LibroNetRepository
import movil.libronet.viewmodel.CategoriaViewModel

class CategoriaFragment : Fragment(), NavegadorError {
    lateinit var viewModel:CategoriaViewModel
    var _binding: FragmentCategoriaBinding ?= null
    val binding: FragmentCategoriaBinding
        get() = checkNotNull(_binding)

    override val navController: NavController
        get() = findNavController()

    override fun getFlecha(mensaje: String): NavDirections =
        CategoriaFragmentDirections.actionCategoriaFragmentToErrorFragment(mensaje)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        iniciarBinding(inflater,container)
        inicializarViewModel()
        return binding.root
    }

    private fun iniciarBinding(inflater: LayoutInflater, container: ViewGroup?){
        _binding = FragmentCategoriaBinding.inflate(inflater,container,false)
    }



    fun inicializarViewModel(){
        viewModel = ViewModelProvider(this).get(CategoriaViewModel::class.java)
        lifecycleScope.launch {
            viewModel.rellenarListaCategoria(
                lambdaExito = { inicializarRecyclerView() },
                lambdaError = { m -> navegarError(m)}
            )
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun inicializarRecyclerView(){
        val adapter = CategoriaAdapter(viewModel.listaCategoria){ holder ->
            Toast.makeText(
                requireContext(),
                "Seleccionado ${holder.categoria.nombreCategoria}",
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.lstCategorias.adapter = adapter
    }

}