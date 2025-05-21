package movil.libronet.vista

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import movil.libronet.R
import movil.libronet.databinding.FragmentErrorBinding

class ErrorFragment : Fragment() {

    var _binding:FragmentErrorBinding?=null
    val binding:FragmentErrorBinding
        get() = checkNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        iniciarBinding(inflater,container)
        inicializarMensajeError()

        return binding.root
    }

    private fun iniciarBinding(inflater: LayoutInflater,container: ViewGroup?){
        _binding = FragmentErrorBinding.inflate(inflater,container,false)
    }

    private fun inicializarMensajeError(){
        val mensaje = ErrorFragmentArgs.fromBundle(requireArguments()).mensajeError
        binding.txtError.text = mensaje
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}