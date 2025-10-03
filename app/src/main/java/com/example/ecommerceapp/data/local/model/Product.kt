package com.example.ecommerceapp.data.local.remote

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.reflect.KClass

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // ✅ auto-generate
    val name: String,
    val price: Double,
    val description: String,
    val imageUrl: String
    )