package com.example.uabigburgerkotlin.data.local.dao

import android.arch.persistence.room.*
import com.example.uabigburgerkotlin.data.local.model.Product
import io.reactivex.Flowable

@Dao
interface ProductsDAO {
    @Query("select * from products")
    fun getProducts(): Flowable<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addProduct(product: Product): Long

    @Delete
    fun deleteProduct(product: Product)
}