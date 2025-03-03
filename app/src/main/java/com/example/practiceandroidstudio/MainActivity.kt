package com.example.practiceandroidstudio

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowCompat

class MainActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_CODE_ACTIVITY2 = 100
        const val REQUEST_CODE_ACTIVITY3 = 200
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Виклик Activity2
        findViewById<Button>(R.id.button_first).setOnClickListener {
            val intent = Intent(this, Activity2::class.java)
            startActivityForResult(intent, REQUEST_CODE_ACTIVITY2)
        }

        // Виклик Activity3
        findViewById<Button>(R.id.button_second).setOnClickListener {
            val intent = Intent(this, Activity3::class.java)
            startActivityForResult(intent, REQUEST_CODE_ACTIVITY3)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val screen = data?.getStringExtra("screen")
            when (requestCode) {
                REQUEST_CODE_ACTIVITY2 -> {
                    Toast.makeText(this, "Повернулися з: Activity2 ($screen)", Toast.LENGTH_SHORT).show()
                }
                REQUEST_CODE_ACTIVITY3 -> {
                    Toast.makeText(this, "Повернулися з: Activity3 ($screen)", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
