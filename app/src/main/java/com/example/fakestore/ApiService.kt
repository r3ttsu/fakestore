package com.example.fakestore

import com.example.fakestore.model.Cart
import com.example.fakestore.model.Login
import com.example.fakestore.model.Product
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private val retrofit = Retrofit.Builder().baseUrl("https://fakestoreapi.com/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val service: ApiService = retrofit.create(ApiService::class.java)

interface ApiService {
    @GET("users")
    suspend fun getAllUsers(): List<Login>

    @GET("products")
    suspend fun getProducts(): List<Product>

    @GET("carts")
    suspend fun getCart(@Query("id") id: Int): List<Cart>

    @GET("products/{id}")
    suspend fun getProductDetail(@Path("id") id: Int): Product
}