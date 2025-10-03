package com.example.ecommerceapp.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.UserRepository
import com.example.ecommerceapp.data.local.remote.CartItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    init {
        loadCart()
    }

    fun loadCart() {
        viewModelScope.launch {
            _cartItems.value = userRepository.getCartItems()
        }
    }

    fun updateQuantity(item: CartItem, newQty: Int) {
        viewModelScope.launch {
            val updatedItem = item.copy(quantity = newQty)
            userRepository.updateCart(updatedItem)
            loadCart()
        }
    }

    fun removeItem(item: CartItem) {
        viewModelScope.launch {
            userRepository.removeCartItem(item)
            loadCart()
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            userRepository.clearCart()
            loadCart()
        }
    }

    fun getTotals(): Triple<Double, Double, Double> {
        val subtotal = _cartItems.value.sumOf { it.price * it.quantity }
        val tax = subtotal * 0.1 // 10% tax
        val grandTotal = subtotal + tax
        return Triple(subtotal, tax, grandTotal)
    }
}
