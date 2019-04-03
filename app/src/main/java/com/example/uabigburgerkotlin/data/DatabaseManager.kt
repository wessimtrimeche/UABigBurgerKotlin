package com.example.uabigburgerkotlin.data

import com.example.uabigburgerkotlin.UABigBurgerKotlinApp
import com.example.uabigburgerkotlin.data.local.model.Product
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DatabaseManager {

    fun getBasketProducts(): Observable<List<Product>> {
        return UABigBurgerKotlinApp.productDatabase.productsDAO().getProducts().subscribeOn(Schedulers.io()).observeOn(
            AndroidSchedulers.mainThread()
        ).toObservable()
    }

    fun addProductToBasket(product: Product): Observable<Any> {
        return Observable.create { subscriber: ObservableEmitter<Any> ->
            UABigBurgerKotlinApp.productDatabase.productsDAO().addProduct(product)
            subscriber.onComplete()
        }
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    fun removeProductFromBasket(product: Product): Observable<Any> {
        return Observable.create({ subscriber: ObservableEmitter<Any> ->
            UABigBurgerKotlinApp.productDatabase.productsDAO().deleteProduct(product)
            subscriber.onComplete()
        }
        ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }
}