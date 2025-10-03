package com.example.ecommerceapp.data.local.remote

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.reflect.KClass


@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productId: Int,
    val name: String,
    val price: Double,
    var quantity: Int
)