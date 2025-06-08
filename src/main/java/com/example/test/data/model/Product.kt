package com.example.test.data.model

data class Product(
    val id: String,
    val name: String,
    val sku: String,
    val price: String,
    val final_price: String,
    val brand_name: String,
    val image: String,
    val description: String,
    val configurable_option: List<ConfigurableOption>,
)

