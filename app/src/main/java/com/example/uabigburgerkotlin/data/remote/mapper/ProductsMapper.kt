package com.example.uabigburgerkotlin.data.remote.mapper

import com.example.uabigburgerkotlin.data.remote.dto.ECatalogProduct
import com.example.uabigburgerkotlin.data.remote.model.CatalogProductModel

object ProductsMapper {
    fun mapCatalogProducts(input: MutableList<ECatalogProduct>): MutableList<CatalogProductModel> {
        val catalogProductModelList = mutableListOf<CatalogProductModel>()
        for (element in input) {
            val catalogProductModel = CatalogProductModel(
                element.ref, element.title, element.description, element.thumbnail,
                element.price?.div(100)
            )
            catalogProductModelList.add(catalogProductModel)
        }
        return catalogProductModelList
    }
}