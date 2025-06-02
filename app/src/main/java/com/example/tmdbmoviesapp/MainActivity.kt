package com.example.tmdbmoviesapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var bannerConnection: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup the Toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        // Initialize Banner
        bannerConnection = findViewById(R.id.banner_connection)

        observeNetworkChanges()
        // Setup NavController
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Setup AppBar with NavController
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homeFragment, R.id.favoritesFragment)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Setup BottomNavigationView with NavController
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        NavigationUI.setupWithNavController(bottomNav, navController)
    }

    private fun observeNetworkChanges() {
        lifecycleScope.launch {
            NetworkListener.observe(this@MainActivity)
                .distinctUntilChanged()
                .collect { isConnected ->
                    if (isConnected) {
                        // Hide the banner
                        bannerConnection.visibility = View.GONE
                    } else {
                        // Show the banner
                        bannerConnection.text = "You are Offline" // Replace with your string resource
                        bannerConnection.setBackgroundColor(getColor(com.google.android.material.R.color.material_blue_grey_800))
                        bannerConnection.setTextColor(getColor(android.R.color.white))
                        bannerConnection.visibility = View.VISIBLE
                    }
                }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        return when (item.itemId) {
            R.id.action_settings -> {
                navController.navigate(R.id.settingsFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
