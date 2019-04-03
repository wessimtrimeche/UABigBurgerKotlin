package com.example.uabigburgerkotlin.data

import com.example.uabigburgerkotlin.UABigBurgerKotlinApp
import com.example.uabigburgerkotlin.data.remote.dto.ECatalogProduct
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DataManager {


    fun getCatalogProducts(): Observable<MutableList<ECatalogProduct>> {

        return UABigBurgerKotlinApp.service.getCatalogProducts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    }

}