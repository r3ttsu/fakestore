package com.example.fakestore

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.transition.Visibility
import com.example.fakestore.databinding.ActivityLoginBinding
import com.example.fakestore.repository.LoginRepository
import com.google.gson.Gson

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = LoginRepository(service)
        viewModel = LoginViewModel(repository)

        setupView()
    }

    private fun setupView() {
        viewModel.userState.observe(this) { state ->
            binding.progressbar.isVisible = state.isLoading
            when {
                state.data.isNotEmpty() -> {
                    //do nothing
                }

                state.error != null -> {
                    Toast.makeText(this, state.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.btnLogin.setOnClickListener { submitLogin() }
    }

    private fun submitLogin() {
        viewModel.userState.observe(this) { state ->
            if (!state.isLoading && state.data.isNotEmpty()) {
                val username = binding.etUsername.text.toString()
                val password = binding.etPassword.text.toString()
                if (username != "" && password != "") {
                    val isValid = viewModel.validateUser(username, password)

                    if (isValid) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this,
                            "Kombinasi username dan password anda belum tepat!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Username dan Password tidak boleh kosong",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }
    }
}