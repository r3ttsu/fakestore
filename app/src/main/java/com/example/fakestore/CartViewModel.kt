package com.example.fakestore

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
        getCart()
    }

    private fun getCart() {
        viewModelScope.launch {
            val carts = repository.getCart()
            _cartState.value = _cartState.value?.copy(
                isLoading = false,
                data = carts
            )
        }
    }

    data class CartState(
        val isLoading: Boolean = true,
        val data: List<Cart> = emptyList(),
        val error: String? = null,
    )
}