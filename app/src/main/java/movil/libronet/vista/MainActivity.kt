package movil.libronet.vista

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import movil.libronet.R
import movil.libronet.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inicializarBinding()
        inicializarToolbar()
        setContentView(binding.root)
    }

    fun inicializarBinding(){
        binding = ActivityMainBinding.inflate(layoutInflater)
    }

    fun inicializarToolbar(){
        setSupportActionBar(binding.materialToolbar)
    }
}