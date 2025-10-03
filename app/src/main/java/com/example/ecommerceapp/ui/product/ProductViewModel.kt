package com.example.ecommerceapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.UserRepository
import com.example.ecommerceapp.data.local.remote.CartItem
import com.example.ecommerceapp.data.local.remote.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.first

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    init {
        insertStaticProducts()
        loadProducts()
    }
    suspend fun getProductById(id: Int): Product? {
        val productList: List<Product> = userRepository.getProducts() // direct list
        return productList.firstOrNull { it.id == id } // single product
    }

    private fun insertStaticProducts() {
        viewModelScope.launch {
            val existing = userRepository.getProducts()
            if (existing.isEmpty()) {
                val staticProducts = listOf(
                    Product(
                        name = "Samsung Galaxy S23",
                        price = 69999.0,
                        description = "Flagship smartphone with AMOLED display and high performance.",
                        imageUrl = "apple_1"
                    ),
                    Product(
                        name = "Apple iPhone 15",
                        price = 99999.0,
                        description = "Latest iPhone with A17 chip and advanced camera system.",
                        imageUrl = "canon_1"
                    ),
                    Product(
                        name = "OnePlus 12",
                        price = 54999.0,
                        description = "High-end smartphone with fast charging and smooth display.",
                        imageUrl = "realme_1"
                    ),
                    Product(
                        name = "Sony WH-1000XM5 Headphones",
                        price = 24999.0,
                        description = "Premium noise-cancelling over-ear headphones.",
                        imageUrl = "dell_1"
                    ),
                    Product(
                        name = "Apple AirPods Pro 2",
                        price = 19999.0,
                        description = "Wireless earbuds with active noise cancellation.",
                        imageUrl = "fitbit_1"
                    ),
                    Product(
                        name = "Realme Buds Q2",
                        price = 2499.0,
                        description = "Affordable true wireless earbuds with good sound quality.",
                        imageUrl = "realme_1"
                    ),
                    Product(
                        name = "Dell Inspiron 15 Laptop",
                        price = 54999.0,
                        description = "15-inch laptop with Intel i5 processor and 8GB RAM.",
                        imageUrl = "dell_1"
                    ),
                    Product(
                        name = "Samsung Galaxy Tab S9",
                        price = 64999.0,
                        description = "High-performance tablet with S Pen support.",
                        imageUrl = "sony_wh"
                    ),
                    Product(
                        name = "Canon EOS 250D Camera",
                        price = 45999.0,
                        description = "DSLR camera with 24.1MP sensor and full HD video recording.",
                        imageUrl = "canon _1"
                    ),
                    Product(
                        name = "Fitbit Charge 6",
                        price = 12999.0,
                        description = "Fitness tracker with heart rate and sleep monitoring.",
                        imageUrl = "fitbit_1"
                    )
                )

                userRepository.insertProducts(staticProducts)
                loadProducts() // ✅ reload after insert
            }
        }
    }


    fun loadProducts() {
        viewModelScope.launch {
            _products.value = userRepository.getProducts()
        }
    }

    fun addToCart(product: Product) {
        viewModelScope.launch {
            userRepository.addToCart(
                CartItem(
                    productId = product.id,
                    name = product.name,
                    price = product.price,
                    quantity = 1
                )
            )
        }
    }
}
