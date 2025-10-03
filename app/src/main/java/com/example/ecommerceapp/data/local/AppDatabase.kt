package com.example.ecommerceapp.data.local.remote


import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Product::class, CartItem::class],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao
}
