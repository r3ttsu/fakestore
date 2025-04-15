package com.example.fakestore

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakestore.model.Product
import com.google.gson.Gson
import kotlinx.coroutines.launch
import javax.inject.Inject

class DashboardViewModel @Inject constructor(private val repository: DashboardRepository): ViewModel() {
    private val _productState = MutableLiveData(DashboardState())
    val productState: LiveData<DashboardState> = _productState

    init {
        fetchProducts()
    }

    private fun fetchProducts(){
        viewModelScope.launch {
            try {
                Log.d(this::class.simpleName, "masuk try")
                val response = repository.getProduct()
                _productState.value = _productState.value?.copy(
                    isLoading = false,
                    data = response,
                    error = null
                )
                Log.d(this::class.simpleName, "_productState.value: ${Gson().toJson(_productState.value)}")
            } catch (e: Exception){
                Log.d(this::class.simpleName, "masuk catch")
                _productState.value = _productState.value?.copy(
                    isLoading = false,
                    data = emptyList(),
                    error = "Error fetch products: ${e.message}"
                )
                Log.d(this::class.simpleName, "_productState.value: ${Gson().toJson(_productState.value)}")
            }
        }
    }

    data class DashboardState(
        val isLoading: Boolean = true,
        val data: List<Product> = emptyList(),
        val error: String? = null,
    )
}