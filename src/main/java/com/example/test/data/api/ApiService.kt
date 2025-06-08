package com.example.test.data.api

import com.example.test.data.model.ProductResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("rest/V1/productdetails/6701/253620")
    suspend fun getProductDetails(
        @Query("lang") lang: String = "en",
        @Query("store") store: String = "KWD"
    ): Response<ProductResponse>
}
