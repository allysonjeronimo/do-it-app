package com.allysonjeronimo.doit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var navController:NavController
    private lateinit var appBarConfiguration:AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.setSupportActionBar(app_toolbar)

        // configurar toolbar utilizando navigation
        // para a toolbar reagir de acordo com a navegação

        // recupera o navHostFragment (Container das fragments de navegação)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        // configurar java 8 no gradle
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Sempre que houver alguma navegação
        navController.addOnDestinationChangedListener { _, _, _ ->
            // modificando ícone de back
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        }
    }

    // BackStack (necessário quando se usa o setupActionBarWithNavController)
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(this.appBarConfiguration) || super.onSupportNavigateUp()
    }
}