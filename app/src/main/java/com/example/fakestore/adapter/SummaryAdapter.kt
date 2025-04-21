package com.example.fakestore.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fakestore.data.Cart
import com.example.fakestore.databinding.ItemSummaryBinding

class SummaryAdapter(
    private val mContext: Context,
    private val carts: List<Cart>
) : RecyclerView.Adapter<SummaryAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemSummaryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSummaryBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n", "CheckResult")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cartItem: Cart = carts[position]
        val qty = cartItem.quantity
        with(holder) {
            Glide.with(mContext).load(cartItem.image).into(binding.ivProduct)
            val price = qty * cartItem.price
            binding.tvTitle.text = cartItem.title
            binding.tvPrice.text = "$$price"
        }
    }

    override fun getItemCount(): Int {
        return carts.size
    }
}