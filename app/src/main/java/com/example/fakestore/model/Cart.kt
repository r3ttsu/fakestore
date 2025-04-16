package com.example.fakestore.model

data class Cart(
    val userId: Int,
    val date: String,
    val products: List<CartProduct>,
    val __v: Int,
)

data class CartProduct(
    val productId: Int,
    val quantity: Int,
)