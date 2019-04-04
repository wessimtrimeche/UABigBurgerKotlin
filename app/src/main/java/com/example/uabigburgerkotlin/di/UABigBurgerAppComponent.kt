package com.example.uabigburgerkotlin.di

import com.example.uabigburgerkotlin.UABigBurgerKotlinApp
import com.example.uabigburgerkotlin.module.productbasket.ProductBasketActivity
import com.example.uabigburgerkotlin.module.productbasket.ProductBasketPresenter
import com.example.uabigburgerkotlin.module.productdetails.ProductDetailsActivity
import com.example.uabigburgerkotlin.module.productdetails.ProductDetailsPresenter
import com.example.uabigburgerkotlin.module.productslist.ProductsListPresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [APICallsModule::class, ContextModule::class, DatabaseModule::class, NetworkModule::class, SharedPreferencesModule::class])
interface UABigBurgerAppComponent {

    fun inject(uaBigBurgerKotlinApp: UABigBurgerKotlinApp)
    fun inject(productBasketActivity: ProductBasketActivity)
    fun inject(productDetailsActivity: ProductDetailsActivity)
    fun inject(productBasketPresenter: ProductBasketPresenter)
    fun inject(productsListPresenter: ProductsListPresenter)
    fun inject(productDetailsPresenter: ProductDetailsPresenter)
}