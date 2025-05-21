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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import movil.libronet.R
import movil.libronet.databinding.FragmentErrorBinding
import movil.libronet.databinding.FragmentLibrosBinding
import movil.libronet.modelo.LibroNetRepository
import movil.libronet.viewmodel.DetalleLibrosViewModel
import movil.libronet.viewmodel.LibrosViewModel

class LibrosFragment : Fragment(), NavegadorError{
    lateinit var viewModel: LibrosViewModel
    var _binding: FragmentLibrosBinding?=null
    val binding: FragmentLibrosBinding
        get() = checkNotNull(_binding)

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
        inicializarViewModel()
        inicializarInterfaz()
        test()
        return binding.root
    }

    private fun iniciarBinding(inflater: LayoutInflater,container: ViewGroup?){
        _binding = FragmentLibrosBinding.inflate(inflater,container,false)
    }
    private fun inicializarViewModel(){
        viewModel = ViewModelProvider(this).get(LibrosViewModel::class.java)
    }

    fun test(){
        lifecycleScope.launch {
            viewModel.rellenarListaLibros(
                {Log.d("test", "Lista de libros cargada: ${viewModel.listaLibros}")},
                { error -> Log.d("test", "Error al cargar la lista de libros: ${error}")}
            )

        }
    }

    fun mostrarToolbar(b:Boolean){
        val mainActivity = activity as MainActivity
        mainActivity.binding.materialToolbar.visibility =
            if (b) View.VISIBLE else View.GONE
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun inicializarInterfaz(){
        if (viewModel.mostrarSplashScreen){
            inicializarSplashScreen()
        }else{
            inicializarInterfazPrincipal()
        }
    }

    fun inicializarSplashScreen(){
        lifecycleScope.launch {
            mostrarToolbar(false)
            delay(2000)
            viewModel.rellenarListaLibros(
                lambdaExito = { inicializarInterfazPrincipal()},
                lambdaError = { m -> navegarError(m)}
            )
        }
    }
    fun inicializarInterfazPrincipal(){
        mostrarToolbar(true)
        binding.capa1.visibility = View.GONE
        binding.capa2.visibility = View.VISIBLE
        inicializarRecycleView()
        viewModel.mostrarSplashScreen = false
    }

    private fun inicializarRecycleView(){
        val adapter = LibroAdapter(viewModel.listaLibros){ holder ->
            Toast.makeText(
                requireContext(),
                "Seleccionado ${holder.libro.titulo}",
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.lstLibros.adapter = adapter
    }

}