package movil.libronet.vista

import androidx.recyclerview.widget.RecyclerView
import movil.libronet.databinding.ItemEditorialBinding
import movil.libronet.modelo.Editorial

class EditorialHolder(val binding:ItemEditorialBinding):RecyclerView.ViewHolder(binding.root) {
    lateinit var editorial: Editorial

    fun mostrarEditorial(e:Editorial){
        editorial = e
        binding.txtNombre.text = e.nombreEditorial
        binding.txtDireccion.text = e.direccion
    }
}