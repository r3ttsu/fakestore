package com.example.fakestore.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fakestore.data.Cart
import com.example.fakestore.databinding.ItemCartBinding

class CartAdapter(
    private val mContext: Context
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root)

    private var carts: List<Cart> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCartBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n", "CheckResult")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cartItem: Cart = carts[position]
        with(holder) {
            Glide.with(mContext).load(cartItem.image).into(binding.ivProduct)
            binding.tvTitle.text = cartItem.title
            binding.tvPrice.text = "$ ${cartItem.price}"
            binding.llCartItem.setOnLongClickListener {
                val alertDialog = AlertDialog.Builder(mContext)
                    .setMessage("Hapus barang dari keranjang?")
                    .setPositiveButton("Hapus") { _: DialogInterface?, _: Int ->
//                        onItemRemove(checkoutCart)
                    }
                    .setNegativeButton("Batal") { dialogInterface: DialogInterface, i: Int -> dialogInterface.dismiss() }
                    .setCancelable(false)
                    .create()
                alertDialog.show()
                return@setOnLongClickListener false
            }
        }
    }

    override fun getItemCount(): Int {
        return carts.size
    }

    fun submitList(newList: List<Cart>) {
        carts = newList
        notifyDataSetChanged()
    }
}