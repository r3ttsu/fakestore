package com.example.fakestore.feature

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.fakestore.MyApp
import com.example.fakestore.databinding.ActivitySplashBinding
import com.example.fakestore.viewmodel.SplashViewModel
import javax.inject.Inject

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MyApp).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, viewModelFactory)[SplashViewModel::class.java]

        validateSession()
    }

    private fun validateSession() {
        viewModel.splashState.observe(this) { state ->
            if (state.error != null) {
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
                Handler().postDelayed({ finish() }, 1500)
            } else {
                val intent = if (viewModel.isLoggedIn()) Intent(
                    this,
                    DashboardActivity::class.java
                ) else Intent(this, LoginActivity::class.java)
                Handler().postDelayed({
                    startActivity(intent)
                    finish()
                }, 1500)
            }
        }
    }
}