package com.example.uabigburgerkotlin.module.productdetails

import com.example.uabigburgerkotlin.UABigBurgerKotlinApp
import com.example.uabigburgerkotlin.data.DatabaseManager
import com.example.uabigburgerkotlin.data.local.model.Product
import io.reactivex.disposables.Disposable
import timber.log.Timber
import javax.inject.Inject

class ProductDetailsPresenter(private val productDetailsView: ProductDetailsView) {
    @Inject
    lateinit var databaseManager: DatabaseManager
    private lateinit var disposable: Disposable

    init {

        UABigBurgerKotlinApp.uaBigBurgerAppComponent.inject(this)

    }

    internal fun addProduct(product: Product) {
        productDetailsView.showProgress()
        disposable = databaseManager.addProductToBasket(product).subscribe({
        }, { error -> Timber.e("Failed to add product : %s", error.message) }, {
            productDetailsView.hideProgress()
            productDetailsView.onProductAdded()
            productDetailsView.onDestroy(disposable)
        })
    }

    internal fun removeProduct(product: Product) {
        productDetailsView.showProgress()
        disposable = databaseManager.removeProductFromBasket(product).subscribe({
        }, { error -> Timber.e("Failed to remove product : %s", error.message) }, {
            productDetailsView.hideProgress()
            productDetailsView.onRemoveSuccess()
//dispose at completion
            productDetailsView.onDestroy(disposable)
        })
    }
}