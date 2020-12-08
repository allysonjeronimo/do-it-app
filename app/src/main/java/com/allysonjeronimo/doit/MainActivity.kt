package com.allysonjeronimo.doit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // configurar toolbar utilizando navigation
        // para a toolbar reagir de acordo com a navegação

        // recupera o navHostFragment (Container das fragments de navegação)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        // configurar java 8 no gradle
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        app_toolbar.setupWithNavController(navController, appBarConfiguration)
    }
}