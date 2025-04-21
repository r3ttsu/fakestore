package com.example.fakestore.repository

import com.example.fakestore.ApiService
import com.example.fakestore.model.Cart
import com.example.fakestore.model.CartProduct
import com.example.fakestore.model.Product
import javax.inject.Inject

class DashboardRepository @Inject constructor(private val service: ApiService) {
    suspend fun getProduct(): List<Product> {
        return service.getProducts()
    }

    suspend fun getCart(id: Int): List<Cart> {
        return service.getCart(id)
    }

    suspend fun getCategories(): List<String>{
        return service.getProductCategories()
    }

    suspend fun getProductByCategory(products: List<Product>, category: String): List<Product>{
        return if (category == "all products") {
            products
        } else {
            service.getProductByCategory(category)
        }
    }

    fun mapCart(
        products: List<Product>,
        cartProducts: List<CartProduct>
    ): List<com.example.fakestore.data.Cart> {
        val finalCart: MutableList<com.example.fakestore.data.Cart> = mutableListOf()
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