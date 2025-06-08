package com.example.test.repository

import com.example.test.data.api.RetrofitClient
import com.example.test.data.model.ProductResponse
import retrofit2.Response

class ProductRepository {
    suspend fun getProductDetails(): Response<ProductResponse> {
        return RetrofitClient.api.getProductDetails()
    }
}
