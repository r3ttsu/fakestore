package com.example.fakestore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakestore.model.Product
import com.example.fakestore.repository.DetailProductRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailProductViewModel @Inject constructor(private val repository: DetailProductRepository) :
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

    data class DetailProductState(
        val isLoading: Boolean = true,
        val data: Product? = null,
        val error: String? = null,
    )
}