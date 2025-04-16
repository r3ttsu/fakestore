package com.example.fakestore.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CartDao {
    @Query("SELECT * FROM cart")
    suspend fun getCart(): List<Cart>

    @Insert
    suspend fun addToCart(cart: Cart)

    @Query("DELETE FROM user")
    suspend fun clearCart()
}