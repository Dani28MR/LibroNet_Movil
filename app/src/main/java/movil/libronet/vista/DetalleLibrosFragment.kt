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
import kotlinx.coroutines.launch
import movil.libronet.R
import movil.libronet.databinding.FragmentDetalleLibrosBinding
import movil.libronet.databinding.FragmentLibrosBinding
import movil.libronet.modelo.Autor
import movil.libronet.modelo.Categoria
import movil.libronet.modelo.Editorial
import movil.libronet.modelo.Libro
import movil.libronet.modelo.LibroNetRepository
import movil.libronet.modelo.Libro_Autor
import movil.libronet.modelo.Reserva
import movil.libronet.modelo.Usuario
import movil.libronet.viewmodel.DetalleLibrosViewModel
import movil.libronet.viewmodel.SharedViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DetalleLibrosFragment : Fragment(), NavegadorError {
    private lateinit var libro: Libro
    lateinit var viewModel:DetalleLibrosViewModel
    var _binding: FragmentDetalleLibrosBinding?=null
    val binding: FragmentDetalleLibrosBinding
        get() = checkNotNull(_binding)

    private var tieneReservaActiva = false

    private lateinit var listaReservas: List<Reserva>

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
        obtenerDatosUsuarioYReservas()
        configurarBotones()

        return binding.root
    }
    private fun obtenerDatosUsuarioYReservas() {
        lifecycleScope.launch {
            try {
                // Obtener usuario y reservas una sola vez
                usuarioLog = LibroNetRepository().consultarUsuario(sharedViewModel.getIdUsuario())
                listaReservas = LibroNetRepository().consultarTodasReservas()

                // Verificar si tiene reserva activa para este libro
                libro = DetalleLibrosFragmentArgs.fromBundle(requireArguments()).libroSeleccionado
                tieneReservaActiva = tieneReservaActiva(libro.idLibro)

                // Actualizar UI después de obtener los datos
                actualizarUI()
            } catch (e: Exception) {
                navegarError(e.message ?: "Error al obtener datos del usuario")
            }
        }
    }

    private fun tieneReservaActiva(idLibro: Int): Boolean {
        if (usuarioLog == null) return false

        return listaReservas.any { reserva ->
            reserva.idUsuario == usuarioLog!!.idUsuario &&
                    reserva.idLibro == idLibro &&
                    reserva.estado == "Activa"
        }
    }
    private fun actualizarUI() {
        val l = libro
        binding.txtTitulo.text = l.titulo
        binding.txtISBN.text = l.ISBN
        binding.txtDescripcion.text = l.descripcion
        binding.txtTotalCopias.text = l.totalCopias.toString()
        binding.txtTotalCopiasDisponibles.text = l.copiasDisponibles.toString()

        lifecycleScope.launch {
            try {
                val listaEditoriales = LibroNetRepository().consultarTodasEditoriales()
                val listaCategorias = LibroNetRepository().consultarTodasCategorias()
                val listaAutores = LibroNetRepository().consultarTodosAutores()
                val listaAutorLibro = LibroNetRepository().consultarTodosLibrosAutor()

                val editorial = listaEditoriales.find { it.idEditorial == l.idEditorial }
                val categoria = listaCategorias.find { it.idCategoria == l.idCategoria }

                binding.txtEditorial.text = editorial?.nombreEditorial ?: "Desconocido"
                binding.txtCategoria.text = categoria?.nombreCategoria ?: "Desconocido"

                val autoresDelLibro = listaAutorLibro
                    .filter { it.idLibro == l.idLibro }
                    .mapNotNull { rel -> listaAutores.find { it.idAutor == rel.idAutor } }

                val nombresAutores = autoresDelLibro.joinToString(", ") { "${it.nombreAutor} ${it.apellidoAutor}" }
                binding.txtAutor.text = nombresAutores.ifEmpty { "Sin autor" }

                // Actualizar estado del botón de reserva
                if (tieneReservaActiva) {
                    binding.btnReservar.isEnabled = false
                    binding.btnReservar.text = "Ya reservado"
                } else if (l.copiasDisponibles <= 0) {
                    binding.btnReservar.isEnabled = false
                    binding.btnReservar.text = "Sin copias disponibles"
                } else {
                    binding.btnReservar.isEnabled = true
                    binding.btnReservar.text = "Reservar libro"
                }
            } catch (e: Exception) {
                navegarError(e.message ?: "Error al cargar detalles del libro")
            }
        }
    }

    private fun configurarBotones() {
        binding.btnReservar.setOnClickListener {
            reservarLibro()
        }
    }

    private fun reservarLibro() {
        // Verificar que tenemos usuario
        if (usuarioLog == null) {
            mostrarToast("No se pudo identificar al usuario")
            return
        }

        // Verificar copias disponibles
        if (libro.copiasDisponibles <= 0) {
            mostrarToast("No hay copias disponibles para reservar")
            return
        }

        // Verificar si ya tiene reserva activa (usando lista local)
        if (tieneReservaActiva(libro.idLibro)) {
            mostrarToast("Ya tienes una reserva activa de este libro")
            return
        }

        // Mostrar progress y deshabilitar botón
        binding.progressBar.visibility = View.VISIBLE
        binding.btnReservar.isEnabled = false

        lifecycleScope.launch {
            try {
                // Crear nueva reserva
                val nuevaReserva = Reserva(
                    idReserva = 0,
                    idUsuario = usuarioLog!!.idUsuario,
                    idLibro = libro.idLibro,
                    fechaReserva = obtenerFechaActual(),
                    fechaExpiracion = calcularFechaExpiracion(),
                    estado = "Activa"
                )

                // Crear reserva y obtener respuesta
                val respuesta = viewModel.crearReserva(nuevaReserva)

                // Manejar respuesta
                if (respuesta.mensaje.contains("éxito", true)) {
                    // Actualizar UI localmente
                    libro = libro.copy(copiasDisponibles = libro.copiasDisponibles - 1)
                    binding.txtTotalCopiasDisponibles.text = libro.copiasDisponibles.toString()
                    mostrarToast(respuesta.mensaje)

                    // Actualizar estado del botón
                    binding.btnReservar.isEnabled = false
                    binding.btnReservar.text = "Reservado"

                    // Actualizar lista local de reservas
                    listaReservas = listaReservas + nuevaReserva
                    tieneReservaActiva = true
                } else {
                    mostrarToast("Error: ${respuesta.mensaje}")
                    binding.btnReservar.isEnabled = true
                }
            } catch (e: Exception) {
                navegarError(e.message ?: "Error desconocido al crear reserva")
                binding.btnReservar.isEnabled = true
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun obtenerFechaActual(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun calcularFechaExpiracion(): String {
        val calendar = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, 30)
        }
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(calendar.time)
    }

    private fun mostrarToast(mensaje: String) {
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show()
    }

    /*fun inicializarInterfaz(){
        lifecycleScope.launch {
            val usuarioLog =
                LibroNetRepository().consultarUsuario(sharedViewModel.getIdUsuario())

            val l = DetalleLibrosFragmentArgs.fromBundle(requireArguments()).libroSeleccionado
            libro = l
            val listaEditoriales:List<Editorial> =
                LibroNetRepository().consultarTodasEditoriales()
            val listaCategorias:List<Categoria> =
                LibroNetRepository().consultarTodasCategorias()
            val listaAutores:List<Autor> =
                LibroNetRepository().consultarTodosAutores()

            val listaAutorLibro:List<Libro_Autor> =
                LibroNetRepository().consultarTodosLibrosAutor()


            binding.txtTitulo.setText(l.titulo)
            binding.txtISBN.setText(l.ISBN)
            binding.txtDescripcion.setText(l.descripcion)
            binding.txtTotalCopias.setText(l.totalCopias.toString())
            binding.txtTotalCopiasDisponibles.setText(l.copiasDisponibles.toString())

            val editorial = listaEditoriales.find { it.idEditorial == l.idEditorial }
            val categoria = listaCategorias.find { it.idCategoria == l.idCategoria }

            binding.txtEditorial.setText(editorial?.nombreEditorial ?: "Desconocido")
            binding.txtCategoria.setText(categoria?.nombreCategoria ?: "Desconocido")

            val autoresDelLibro = listaAutorLibro
                .filter { it.idLibro == l.idLibro }
                .mapNotNull { rel -> listaAutores.find { it.idAutor == rel.idAutor } }

            val nombresAutores = autoresDelLibro.joinToString(", ") { "${it.nombreAutor} ${it.apellidoAutor}" }

            binding.txtAutor.setText(nombresAutores.ifEmpty { "Sin autor" })

            val tieneReserva = viewModel.tieneReservaActiva(
                usuarioLog.idUsuario,
                libro.idLibro
            )

            if (tieneReserva) {
                binding.btnReservar.isEnabled = false
                binding.btnReservar.text = "Ya reservado"
            }
        }

    }*/


    private fun iniciarBinding(inflater: LayoutInflater,container: ViewGroup?){
        _binding = FragmentDetalleLibrosBinding.inflate(inflater,container,false)
    }
    private fun inicializarViewModel(){
        viewModel = ViewModelProvider(this).get(DetalleLibrosViewModel::class.java)
        lifecycleScope.launch {
            usuarioLog = LibroNetRepository().consultarUsuario(sharedViewModel.getIdUsuario())
            listaReservas = LibroNetRepository().consultarTodasReservas()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}