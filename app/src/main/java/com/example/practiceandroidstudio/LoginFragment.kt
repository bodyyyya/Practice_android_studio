package com.example.practiceandroidstudio

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import android.widget.Toast
import android.content.Context

class LoginFragment : Fragment(R.layout.fragment_login) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etEmail = view.findViewById<EditText>(R.id.etEmail)
        val etPassword = view.findViewById<EditText>(R.id.etPassword)
        val btnLogin = view.findViewById<Button>(R.id.btnLogin)
        val tvRegister = view.findViewById<TextView>(R.id.tvRegister)

        btnLogin.setOnClickListener {
            val inputEmail = etEmail.text.toString().trim()
            val inputPassword = etPassword.text.toString().trim()

            // Отримуємо збережені дані
            val sharedPreferences = requireActivity().getSharedPreferences("user_pref", Context.MODE_PRIVATE)
            val savedEmail = sharedPreferences.getString("email", "")
            val savedPassword = sharedPreferences.getString("password", "")

            if (inputEmail == savedEmail && inputPassword == savedPassword) {
                Toast.makeText(requireContext(), "Авторизація успішна", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
            } else {
                Toast.makeText(requireContext(), "Невірний email або пароль", Toast.LENGTH_SHORT).show()
            }
        }


        tvRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }
    }
}
