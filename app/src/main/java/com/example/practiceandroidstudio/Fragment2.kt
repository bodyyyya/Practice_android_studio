package com.example.practiceandroidstudio

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import java.io.File
import java.io.FileOutputStream

class Fragment2 : Fragment(R.layout.fragment_activity2) {

    private lateinit var imageView: ImageView
    private lateinit var sharedPrefs: SharedPreferences

    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_IMAGE_PICK = 2

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPrefs = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        imageView = view.findViewById(R.id.imageView)

        // Завантаження збереженого фото, якщо є
        loadSavedImage()

        // Обробка кнопки галереї
        view.findViewById<Button>(R.id.button_gallery).setOnClickListener {
            dispatchPickImageIntent()
        }

        // Кнопка повернення (переходимо назад)
        view.findViewById<Button>(R.id.button_back).setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun dispatchPickImageIntent() {
        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickIntent, REQUEST_IMAGE_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == android.app.Activity.RESULT_OK && data != null) {
            when (requestCode) {
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
                REQUEST_IMAGE_CAPTURE -> {
                    val extras = data.extras
                    val imageBitmap = extras?.get("data") as? Bitmap
                    imageView.setImageBitmap(imageBitmap)
                    saveImagePath("thumbnail")
                }
            }
        }
    }

    private fun copyImageToInternalStorage(uri: Uri): String? {
        return try {
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val file = File(requireContext().filesDir, "saved_image.jpg")
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

    private fun saveImagePath(path: String) {
        with(sharedPrefs.edit()) {
            putString("image_path", path)
            apply()
        }
    }

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
