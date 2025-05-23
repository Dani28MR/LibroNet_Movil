package movil.libronet.vista

import android.os.Bundle
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
import movil.libronet.R
import movil.libronet.databinding.FragmentCategoriaBinding
import movil.libronet.databinding.FragmentEditorialBinding
import movil.libronet.viewmodel.CategoriaViewModel
import movil.libronet.viewmodel.EditorialViewModel

class EditorialFragment : Fragment(), NavegadorError {
    lateinit var viewModel: EditorialViewModel
    var _binding: FragmentEditorialBinding ?= null
    val binding: FragmentEditorialBinding
        get() = checkNotNull(_binding)

    override val navController: NavController
        get() = findNavController()

    override fun getFlecha(mensaje: String): NavDirections =
        EditorialFragmentDirections.actionEditorialFragmentToErrorFragment(mensaje)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        iniciarBinding(inflater,container)
        inicializarViewModel()
        return binding.root
    }

    private fun iniciarBinding(inflater: LayoutInflater,container: ViewGroup?){
        _binding = FragmentEditorialBinding.inflate(inflater,container,false)
    }

    fun inicializarViewModel(){
        viewModel = ViewModelProvider(this).get(EditorialViewModel::class.java)
        lifecycleScope.launch {
            viewModel.rellenarListaEditorial(
                lambdaExito = { inicializarRecyclerView() },
                lambdaError = { m -> navegarError(m)}
            )
        }
    }

    fun inicializarRecyclerView(){
        val adapter = EditorialAdapter(viewModel.listaEditorial){ holder ->
            Toast.makeText(
                requireContext(),
                "Seleccionado ${holder.editorial.nombreEditorial}",
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.lstEditoriales.adapter = adapter
    }

}