package com.example.fakestore

import com.example.fakestore.model.Login
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

private val retrofit = Retrofit.Builder().baseUrl("https://fakestoreapi.com/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val service: ApiService = retrofit.create(ApiService::class.java)

interface ApiService {
    @GET("users")
    suspend fun getAllUsers(): List<Login>
}