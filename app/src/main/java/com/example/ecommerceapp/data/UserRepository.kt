package com.example.ecommerceapp.data

import com.example.ecommerceapp.data.local.remote.AppDatabase
import com.example.ecommerceapp.data.local.remote.CartItem
import com.example.ecommerceapp.data.local.remote.Product
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class UserRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: AppDatabase
) {

    // Firebase Auth functions
    fun getCurrentUser() = auth.currentUser

    suspend fun login(email: String, password: String, useMock: Boolean = false): Result<Boolean> {
        return if (useMock) {
            // Mock credentials
            if (email == "test@example.com" && password == "Test@1234") {
                Result.success(true)
            } else {
                Result.failure(Exception("Invalid mock credentials"))
            }
        } else {
            try {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                Result.success(result.user != null)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    fun logout() {
        auth.signOut()
    }

    // Product functions
    suspend fun insertProducts(products: List<Product>) = db.productDao().insertProducts(products)

    suspend fun getProducts(): List<Product> = db.productDao().getAllProducts()

    // Cart functions
    suspend fun addToCart(item: CartItem) = db.cartDao().addCartItem(item)

    suspend fun updateCart(item: CartItem) = db.cartDao().updateCartItem(item)

    suspend fun removeCartItem(item: CartItem) = db.cartDao().removeCartItem(item)

    suspend fun getCartItems(): List<CartItem> = db.cartDao().getAllCartItems()

    suspend fun clearCart() = db.cartDao().clearCart()
}