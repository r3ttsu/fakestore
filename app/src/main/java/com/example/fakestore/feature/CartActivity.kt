package com.example.fakestore.feature

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fakestore.viewmodel.CartViewModel
import com.example.fakestore.MyApp
import com.example.fakestore.R
import com.example.fakestore.adapter.CartAdapter
import com.example.fakestore.adapter.SummaryAdapter
import com.example.fakestore.data.Cart
import com.example.fakestore.databinding.ActivityCartBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import javax.inject.Inject

class CartActivity : AppCompatActivity(), CartAdapter.OnItemClickListener, View.OnClickListener {
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
        val cartAdapter = CartAdapter(this, this)
        binding.rvCart.layoutManager = LinearLayoutManager(this)
        binding.rvCart.adapter = cartAdapter
        viewModel.cartState.observe(this) { state ->
            if(state.data.isEmpty()){
                binding.rvCart.isVisible = false
                binding.tvEmpty.isVisible = true
                binding.btnCheckout.isEnabled = false
                binding.tvTotal.text = "$0.00"
            } else {
                binding.rvCart.isVisible = true
                binding.tvEmpty.isVisible = false
                binding.btnCheckout.isEnabled = true
                cartAdapter.submitList(state.data)
                binding.tvTotal.text = "$${String.format("%.2f", state.totalPrice)}"
            }
        }
        binding.btnCheckout.setOnClickListener(this)
    }

    private fun showProfile(cart: List<Cart>) {
        val view: View = layoutInflater.inflate(R.layout.bottom_sheet_order_summary, null)

        val dialog = BottomSheetDialog(this)
        dialog.behavior.maxHeight =
            (0.8 * Resources.getSystem().displayMetrics.heightPixels).toInt()
        val rvSummary = view.findViewById<RecyclerView>(R.id.rv_summary)
        val btnBack = view.findViewById<AppCompatButton>(R.id.btn_back)
        val total = view.findViewById<TextView>(R.id.total)

        rvSummary.setHasFixedSize(true)
        rvSummary.layoutManager = LinearLayoutManager(this)
        val summaryAdapter = SummaryAdapter(this, cart)
        rvSummary.adapter = summaryAdapter
        btnBack.setOnClickListener {
            dialog.dismiss()
            finish()
        }

        viewModel.cartState.value?.let {
            total.text = "${resources.getString(R.string.total_belanja)}: $${it.totalPrice}"
        }

        dialog.setCancelable(false)
        dialog.setContentView(view)
        dialog.show()
    }

    override fun onItemRemove(item: Cart) {
        viewModel.removeItem(item)
    }

    override fun updateQty(cart: Cart) {
        viewModel.updateQty(cart)
    }

    override fun onClick(view: View?) {
        if(view?.id == binding.btnCheckout.id){
            viewModel.cartState.value?.let {
                showProfile(it.data)
                viewModel.checkout()
            }
        }
    }
}