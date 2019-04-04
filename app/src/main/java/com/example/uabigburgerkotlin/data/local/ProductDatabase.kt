package com.example.uabigburgerkotlin.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.example.uabigburgerkotlin.data.local.dao.ProductsDAO
import com.example.uabigburgerkotlin.data.local.model.Product

@Database(entities = [Product::class], version = 1, exportSchema = false)
abstract class ProductDatabase : RoomDatabase() {
    abstract fun productsDAO(): ProductsDAO

}