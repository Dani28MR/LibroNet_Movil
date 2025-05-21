package movil.libronet.vista

import androidx.recyclerview.widget.RecyclerView
import movil.libronet.databinding.ItemLibroBinding
import movil.libronet.modelo.Libro

class LibroHolder(val binding:ItemLibroBinding):RecyclerView.ViewHolder(binding.root) {
    lateinit var libro: Libro

    fun mostrarLibro(l:Libro){
        libro = l
        binding.txtTitulo.text = l.titulo
        binding.txtISBN.text = l.ISBN
        binding.txtTotalCopias.text = l.totalCopias.toString()
    }

}