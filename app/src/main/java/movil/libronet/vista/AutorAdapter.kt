package movil.libronet.vista

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import movil.libronet.databinding.ItemAutorBinding
import movil.libronet.modelo.Autor

class AutorAdapter(
    var autores:List<Autor>,
    val lambda:(AutorHolder) -> Unit
):RecyclerView.Adapter<AutorHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AutorHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAutorBinding.inflate(inflater,parent,false)
        return AutorHolder(binding)
    }

    override fun getItemCount(): Int = autores.size

    override fun onBindViewHolder(holder: AutorHolder, position: Int) {
        val autor = autores.get(position)
        holder.mostrarAutor(autor)
        holder.binding.root.setOnClickListener {
            lambda(holder)
        }
    }
    fun setListaAutores(a:List<Autor>){
        autores = a
        notifyDataSetChanged()
    }

}