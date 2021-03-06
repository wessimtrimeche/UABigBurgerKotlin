package com.example.uabigburgerkotlin.data

import com.example.uabigburgerkotlin.data.local.ProductDatabase
import com.example.uabigburgerkotlin.data.local.model.Product
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseManager @Inject constructor(val productDatabase: ProductDatabase) {

    fun getBasketProducts(): Observable<List<Product>> {
        return productDatabase.productsDAO().getProducts().subscribeOn(Schedulers.io()).observeOn(
            AndroidSchedulers.mainThread()
        ).toObservable()
    }


    //The observable.create is a workaround for room restriction on return types (no observables allowed)
    fun addProductToBasket(product: Product): Observable<Any> {
        return Observable.create { subscriber: ObservableEmitter<Any> ->
            productDatabase.productsDAO().addProduct(product)
            subscriber.onComplete()
        }
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    //The observable.create is a workaround for room restriction on return types (no observables allowed)
    fun removeProductFromBasket(product: Product): Observable<Any> {
        return Observable.create { subscriber: ObservableEmitter<Any> ->
            productDatabase.productsDAO().deleteProduct(product)
            subscriber.onComplete()
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }
}