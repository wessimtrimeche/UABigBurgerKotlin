package com.example.uabigburgerkotlin.module.productdetails

import com.example.uabigburgerkotlin.module.base.BaseView

interface ProductDetailsView : BaseView {
    fun onProductAdded()
    fun onRemoveSuccess()
}