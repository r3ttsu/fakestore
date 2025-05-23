package com.example.fakestore.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fakestore.databinding.ItemProductBinding
import com.example.fakestore.model.Product

class ProductAdapter(
    private val mContext: Context,
    private val onItemClick: ((detail: Product) -> Unit)
) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root)
    private var products: List<Product> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product: Product = products[position]
        with(holder) {
            Glide.with(mContext).load(product.image).into(binding.ivProduct)
            binding.tvName.text = product.title
            binding.tvCategory.text = product.category
            binding.tvRating.text = "${product.rating.rate} oleh ${product.rating.count} orang"
            binding.tvHarga.text = "$${product.price}"
            binding.llProduct.setOnClickListener {
                onItemClick(product)
            }
        }
    }

    override fun getItemCount(): Int {
        return products.size
    }

    fun submitList(newList: List<Product>) {
        products = newList
        notifyDataSetChanged()
    }
}