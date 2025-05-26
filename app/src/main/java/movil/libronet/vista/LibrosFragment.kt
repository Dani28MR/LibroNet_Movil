package movil.libronet.vista

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
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
        configurarNavigationDrawer()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inicializarViewModel()
        inicializarInterfaz()
    }

    private fun iniciarBinding(inflater: LayoutInflater,container: ViewGroup?){
        _binding = FragmentLibrosBinding.inflate(inflater,container,false)
    }
    private fun inicializarViewModel(){
        viewModel = ViewModelProvider(this).get(LibrosViewModel::class.java)
    }

    fun test() {
        lifecycleScope.launch {
            val editoriales = LibroNetRepository().consultarTodasEditoriales()
            editoriales.forEach { categoria ->
                Log.d("pruebaa", categoria.toString())
            }

            val autores = LibroNetRepository().consultarTodosAutores()
            autores.forEach { autor ->
                Log.d("pruebaa", autor.toString())
            }
        }
    }
    private fun configurarNavigationDrawer(){
        NavigationUI.setupWithNavController(binding.navigationView, navController)

    }

    fun mostrarToolbar(b:Boolean){
        val mainActivity = activity as MainActivity
        mainActivity.binding.materialToolbar.visibility =
            if (b) View.VISIBLE else View.GONE


        val navController = navController
        val configuracion = AppBarConfiguration
            .Builder(setOf(R.id.librosFragment,R.id.autorFragment,R.id.editorialFragment,R.id.categoriaFragment))
            .setOpenableLayout(binding.navigationDrawer)
            .build()
        NavigationUI.setupWithNavController(binding.materialToolbar,navController,configuracion)

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
            val flecha = LibrosFragmentDirections.
            actionLibrosFragmentToDetalleLibrosFragment(holder.libro)
            findNavController().navigate(flecha)
        }
        binding.lstLibros.adapter = adapter
    }

}