package com.example.fakestore

import com.example.fakestore.model.Product
import javax.inject.Inject

class DashboardRepository @Inject constructor(private val service: ApiService) {
    suspend fun getProduct(): List<Product> {
        return service.getProducts()
    }
}