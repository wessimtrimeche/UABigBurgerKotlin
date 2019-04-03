package com.example.uabigburgerkotlin.data.local.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey
    val ref: Int,
    val title: String?,
    val description: String?,
    val thumbnail: String?,
    val price: Float = 0f
) {

    @Ignore
    var basketId: Long? = null

    override fun toString(): String {
        return "CatalogProduct(ref='$ref', title=$title, description=$description, thumbnail=$thumbnail, price=$price, basketId=$basketId)"
    }
}