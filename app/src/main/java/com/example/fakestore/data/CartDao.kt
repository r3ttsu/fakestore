package com.example.fakestore.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CartDao {
    @Query("SELECT * FROM cart")
    suspend fun getCart(): List<Cart>

    @Insert
    suspend fun addToCart(cart: Cart)

    @Update
    suspend fun updateCart(cart: Cart)

    @Delete
    suspend fun deleteFromCart(cart: Cart)

    @Query("DELETE FROM cart")
    suspend fun clearCart()
}