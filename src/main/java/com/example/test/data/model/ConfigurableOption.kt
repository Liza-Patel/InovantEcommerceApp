package com.example.test.data.model

data class ConfigurableOption(
    val attribute_id: Int,
    val type: String,
    val attribute_code: String,
    val attributes: List<ProductAttribute>
)