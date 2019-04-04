package com.example.uabigburgerkotlin.module.productslist

import com.example.uabigburgerkotlin.data.remote.model.CatalogProductModel
import com.example.uabigburgerkotlin.module.base.BaseView

interface ProductsListView : BaseView {
    fun onFetchCatalogProducts(catalogProducts: MutableList<CatalogProductModel>)
    fun onFetchCatalogProductsFailed()
}