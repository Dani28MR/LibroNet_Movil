package movil.libronet.vista

import androidx.recyclerview.widget.RecyclerView
import movil.libronet.databinding.ItemAutorBinding
import movil.libronet.modelo.Autor

class AutorHolder(val binding:ItemAutorBinding):RecyclerView.ViewHolder(binding.root) {
    lateinit var autor:Autor

    fun mostrarAutor(a:Autor){
        autor = a
        binding.txtNombreAutor.text = a.nombreAutor
        binding.txtApellidosAutor.text = a.apellidoAutor
    }
}