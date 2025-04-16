package com.example.fakestore

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fakestore.adapter.CartAdapter
import com.example.fakestore.databinding.ActivityCartBinding
import javax.inject.Inject

class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: CartViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MyApp).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, viewModelFactory)[CartViewModel::class.java]

        setupView()
    }

    private fun setupView() {
        val cartAdapter = CartAdapter(this)
        binding.rvCart.layoutManager = LinearLayoutManager(this)
        binding.rvCart.adapter = cartAdapter
        viewModel.cartState.observe(this) { state ->
            cartAdapter.submitList(state.data)
        }
    }
}