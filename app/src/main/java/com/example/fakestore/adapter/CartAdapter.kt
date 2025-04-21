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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CartAdapter(
    private val mContext: Context,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root)

    private var carts: List<Cart> = emptyList()
    private val adapterScope = CoroutineScope(Dispatchers.Main)
    private var debounceJob: Job? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCartBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n", "CheckResult")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cartItem: Cart = carts[position]
        var qty = cartItem.quantity
        with(holder) {
            Glide.with(mContext).load(cartItem.image).into(binding.ivProduct)
            binding.tvTitle.text = cartItem.title
            binding.tvPrice.text = "$${cartItem.price}"
            binding.ibDelete.setOnClickListener {
                val alertDialog = AlertDialog.Builder(mContext)
                    .setMessage("Hapus barang dari keranjang?")
                    .setPositiveButton("Hapus") { _: DialogInterface?, _: Int ->
                        listener.onItemRemove(cartItem)
                    }
                    .setNegativeButton("Batal") { dialogInterface: DialogInterface, i: Int -> dialogInterface.dismiss() }
                    .setCancelable(false)
                    .create()
                alertDialog.show()
            }
            binding.tvQty.text = qty.toString()
            if (qty == 1) binding.btnMin.isEnabled = false
            binding.btnPlus.setOnClickListener {
                qty++
                if (!binding.btnMin.isEnabled) binding.btnMin.isEnabled = true
                binding.tvQty.text = qty.toString()
                cartItem.quantity = qty
                triggerDebouncedUpdate(cartItem)
            }
            binding.btnMin.setOnClickListener {
                qty--
                binding.btnMin.isEnabled = qty > 1
                binding.tvQty.text = qty.toString()
                cartItem.quantity = qty
                triggerDebouncedUpdate(cartItem)
            }
        }
    }

    override fun getItemCount(): Int {
        return carts.size
    }

    private fun triggerDebouncedUpdate(cart: Cart) {
        debounceJob?.cancel() // cancel previous job if user taps again fast
        debounceJob = adapterScope.launch {
            delay(1500L) // debounce duration
            listener.updateQty(cart)
        }
    }

    fun submitList(newList: List<Cart>) {
        carts = newList
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemRemove(item: Cart)
        fun updateQty(cart: Cart)
    }
}