package com.example.uabigburgerkotlin.module.productbasket

import com.example.uabigburgerkotlin.data.DatabaseManager
import com.example.uabigburgerkotlin.data.local.model.Product
import io.reactivex.disposables.Disposable
import timber.log.Timber

class ProductBasketPresenter(private val productBasketView: ProductBasketView) {
    private val databaseManager = DatabaseManager()
    private lateinit var disposable: Disposable

    internal fun getBasketProducts() {
        productBasketView.showProgress()
        disposable = databaseManager.getBasketProducts().subscribe({ result ->
            productBasketView.hideProgress()
            productBasketView.onFetchBasketProducts(result)
            productBasketView.onDestroy(disposable)
        }, { error ->
            Timber.e("Failed to fetch basket products : %s", error.message)
            productBasketView.onFetchBasketProductsFailure()
        })
    }

    internal fun removeProduct(product: Product) {
        productBasketView.showProgress()
        disposable = databaseManager.removeProductFromBasket(product).subscribe({
        }, { error -> Timber.e("Failed to remove product : %s", error.message) }, {
            productBasketView.hideProgress()
            productBasketView.onRemoveSuccess()
//dispose at completion
            productBasketView.onDestroy(disposable)
        })
    }
}