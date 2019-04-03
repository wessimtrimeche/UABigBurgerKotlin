//package com.example.uabigburgerkotlin.data.remote.mapper
//
//import com.example.uabigburgerkotlin.data.remote.dto.ECatalogProduct
//import com.example.uabigburgerkotlin.data.remote.model.CatalogProductModel
//
//class ProductsMapper {
//    companion object {
//
//        fun mapCatalogProducts(input:ArrayList<ECatalogProduct>):ArrayList<CatalogProductModel> {
//
//            for (element in input)
//            {
//                val catalogProductModel = CatalogProductModel(element.ref,element.title,element.description,element.thumbnail,
//                    element.price!! /100)
//                cardModels.add(catalogProductModel)
//            }
//            return cardModels
//        }
//    }
//}