package com.example.uabigburgerkotlin.module.productslist

import com.example.uabigburgerkotlin.UABigBurgerKotlinApp
import com.example.uabigburgerkotlin.data.DataManager
import io.reactivex.disposables.Disposable
import timber.log.Timber
import javax.inject.Inject

class ProductsListPresenter(val productsListView: ProductsListView) {

    @Inject
    lateinit var dataManager: DataManager
    private lateinit var disposable: Disposable

    init {

        UABigBurgerKotlinApp.uaBigBurgerAppComponent.inject(this)

    }

    internal fun getFullProducts() {
        productsListView.showProgress()
        disposable = dataManager.getCatalogProducts().subscribe({ result ->
            Timber.e("Subscription succeeded.")
            productsListView.hideProgress()
            productsListView.onFetchCatalogProducts(result)
        }, { error ->
            productsListView.hideProgress()
            productsListView.onFetchCatalogProductsFailed()
            Timber.e("Error loading products : %s", error.getLocalizedMessage())
        }, {
            productsListView.hideProgress()
            Timber.e("Product loaded.")
//dispose at completion
            productsListView.onDestroy(disposable)
        })
    }
}