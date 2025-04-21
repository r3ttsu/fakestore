package com.example.fakestore.feature

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.fakestore.Constant
import com.example.fakestore.MyApp
import com.example.fakestore.databinding.ActivityDetailProductBinding
import com.example.fakestore.viewmodel.DetailProductViewModel
import javax.inject.Inject

class DetailProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailProductBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: DetailProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MyApp).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, viewModelFactory)[DetailProductViewModel::class.java]

        setupView()
    }

    private fun setupView() {
        with(binding) {
            viewModel.getProductDetail(intent.getIntExtra(Constant.ID, 0))
            viewModel.detailProductState.observe(this@DetailProductActivity) { state ->
                progressbar.isVisible = state.isLoading
                clMain.isVisible = !state.isLoading
                if (state.error != null) {
                    Toast.makeText(this@DetailProductActivity, state.error, Toast.LENGTH_SHORT)
                        .show()
                } else {
                    val product = state.data

                    if (!state.isLoading) {
                        Glide.with(this@DetailProductActivity).load(product?.image)
                            .into(binding.ivProduct)
                        tvName.text = product?.title
                        tvPrice.text = "$${product?.price}"
                        tvRating.text = "${product?.rating?.rate} oleh ${product?.rating?.count} orang"
                        tvDetail.text = product?.description
                    }
                }
                btnBuy.setOnClickListener { state.data?.let { it1 -> viewModel.addToCart(it1) } }
                if(state.message != null){
                    Toast.makeText(this@DetailProductActivity, state.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}