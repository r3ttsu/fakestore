package com.example.fakestore.repository

import com.example.fakestore.data.Cart
import com.example.fakestore.data.CartDao
import javax.inject.Inject

class CartRepository @Inject constructor(private val cartDao: CartDao) {
    suspend fun addToCart(cart: Cart) = cartDao.addToCart(cart)

    suspend fun getCart(): List<Cart> = cartDao.getCart()

    suspend fun updateCart(cart: Cart) = cartDao.updateCart(cart)

    suspend fun clearCart() = cartDao.clearCart()
}