package com.example.practiceandroidstudio

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.practiceandroidstudio.databinding.Activity2Binding
import android.app.Activity
import android.widget.Button

class Activity2 : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: Activity2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = Activity2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fab).show()
        }

        // Додаємо кнопку для повернення на головну активність
        findViewById<Button>(R.id.button_back).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()  // Завершуємо Activity2, щоб не зберігати його в стеку
        }
        // Для Activity2
        findViewById<Button>(R.id.button_back).setOnClickListener {
            val resultIntent = Intent().apply { putExtra("screen", "Activity2") }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_2)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}
