package movil.libronet.vista

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import movil.libronet.databinding.ItemLibroBinding
import movil.libronet.modelo.Libro

class LibroAdapter(
    var libros:List<Libro>,
    val lambda: (LibroHolder) -> Unit
    ) : RecyclerView.Adapter<LibroHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibroHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLibroBinding.inflate(inflater, parent, false)
        return LibroHolder(binding)
    }

    override fun getItemCount(): Int = libros.size

    override fun onBindViewHolder(holder: LibroHolder, position: Int) {
        val libro = libros.get(position)
        holder.mostrarLibro(libro)
        holder.binding.root.setOnClickListener {
            lambda(holder)
        }
    }

    fun setListaLibros(l:List<Libro>){
        libros = l
        notifyDataSetChanged()
    }

}