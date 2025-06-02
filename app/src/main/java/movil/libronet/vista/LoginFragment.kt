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
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.delay
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
                viewModel.setLoadingState()

                binding.progressBar.visibility = View.VISIBLE
                binding.btnIniciarSesion.visibility = View.GONE

                viewModel.validarLogin(email, password)
            }

        }
        lifecycleScope.launch {
            viewModel.loginState.collect { result ->
                when (result) {
                    is LoginResult.Idle -> {
                        updateUI(showProgress = false)
                    }
                    is LoginResult.Success -> {

                        sharedViewModel.setIdUsuario(result.usuario.idUsuario)

                        delay(2000)

                        updateUI(showProgress = false)

                        findNavController().navigate(
                            R.id.action_loginFragment_to_librosFragment,
                            null,
                            NavOptions.Builder()
                                .setPopUpTo(R.id.loginFragment, inclusive = true)
                                .build()
                        )
                    }

                    is LoginResult.Error -> {
                        updateUI(showProgress = false)
                        Toast.makeText(requireContext(), result.mensaje, Toast.LENGTH_SHORT).show()
                        viewModel.resetState()
                    }

                    is LoginResult.Loading -> {
                        updateUI(showProgress = true)
                    }
                }
            }
        }
    }

    private fun updateUI(showProgress: Boolean) {
        binding.apply {
            progressBar.visibility = if (showProgress) View.VISIBLE else View.GONE
            btnIniciarSesion.visibility = if (showProgress) View.GONE else View.VISIBLE
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