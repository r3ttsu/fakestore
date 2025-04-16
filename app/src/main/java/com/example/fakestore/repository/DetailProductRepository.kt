package com.example.fakestore.repository

import com.example.fakestore.ApiService
import com.example.fakestore.model.Product
import javax.inject.Inject

class DetailProductRepository @Inject constructor(private val service: ApiService) {
    suspend fun getProductDetail(id: Int): Product{
        return service.getProductDetail(id)
    }
}