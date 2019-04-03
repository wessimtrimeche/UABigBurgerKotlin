package com.example.uabigburgerkotlin.module.productbasket

import com.example.uabigburgerkotlin.data.local.model.Product
import com.example.uabigburgerkotlin.module.base.BaseView

interface ProductBasketView : BaseView {
    fun onFetchBasketProducts(basketProducts: List<Product>)
    fun onFetchBasketProductsFailure()
    fun onRemoveSuccess()
}