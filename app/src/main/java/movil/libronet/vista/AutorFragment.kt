package movil.libronet.vista

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import movil.libronet.R
import movil.libronet.databinding.FragmentAutorBinding
import movil.libronet.viewmodel.AutorViewModel

class AutorFragment : Fragment(), NavegadorError {

    lateinit var viewModel:AutorViewModel
    var _binding:FragmentAutorBinding ?= null
    val binding: FragmentAutorBinding
        get() = checkNotNull(_binding)

    override val navController: NavController
        get() = findNavController()

    override fun getFlecha(mensaje: String): NavDirections =
        AutorFragmentDirections.actionAutorFragmentToErrorFragment(mensaje)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        iniciarBinding(inflater,container)
        inicializarViewModel()
        return binding.root
    }

    fun iniciarBinding(inflater: LayoutInflater,container: ViewGroup?){
        _binding = FragmentAutorBinding.inflate(inflater,container,false)
    }

    fun inicializarViewModel(){
        viewModel = ViewModelProvider(this).get(AutorViewModel::class.java)
        lifecycleScope.launch {
            viewModel.rellenarListaAutores(
                lambdaExito = { inicializarRecyclerView()},
                lambdaError = { m -> navegarError(m)}
            )
        }
    }

    fun inicializarRecyclerView(){
        val adapter = AutorAdapter(viewModel.listaAutores){ holder ->
            val flecha = AutorFragmentDirections.
                actionAutorFragmentToDetalleAutorFragment(holder.autor)
            findNavController().navigate(flecha)
        }
        binding.lstAutores.layoutManager = LinearLayoutManager(requireContext())
        binding.lstAutores.adapter = adapter
    }
}