package com.example.fakestore.repository

import com.example.fakestore.data.Cart
import com.example.fakestore.data.CartDao
import javax.inject.Inject

class CartRepository @Inject constructor(private val cartDao: CartDao) {
    suspend fun addToCart(cart: Cart) = cartDao.addToCart(cart)

    suspend fun getCart(): List<Cart> = cartDao.getCart()

    suspend fun updateCart(cart: Cart) = cartDao.updateCart(cart)

    suspend fun removeItem(cart: Cart) = cartDao.deleteFromCart(cart)

    suspend fun clearCart() = cartDao.clearCart()

    fun getTotal(data: List<Cart>): Double{
        var total = 0.0
        data.forEach{
            total += it.quantity * it.price
        }

        return total
    }
}