package com.example.practiceandroidstudio

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import java.io.File
import java.io.FileOutputStream

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null
    private lateinit var sharedPref: android.content.SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref = requireActivity().getSharedPreferences("user_pref", Context.MODE_PRIVATE)

        loadSavedImage(view)

        val ivAvatar = view.findViewById<ImageView>(R.id.ivAvatar)
        val btnEditAvatar = view.findViewById<Button>(R.id.btnEditAvatar)
        val etName = view.findViewById<EditText>(R.id.etNameProfile)
        val etDOB = view.findViewById<EditText>(R.id.etDOBProfile)
        val etAbout = view.findViewById<EditText>(R.id.etAboutProfile)
        val etEmail = view.findViewById<EditText>(R.id.etEmailProfile)
        val btnSaveProfile = view.findViewById<Button>(R.id.btnSaveProfile)
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)
        val btnDeleteUser = view.findViewById<Button>(R.id.btnDeleteUser)

        // Завантаження інших даних з SharedPreferences
        etName.setText(sharedPref.getString("name", ""))
        etDOB.setText(sharedPref.getString("dob", ""))
        etAbout.setText(sharedPref.getString("about", ""))
        etEmail.setText(sharedPref.getString("email", ""))

        btnEditAvatar.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        btnSaveProfile.setOnClickListener {
            val editor = sharedPref.edit()
            editor.putString("name", etName.text.toString())
            editor.putString("dob", etDOB.text.toString())
            editor.putString("about", etAbout.text.toString())
            editor.putString("email", etEmail.text.toString())
            editor.apply()

            Toast.makeText(requireContext(), "Зміни збережено", Toast.LENGTH_SHORT).show()
        }

        btnLogout.setOnClickListener {
            Toast.makeText(requireContext(), "Ви вийшли з акаунту", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.loginFragment)
        }

        btnDeleteUser.setOnClickListener {
            sharedPref.edit().clear().apply()
            Toast.makeText(requireContext(), "Акаунт видалено", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.loginFragment)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data
            imageUri?.let {
                val localPath = copyImageToInternalStorage(it)
                localPath?.let { path ->
                    view?.findViewById<ImageView>(R.id.ivAvatar)?.setImageURI(Uri.fromFile(File(path)))
                    saveImagePath(path)
                }
            }
        }
    }

    private fun copyImageToInternalStorage(uri: Uri): String? {
        return try {
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val file = File(requireContext().filesDir, "profile_image.jpg")
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
        with(sharedPref.edit()) {
            putString("profile_image_path", path)
            apply()
        }
    }

    private fun loadSavedImage(view: View) {
        val savedPath = sharedPref.getString("profile_image_path", null)
        savedPath?.let {
            val file = File(it)
            if (file.exists()) {
                view.findViewById<ImageView>(R.id.ivAvatar).setImageURI(Uri.fromFile(file))
            }
        }
    }
}
