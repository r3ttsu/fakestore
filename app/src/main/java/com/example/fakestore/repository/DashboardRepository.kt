package com.example.fakestore.repository

import android.util.Log
import com.example.fakestore.ApiService
import com.example.fakestore.model.Cart
import com.example.fakestore.model.CartProduct
import com.example.fakestore.model.Product
import com.google.gson.Gson
import javax.inject.Inject

class DashboardRepository @Inject constructor(private val service: ApiService) {
    suspend fun getProduct(): List<Product> {
        return service.getProducts()
    }

    suspend fun getCart(id: Int): List<Cart> {
        return service.getCart(id)
    }

    fun filterProduct(products: List<Product>, category: String): List<Product> {
        return if (category == "all products") {
            products
        } else {
            products.filter { it.category == category }
        }
    }

    fun mapCart(
        products: List<Product>,
        cartProducts: List<CartProduct>
    ): List<com.example.fakestore.data.Cart> {
        val finalCart: MutableList<com.example.fakestore.data.Cart> = mutableListOf()
        Log.d(this::class.simpleName, "products: ${Gson().toJson(products)} cartProducts: ${Gson().toJson(cartProducts)}")
        cartProducts.map { cartProduct ->
            products.map { product ->
                if (product.id == cartProduct.productId) {
                    finalCart.add(
                        com.example.fakestore.data.Cart(
                            product.id,
                            product.title,
                            product.price,
                            product.image,
                            cartProduct.quantity
                        )
                    )
                }
            }
        }
        return finalCart
    }
}