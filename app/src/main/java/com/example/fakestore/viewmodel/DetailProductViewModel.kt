package com.example.fakestore.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakestore.model.Product
import com.example.fakestore.repository.CartRepository
import com.example.fakestore.repository.DetailProductRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailProductViewModel @Inject constructor(
    private val repository: DetailProductRepository,
    private val cartRepository: CartRepository
) :
    ViewModel() {
    private val _detailProductState = MutableLiveData(DetailProductState())
    val detailProductState: LiveData<DetailProductState> = _detailProductState

    fun getProductDetail(id: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getProductDetail(id)
                _detailProductState.value = _detailProductState.value?.copy(
                    isLoading = false,
                    data = response
                )
            } catch (e: Exception) {
                _detailProductState.value = _detailProductState.value?.copy(
                    isLoading = false,
                    error = "Terjadi kesalahan: ${e.message}"
                )
            }
        }
    }

    fun addToCart(product: Product) {
        viewModelScope.launch {
            try {
                val carts = cartRepository.getCart()
                val existingItem = carts.find { it.id == product.id }
                if(existingItem != null){
                    val newCart = existingItem.copy(
                        quantity = existingItem.quantity + 1
                    )
                    cartRepository.updateCart(newCart)
                } else {
                    val newItem = com.example.fakestore.data.Cart(
                        product.id,
                        product.title,
                        product.price,
                        product.image,
                        1
                    )
                    cartRepository.addToCart(newItem)
                }
                _detailProductState.value = _detailProductState.value?.copy(
                    message = "Berhasil menambahkan produk ke keranjang",
                )
            } catch (e: Exception){
                _detailProductState.value = _detailProductState.value?.copy(
                    error = "Terjadi kesalahan: ${e.message}"
                )
            }
        }
    }

    data class DetailProductState(
        val isLoading: Boolean = true,
        val data: Product? = null,
        val error: String? = null,
        val message: String? = null,
    )
}