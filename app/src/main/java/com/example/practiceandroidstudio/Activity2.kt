package com.example.practiceandroidstudio

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.practiceandroidstudio.databinding.Activity2Binding
import java.io.File
import java.io.FileOutputStream

class Activity2 : AppCompatActivity() {

    private lateinit var binding: Activity2Binding
    private lateinit var imageView: ImageView
    private lateinit var sharedPrefs: SharedPreferences

    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_IMAGE_PICK = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = Activity2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ініціалізація SharedPreferences
        sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // Отримуємо посилання на ImageView з layout
        imageView = findViewById(R.id.imageView)

        // Завантаження збереженого фото, якщо є
        loadSavedImage()

        // Обробка кнопки галереї
        findViewById<Button>(R.id.button_gallery).setOnClickListener {
            dispatchPickImageIntent()
        }

        // Кнопка повернення
        findViewById<Button>(R.id.button_back).setOnClickListener {
            val resultIntent = Intent().apply { putExtra("screen", "Activity2") }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    // Запуск камери (приклад, якщо потрібно)
    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    // Запуск галереї
    private fun dispatchPickImageIntent() {
        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickIntent, REQUEST_IMAGE_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    // Отримуємо зображення з даних Intent (thumbnail)
                    val extras = data.extras
                    val imageBitmap = extras?.get("data") as? Bitmap
                    imageView.setImageBitmap(imageBitmap)
                    // Зберігаємо маркер "thumbnail" як приклад
                    saveImagePath("thumbnail")
                }
                REQUEST_IMAGE_PICK -> {
                    // Отримуємо URI з галереї
                    val imageUri = data.data
                    imageUri?.let {
                        // Копіюємо зображення у внутрішнє сховище
                        val localPath = copyImageToInternalStorage(it)
                        localPath?.let { path ->
                            // Встановлюємо зображення з локального файлу
                            imageView.setImageURI(Uri.fromFile(File(path)))
                            // Зберігаємо локальний шлях для наступного запуску
                            saveImagePath(path)
                        }
                    }
                }
            }
        }
    }

    // Функція для копіювання зображення у внутрішнє сховище
    private fun copyImageToInternalStorage(uri: Uri): String? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            val file = File(filesDir, "saved_image.jpg")
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Зберігання шляху до фото через SharedPreferences
    private fun saveImagePath(path: String) {
        with(sharedPrefs.edit()) {
            putString("image_path", path)
            apply()
        }
    }

    // Завантаження фото з SharedPreferences при старті Activity
    private fun loadSavedImage() {
        val savedPath = sharedPrefs.getString("image_path", null)
        savedPath?.let {
            if (it == "thumbnail") {
                imageView.setImageResource(android.R.drawable.ic_menu_gallery)
            } else {
                imageView.setImageURI(Uri.fromFile(File(it)))
            }
        }
    }
}
