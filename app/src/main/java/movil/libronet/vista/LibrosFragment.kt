package movil.libronet.vista

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import movil.libronet.R
import movil.libronet.databinding.FragmentErrorBinding
import movil.libronet.databinding.FragmentLibrosBinding
import movil.libronet.modelo.LibroNetRepository
import movil.libronet.viewmodel.DetalleLibrosViewModel
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
    }



}