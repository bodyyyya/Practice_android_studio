package com.example.practiceandroidstudio

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.ui.setupWithNavController
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferences = getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("email", "")
        val password = sharedPreferences.getString("password", "")

        if (!email.isNullOrEmpty() && !password.isNullOrEmpty()) {
            val navController = findNavController(R.id.nav_host_fragment)
            when (navController.currentDestination?.id) {
                R.id.loginFragment -> navController.navigate(R.id.action_loginFragment_to_mainFragment)
                R.id.registrationFragment -> navController.navigate(R.id.action_registrationFragment_to_mainFragment)
            }
        }


        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        setupActionBarWithNavController(findNavController(R.id.nav_host_fragment))

        val navController = findNavController(R.id.nav_host_fragment)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment, R.id.registrationFragment -> bottomNav.visibility = View.GONE
                else -> bottomNav.visibility = View.VISIBLE
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp() || super.onSupportNavigateUp()
    }
}
