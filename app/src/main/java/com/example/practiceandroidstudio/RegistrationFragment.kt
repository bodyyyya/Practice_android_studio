package com.example.practiceandroidstudio

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import android.widget.Toast

class RegistrationFragment : Fragment(R.layout.fragment_registration) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etName = view.findViewById<EditText>(R.id.etName)
        val etDOB = view.findViewById<EditText>(R.id.etDOB)
        val etAbout = view.findViewById<EditText>(R.id.etAbout)
        val etEmail = view.findViewById<EditText>(R.id.etEmail)
        val etPassword = view.findViewById<EditText>(R.id.etPassword)
        val btnRegister = view.findViewById<Button>(R.id.btnRegister)
        val tvLogin = view.findViewById<TextView>(R.id.tvLogin)

        btnRegister.setOnClickListener {
            val name = etName.text.toString().trim()
            val dob = etDOB.text.toString().trim()
            val about = etAbout.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                val sharedPreferences = requireActivity().getSharedPreferences("user_pref", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("name", name)
                editor.putString("dob", dob)
                editor.putString("about", about)
                editor.putString("email", email)
                editor.putString("password", password)
                editor.apply()

                Toast.makeText(requireContext(), "Реєстрація успішна", Toast.LENGTH_SHORT).show()

                findNavController().navigate(R.id.action_registrationFragment_to_mainFragment)
            } else {
                Toast.makeText(requireContext(), "Будь ласка, заповніть усі обов’язкові поля", Toast.LENGTH_SHORT).show()
            }
        }

        tvLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
        }
    }
}
