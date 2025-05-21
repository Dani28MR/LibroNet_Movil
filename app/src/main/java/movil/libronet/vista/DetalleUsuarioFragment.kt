package movil.libronet.vista

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import movil.libronet.R
import movil.libronet.databinding.FragmentDetalleLibrosBinding
import movil.libronet.databinding.FragmentDetalleUsuarioBinding
import movil.libronet.viewmodel.DetalleLibrosViewModel
import movil.libronet.viewmodel.DetalleUsuarioViewModel

class DetalleUsuarioFragment : Fragment() {
    lateinit var viewModel: DetalleUsuarioViewModel
    var _binding: FragmentDetalleUsuarioBinding?=null
    val binding: FragmentDetalleUsuarioBinding
        get() = checkNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inicializarBinding(inflater,container)
        inicializarViewModel()

        return binding.root
    }

    private fun inicializarBinding(inflater: LayoutInflater,container: ViewGroup?){
        _binding = FragmentDetalleUsuarioBinding.inflate(inflater,container,false)
    }
    private fun inicializarViewModel(){
        viewModel = ViewModelProvider(this).get(DetalleUsuarioViewModel::class.java)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}