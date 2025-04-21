package com.example.fakestore.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakestore.data.Cart
import com.example.fakestore.repository.CartRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class CartViewModel @Inject constructor(private val repository: CartRepository) : ViewModel() {
    private val _cartState = MutableLiveData(CartState())
    val cartState: LiveData<CartState> = _cartState

    init {
        loadCart()
    }

    private fun loadCart() {
        viewModelScope.launch {
            val carts = repository.getCart()
            val total = repository.getTotal(carts)
            _cartState.value = _cartState.value?.copy(
                isLoading = false,
                data = carts,
                totalPrice = total
            )
        }
    }

    fun updateQty(cart: Cart) {
        viewModelScope.launch {
            repository.updateCart(cart)
            loadCart()
        }
    }

    fun removeItem(cart: Cart) {
        viewModelScope.launch {
            repository.removeItem(cart)
            loadCart()
        }
    }

    fun checkout(){
        viewModelScope.launch {
            repository.clearCart()
        }
    }

    data class CartState(
        val isLoading: Boolean = true,
        val data: List<Cart> = emptyList(),
        val totalPrice: Double? = 0.0,
        val error: String? = null,
    )
}