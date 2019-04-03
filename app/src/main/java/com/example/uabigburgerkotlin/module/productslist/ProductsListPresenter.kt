package com.example.uabigburgerkotlin.module.productslist

import com.example.uabigburgerkotlin.data.DataManager
import io.reactivex.disposables.Disposable
import timber.log.Timber

class ProductsListPresenter(private val productsListView: ProductsListView) {
    private val dataManager = DataManager()
    private lateinit var disposable: Disposable

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