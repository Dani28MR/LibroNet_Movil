package movil.libronet.vista

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.drawerlayout.widget.DrawerLayout
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

        val navController = getNavController()
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val ocultar = destination.id == R.id.loginFragment || destination.id == R.id.errorFragment

            val visibilidad = if (ocultar) View.GONE else View.VISIBLE

            binding.materialToolbar.visibility = visibilidad
            binding.bottomNavigationBar.visibility = visibilidad
            binding.navigationDrawer.setDrawerLockMode(
                if (ocultar) DrawerLayout.LOCK_MODE_LOCKED_CLOSED
                else DrawerLayout.LOCK_MODE_UNLOCKED
            )
        }
    }

    fun inicializarBinding(){
        binding = ActivityMainBinding.inflate(layoutInflater)
    }

    fun configurarToolbar(){
        setSupportActionBar(binding.materialToolbar)

        val navController = getNavController()

        val appBarConfiguration = AppBarConfiguration.Builder(
            setOf(
                R.id.librosFragment,
                R.id.autorFragment,
                R.id.editorialFragment,
                R.id.categoriaFragment
            )
        )
            .setOpenableLayout(binding.navigationDrawer)
            .build()

        NavigationUI.setupWithNavController(binding.materialToolbar, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(binding.navigationView, navController) // drawer menu

        // MenuProvider para la toolbar
        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_barra, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return menuItem.onNavDestinationSelected(navController)
            }
        })
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

    fun mostrarElementosComunes(mostrar: Boolean) {
        val visibilidad = if (mostrar) View.VISIBLE else View.GONE
        binding.materialToolbar.visibility = visibilidad
        binding.navigationDrawer.setDrawerLockMode(
            if (mostrar) DrawerLayout.LOCK_MODE_UNLOCKED
            else DrawerLayout.LOCK_MODE_LOCKED_CLOSED
        )
    }


}