package com.example.uabigburgerkotlin.data.remote.model


data class CatalogProductModel(
    val ref: String,
    val title: String?,
    val description: String?,
    val thumbnail: String?,
    val price: Float
)