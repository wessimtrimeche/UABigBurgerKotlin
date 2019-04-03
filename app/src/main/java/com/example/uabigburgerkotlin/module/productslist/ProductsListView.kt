package com.example.uabigburgerkotlin.module.productslist

import com.example.uabigburgerkotlin.data.remote.dto.ECatalogProduct
import com.example.uabigburgerkotlin.module.base.BaseView

interface ProductsListView : BaseView {
    fun onFetchCatalogProducts(catalogProducts: MutableList<ECatalogProduct>)
    fun onFetchCatalogProductsFailed()
}