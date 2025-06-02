package movil.libronet.vista

import android.os.Bundle
import android.util.Log
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
import kotlinx.coroutines.launch
import movil.libronet.databinding.FragmentDetalleLibrosBinding
import movil.libronet.modelo.EstadoReserva
import movil.libronet.modelo.Libro
import movil.libronet.modelo.LibroNetRepository
import movil.libronet.modelo.Reserva
import movil.libronet.modelo.Usuario
import movil.libronet.viewmodel.DetalleLibrosViewModel
import movil.libronet.viewmodel.SharedViewModel
import java.time.LocalDate

class DetalleLibrosFragment : Fragment(), NavegadorError {
    private lateinit var libro: Libro
    lateinit var viewModel:DetalleLibrosViewModel
    var _binding: FragmentDetalleLibrosBinding?=null
    val binding: FragmentDetalleLibrosBinding
        get() = checkNotNull(_binding)


    private var reservasActivas = mutableListOf<Reserva>()

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var usuarioLog:Usuario

    override val navController: NavController
        get() = findNavController()

    override fun getFlecha(mensaje: String): NavDirections =
        DetalleLibrosFragmentDirections.actionDetalleLibrosFragmentToErrorFragment(mensaje)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        iniciarBinding(inflater,container)
        inicializarViewModel()
        libro = DetalleLibrosFragmentArgs.fromBundle(requireArguments()).libroSeleccionado
        configurarBotones()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        obtenerDatosUsuarioYReservas()
    }

    private fun obtenerDatosUsuarioYReservas() {
        lifecycleScope.launch {
            try {
                usuarioLog = LibroNetRepository().consultarUsuario(sharedViewModel.getIdUsuario())

                reservasActivas.clear()
                reservasActivas.addAll(
                    LibroNetRepository().consultarTodasReservas()
                        .filter {
                            it.estado == EstadoReserva.ACTIVO &&
                                    it.idUsuario == usuarioLog.idUsuario
                        }
                )
                actualizarUI()
            } catch (e: Exception) {
                navegarError(e.message ?: "Error al obtener datos del usuario")
            }
        }
    }
    private fun actualizarUI() {
        lifecycleScope.launch {
            binding.progressBar.visibility = View.VISIBLE
            val l = libro
            binding.txtTitulo.text = l.titulo
            binding.txtISBN.text = l.ISBN
            binding.txtDescripcion.text = l.descripcion
            binding.txtTotalCopias.text = l.totalCopias.toString()
            binding.txtTotalCopiasDisponibles.text = l.copiasDisponibles.toString()
            binding.txtAutor.text = obtenerAutor(l.idLibro)
            binding.txtCategoria.text = obtenerCategoria(l.idLibro)
            binding.txtEditorial.text = obtenerEditorial(l.idLibro)

            binding.progressBar.visibility = View.GONE
            binding.btnReservar.isEnabled = !tieneReservaActiva(l.idLibro) && l.copiasDisponibles > 0
            when {
                tieneReservaActiva(l.idLibro) -> {
                    binding.btnReservar.text = "Ya reservado"
                    binding.btnReservar.isEnabled = false
                }
                l.copiasDisponibles <= 0 -> {
                    binding.btnReservar.text = "Sin copias disponibles"
                    binding.btnReservar.isEnabled = false
                }
                else -> {
                    binding.btnReservar.text = "Reservar libro"
                    binding.btnReservar.isEnabled = true
                }
            }

        }

    }
    suspend fun obtenerAutor(idLibro: Int): String {
        val listaLibroAutor = LibroNetRepository().consultarTodosLibrosAutor()
        val listaAutores = LibroNetRepository().consultarTodosAutores()

        val idsAutor = listaLibroAutor
            .filter { it.idLibro == idLibro }
            .map { it.idAutor }

        val nombresAutores = listaAutores
            .filter { it.idAutor in idsAutor }
            .joinToString(", ") { "${it.nombreAutor} ${it.apellidoAutor}" }

        return if (nombresAutores.isNotBlank()) nombresAutores else "Autor desconocido"
    }


    suspend fun obtenerCategoria(idLibro: Int): String {
        val listaLibros = LibroNetRepository().consultarTodosLibros()
        val listaCategorias = LibroNetRepository().consultarTodasCategorias()

        val libro = listaLibros.find { it.idLibro == idLibro }
        val idCategoria = libro?.idCategoria

        val categoria = listaCategorias.find { it.idCategoria == idCategoria }

        return categoria?.nombreCategoria ?: "Categoría desconocida"
    }

    suspend fun obtenerEditorial(idLibro: Int): String {
        val listaLibros = LibroNetRepository().consultarTodosLibros()
        val listaEditoriales = LibroNetRepository().consultarTodasEditoriales()

        val libro = listaLibros.find { it.idLibro == idLibro }
        val idEditorial = libro?.idEditorial

        val editorial = listaEditoriales.find { it.idEditorial == idEditorial }

        return editorial?.nombreEditorial ?: "Editorial desconocida"
    }


    private fun configurarBotones() {
        binding.btnReservar.setOnClickListener {
            reservarLibro()
        }
        binding.btnCancelarReserva.setOnClickListener{
            cancelarReserva()
        }
    }

    private fun cancelarReserva() {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnCancelarReserva.isEnabled = false

        lifecycleScope.launch {
            try {
                val listaReserva = LibroNetRepository().consultarTodasReservas()
                val idUsuario = usuarioLog.idUsuario
                val idLibro = libro.idLibro

                val reservaEncontrada = listaReserva.find {
                    it.idUsuario == idUsuario && it.idLibro == idLibro && it.estado == EstadoReserva.ACTIVO
                }

                if (reservaEncontrada != null) {
                    val (respuesta, libroActualizado) = viewModel.cancelarReserva(reservaEncontrada)
                    mostrarToast("Reserva cancelada con éxito")

                    libro = libroActualizado
                    reservasActivas.remove(reservaEncontrada)

                    reservasActivas.clear()
                    reservasActivas.addAll(
                        LibroNetRepository().consultarTodasReservas()
                            .filter {
                                it.estado == EstadoReserva.ACTIVO &&
                                        it.idUsuario == usuarioLog.idUsuario
                            }
                    )

                    actualizarUI()
                } else {
                    mostrarToast("No se encontró una reserva activa para cancelar")
                }
            } catch (e: Exception) {                mostrarToast("Error: ${e.message ?: "Desconocido"}")
                Log.e("CancelarReserva", "Error: ${e.stackTraceToString()}")
            } finally {
                binding.progressBar.visibility = View.GONE
                binding.btnCancelarReserva.isEnabled = true
            }
        }
    }


    private fun reservarLibro() {
        val libroActual = libro ?: return
        val usuarioActual = usuarioLog ?: return

        if (tieneReservaActiva(libroActual.idLibro)) {
            mostrarToast("Ya tienes una reserva activa")
            return
        }
        if (libroActual.copiasDisponibles <= 0) {
            mostrarToast("No hay copias disponibles para reservar")
            return
        }

        binding.progressBar.visibility = View.VISIBLE
        binding.btnReservar.isEnabled = false

        lifecycleScope.launch {
            try {
                val nuevaReserva = Reserva(
                    idReserva = 0,
                    idUsuario = usuarioActual.idUsuario,
                    idLibro = libroActual.idLibro,
                    fechaReserva = LocalDate.now(),
                    fechaExpiracion = LocalDate.now().plusDays(30),
                    estado = EstadoReserva.ACTIVO
                )
                val respuestaReserva = viewModel.crearReserva(nuevaReserva)

                if (!respuestaReserva.mensaje.contains("exitosamente", true)) {
                    mostrarToast(respuestaReserva.mensaje)
                    return@launch
                }

                val libroActualizado = libroActual.copy(
                    copiasDisponibles = libroActual.copiasDisponibles - 1
                )

                val respuestaActualizacion = viewModel.actualizarLibro(libroActualizado)

                if (!respuestaActualizacion.mensaje.contains("correctamente", true)) {
                    viewModel.cancelarReserva(nuevaReserva)
                    mostrarToast("Error actualizando disponibilidad")
                    return@launch
                }

                reservasActivas.add(nuevaReserva)
                libro = libroActualizado
                actualizarUI()
                mostrarToast("Se ha realizado tu reserva" )


            } catch (e: Exception) {
                mostrarToast("Error: ${e.message ?: "Desconocido"}")
                Log.e("Reserva", "Error: ${e.stackTraceToString()}")
            } finally {
                binding.btnReservar.isEnabled = true
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun tieneReservaActiva(idLibro: Int): Boolean {
        return reservasActivas.any { it.idLibro == idLibro }
    }

    private fun mostrarToast(mensaje: String) {
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show()
    }


    private fun iniciarBinding(inflater: LayoutInflater,container: ViewGroup?){
        _binding = FragmentDetalleLibrosBinding.inflate(inflater,container,false)
    }
    private fun inicializarViewModel(){
        viewModel = ViewModelProvider(this).get(DetalleLibrosViewModel::class.java)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}