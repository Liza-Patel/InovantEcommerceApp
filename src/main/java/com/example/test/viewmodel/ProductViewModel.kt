package com.example.test.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test.data.model.Product
import com.example.test.data.model.ProductResponse
import com.example.test.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class ProductViewModel : ViewModel() {

    private val repository = ProductRepository()

    private val _product = MutableStateFlow<Product?>(null)
    val product: StateFlow<Product?> = _product

    fun fetchProduct() {
        viewModelScope.launch {
            val response: Response<ProductResponse> = repository.getProductDetails()
            if (response.isSuccessful) {
                _product.value = response.body()?.data
            } else {
                // Optional: log or handle error
            }
        }
    }
}
