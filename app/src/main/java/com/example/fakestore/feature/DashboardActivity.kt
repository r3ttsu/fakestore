package com.example.fakestore.feature

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fakestore.Constant
import com.example.fakestore.MyApp
import com.example.fakestore.R
import com.example.fakestore.adapter.CategoryAdapter
import com.example.fakestore.adapter.ProductAdapter
import com.example.fakestore.data.User
import com.example.fakestore.databinding.ActivityMainBinding
import com.example.fakestore.viewmodel.DashboardViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import javax.inject.Inject

class DashboardActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    private var doubleBackToExitPressedOnce = false

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: DashboardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MyApp).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        viewModel = ViewModelProvider(this, viewModelFactory)[DashboardViewModel::class.java]

//        setupView()
    }

    private fun setupView() {
        with(binding) {
            fabCart.setOnClickListener(this@DashboardActivity)
            btnFilter.setOnClickListener(this@DashboardActivity)
            val productAdapter =
                ProductAdapter(
                    this@DashboardActivity
                ) { onItemClicked(it.id) }
            rvProduct.layoutManager =
                GridLayoutManager(this@DashboardActivity, 2, GridLayoutManager.VERTICAL, false)
            rvProduct.adapter = productAdapter
            viewModel.productState.observe(this@DashboardActivity) { state ->
                progressbar.isVisible = state.isLoading
                fabCart.isVisible = !state.isLoading
                if (state.error != null) {
                    Toast.makeText(this@DashboardActivity, state.error, Toast.LENGTH_SHORT).show()
                } else {
                    productAdapter.submitList(state.data)
                    tvActiveCategory.text = state.filter
                    viewModel.fetchCart()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setupView()
    }

    private fun onItemClicked(id: Int) {
        val intent = Intent(this, DetailProductActivity::class.java)
        intent.putExtra(Constant.ID, id)
        startActivity(intent)
    }

    private fun showProfile(user: User?) {
        val view: View = layoutInflater.inflate(R.layout.bottom_sheet_profile, null)

        val dialog = BottomSheetDialog(this)
        dialog.behavior.maxHeight =
            (0.8 * Resources.getSystem().displayMetrics.heightPixels).toInt()
        val username = view.findViewById<TextView>(R.id.tv_username)
        val fullname = view.findViewById<TextView>(R.id.tv_fullname)
        val email = view.findViewById<TextView>(R.id.tv_email)
        val address = view.findViewById<TextView>(R.id.tv_address)
        val phone = view.findViewById<TextView>(R.id.tv_phone)
        username.text = user?.username
        fullname.text = "${user?.name?.firstname} ${user?.name?.lastname}"
        email.text = user?.email
        address.text =
            "${user?.address?.number}, ${user?.address?.street} ${user?.address?.city} ${user?.address?.zipcode}"
        phone.text = user?.phone

        dialog.setOnCancelListener { obj: DialogInterface -> obj.dismiss() }
        dialog.setCancelable(true)
        dialog.setContentView(view)
        dialog.show()
    }

    private fun filterCategoryDialog() {
        val view: View = layoutInflater.inflate(R.layout.bottom_sheet_filter, null)

        val dialog = BottomSheetDialog(this)
        val rvCategory = view.findViewById<RecyclerView>(R.id.rv_category)
        rvCategory.setHasFixedSize(true)
        rvCategory.layoutManager = LinearLayoutManager(this)
        val categoryAdapter = CategoryAdapter(Constant.FILTER_LIST) {
            viewModel.filterProduct(it)
            dialog.dismiss()
        }
        rvCategory.adapter = categoryAdapter

        dialog.setOnCancelListener { obj: DialogInterface -> obj.dismiss() }
        dialog.setCancelable(true)
        dialog.setContentView(view)
        dialog.show()
    }

    private fun goToCart() {
        val intent = Intent(this, CartActivity::class.java)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.topbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.productState.observe(this@DashboardActivity) { state ->
            when (item.itemId) {
                R.id.profile -> {
                    showProfile(state.user)
                }

                R.id.logout -> {
                    val intent = Intent(this, LoginActivity::class.java)
                    viewModel.removeUser()
                    startActivity(intent)
                    finish()
                }

                else -> super.onOptionsItemSelected(item)
            }
        }
        return true
    }

    @SuppressLint("MissingSuperCall")
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            val a = Intent(Intent.ACTION_MAIN)
            a.addCategory(Intent.CATEGORY_HOME)
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(a)
        } else {
            this.doubleBackToExitPressedOnce = true
            Toast.makeText(this, "Tekan sekali lagi untuk keluar", Toast.LENGTH_SHORT).show()
            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            binding.fabCart.id -> goToCart()
            binding.btnFilter.id -> filterCategoryDialog()
        }
    }
}