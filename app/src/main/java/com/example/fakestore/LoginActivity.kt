package com.example.fakestore

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.fakestore.databinding.ActivityLoginBinding
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MyApp).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]

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
            binding.btnLogin.setOnClickListener { submitLogin() }
        }
    }

    private fun submitLogin() {
        val username = binding.etUsername.text.toString()
        val password = binding.etPassword.text.toString()
        if (username != "" && password != "") {
            val isValid = viewModel.validateUser(username, password)

            if (isValid) {
                viewModel.setActiveUser(username, password)
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
                finish()
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