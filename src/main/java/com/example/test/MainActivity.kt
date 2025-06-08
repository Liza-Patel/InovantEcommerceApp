package com.example.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.test.ui.theme.ProductScreen
import com.example.test.ui.theme.TestTheme
import com.example.test.viewmodel.ProductViewModel

class MainActivity : ComponentActivity() {

    // Use viewModels() delegate to get ViewModel instance
    private val viewModel: ProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Trigger data fetch
        viewModel.fetchProduct()

        // Set Compose UI content
        setContent {
            TestTheme {
                ProductScreen(viewModel = viewModel)
            }
        }
    }
}

