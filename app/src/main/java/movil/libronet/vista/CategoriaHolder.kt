package movil.libronet.vista

import androidx.recyclerview.widget.RecyclerView
import movil.libronet.databinding.ItemCategoriaBinding
import movil.libronet.modelo.Categoria

class CategoriaHolder(val binding:ItemCategoriaBinding):RecyclerView.ViewHolder(binding.root) {
    lateinit var categoria: Categoria

    fun mostrarCategoria(c:Categoria){
        categoria = c
        binding.txtCategoria.text = c.nombreCategoria
    }
}