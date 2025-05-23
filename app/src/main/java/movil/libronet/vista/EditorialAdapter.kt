package movil.libronet.vista

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import movil.libronet.databinding.ItemEditorialBinding
import movil.libronet.modelo.Editorial

class EditorialAdapter(
    var editoriales:List<Editorial>,
    val lambda:(EditorialHolder) -> Unit
):RecyclerView.Adapter<EditorialHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditorialHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemEditorialBinding.inflate(inflater,parent,false)
        return EditorialHolder(binding)
    }

    override fun getItemCount(): Int = editoriales.size

    override fun onBindViewHolder(holder: EditorialHolder, position: Int) {
        val editorial = editoriales.get(position)
        holder.mostrarEditorial(editorial)
        holder.binding.root.setOnClickListener {
            lambda(holder)
        }
    }

    fun setListaEditoriales(e:List<Editorial>){
        editoriales = e
        notifyDataSetChanged()
    }

}