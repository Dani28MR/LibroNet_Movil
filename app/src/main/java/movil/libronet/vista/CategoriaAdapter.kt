package movil.libronet.vista

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import movil.libronet.databinding.ItemCategoriaBinding
import movil.libronet.modelo.Categoria

class CategoriaAdapter(
    var categorias:List<Categoria>,
    val lambda: (CategoriaHolder) -> Unit
    ):RecyclerView.Adapter<CategoriaHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriaHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCategoriaBinding.inflate(inflater,parent,false)
        return CategoriaHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoriaHolder, position: Int) {
        val categoria = categorias.get(position)
        holder.mostrarCategoria(categoria)
        holder.binding.root.setOnClickListener {
            lambda(holder)
        }
    }

    override fun getItemCount(): Int = categorias.size

    fun setListaCategorias(c:List<Categoria>){
        categorias = c
        notifyDataSetChanged()
    }

    }