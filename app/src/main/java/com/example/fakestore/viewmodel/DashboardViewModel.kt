package com.example.fakestore.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakestore.Constant
import com.example.fakestore.data.User
import com.example.fakestore.model.Product
import com.example.fakestore.repository.CartRepository
import com.example.fakestore.repository.DashboardRepository
import com.example.fakestore.repository.UserRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class DashboardViewModel @Inject constructor(
    private val repository: DashboardRepository,
    private val userRepository: UserRepository,
    private val cartRepository: CartRepository,
) : ViewModel() {
    private val _productState = MutableLiveData(DashboardState())
    val productState: LiveData<DashboardState> = _productState
    private var allProducts: List<Product> = emptyList()

    init {
        fetchProducts()
        fetchUser()
    }

    private fun fetchProducts() {
        viewModelScope.launch {
            try {
                val response = repository.getProduct()
                allProducts = response
                _productState.value = _productState.value?.copy(
                    isLoading = false,
                    data = response,
                    error = null
                )
            } catch (e: Exception) {
                _productState.value = _productState.value?.copy(
                    isLoading = false,
                    data = emptyList(),
                    error = "Error fetch products: ${e.message}"
                )
            }
        }
    }

    private fun fetchUser() {
        viewModelScope.launch {
            val user = userRepository.getUser()
            _productState.value = _productState.value?.copy(
                user = user,
            )
        }
    }

    fun fetchCart(){
        viewModelScope.launch {
            val user = _productState.value?.user
            val carts = user?.let { repository.getCart(it.id) }
            if (cartRepository.getCart().isEmpty()) {
                val myCart = carts?.find { it.userId == user.id }
                val finalCart =
                    myCart?.let { repository.mapCart(allProducts, it.products) }
                finalCart?.map { itemFinalCart ->
                    cartRepository.addToCart(itemFinalCart)
                }
            }
        }
    }

    fun filterProduct(category: String) {
        val filtered = repository.filterProduct(allProducts, category)
        _productState.value = filtered.let {
            _productState.value?.copy(
                data = it,
                filter = category
            )
        }
    }

    fun removeUser() {
        viewModelScope.launch {
            userRepository.deleteUser()
            cartRepository.clearCart()
        }
    }

    data class DashboardState(
        val isLoading: Boolean = true,
        val data: List<Product> = emptyList(),
        val user: User? = null,
        val filter: String = Constant.FILTER.ALL,
        val error: String? = null,
    )
}