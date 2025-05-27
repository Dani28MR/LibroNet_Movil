package movil.libronet.vista

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import movil.libronet.R
import movil.libronet.databinding.FragmentLoginBinding
import movil.libronet.viewmodel.LoginResult
import movil.libronet.viewmodel.LoginViewModel
import movil.libronet.viewmodel.SharedViewModel

class LoginFragment : Fragment(),NavegadorError {
    lateinit var viewModel:LoginViewModel

    var _binding:FragmentLoginBinding ?= null
    val binding: FragmentLoginBinding
        get() = checkNotNull(_binding)

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override val navController: NavController
        get() = findNavController()

    override fun getFlecha(mensaje: String): NavDirections =
        LoginFragmentDirections.actionLoginFragmentToErrorFragment(mensaje)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        iniciarBinding(inflater,container)
        return binding.root
    }

    fun iniciarBinding(inflater: LayoutInflater,container: ViewGroup?){
        _binding = FragmentLoginBinding.inflate(inflater,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inicializarViewModel()

        viewModel.cargarUsuarios(
            lambdaExito = {

            },
            lambdaError = { mensaje ->
                navegarError(mensaje)
            }
        )



        binding.btnIniciarSesion.setOnClickListener {
            val email = binding.txtEmail.text.toString().trim()
            val password = binding.txtPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Debes ingresar email y contraseña", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.validarLogin(email, password)
            }

        }
        lifecycleScope.launch {
            viewModel.loginState.collect { result ->
                when (result) {
                    is LoginResult.Success -> {
                        val usuario = result.usuario

                        sharedViewModel.setIdUsuario(usuario.idUsuario)

                        findNavController().navigate(R.id.action_loginFragment_to_librosFragment)
                    }

                    is LoginResult.Error -> {
                        Toast.makeText(requireContext(), result.mensaje, Toast.LENGTH_SHORT).show()
                    }

                    is LoginResult.Loading -> {

                    }
                }
            }
        }
    }

    fun inicializarViewModel(){
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}