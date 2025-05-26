package movil.libronet.vista

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.onNavDestinationSelected
import com.google.android.material.navigation.NavigationView
import movil.libronet.R
import movil.libronet.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inicializarBinding()
        configurarToolbar()
        configurarBottomNavigationBar()
        setContentView(binding.root)
    }

    fun inicializarBinding(){
        binding = ActivityMainBinding.inflate(layoutInflater)
    }



    fun configurarToolbar(){
        setSupportActionBar(binding.materialToolbar)
        addMenuProvider(object:MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_barra,menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                val navController = findNavController(R.id.fragment_container_view)
                menuItem.onNavDestinationSelected(navController)

                return true
            }
        })

        val navController = getNavController()
        val configuracion = AppBarConfiguration.Builder(navController.graph).build()
        NavigationUI.setupWithNavController(binding.materialToolbar,navController,configuracion)
    }
    private fun configurarBottomNavigationBar(){
        val navController = getNavController()
        NavigationUI.setupWithNavController(binding.bottomNavigationBar,navController)
    }

    fun getNavController():NavController{
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_container_view) as NavHostFragment
        return navHostFragment.navController
    }


}