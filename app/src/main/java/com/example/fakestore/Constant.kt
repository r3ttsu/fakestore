package com.example.fakestore

object Constant {
    const val ID: String = "id"

    object FILTER {
        const val ALL: String = "all products"
        const val MENS_CLOTHING: String = "men's clothing"
        const val JEWELERY: String = "jewelery"
        const val ELECTRONICS: String = "electronics"
        const val WOMENS_CLOTHING: String = "women's clothing"
    }

    val FILTER_LIST: List<String> = listOf(
        FILTER.ALL,
        FILTER.MENS_CLOTHING,
        FILTER.JEWELERY,
        FILTER.ELECTRONICS,
        FILTER.WOMENS_CLOTHING
    )
}