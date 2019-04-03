package com.example.uabigburgerkotlin.data

import com.example.uabigburgerkotlin.UABigBurgerKotlinApp
import com.example.uabigburgerkotlin.data.remote.mapper.ProductsMapper
import com.example.uabigburgerkotlin.data.remote.model.CatalogProductModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DataManager {


    fun getCatalogProducts(): Observable<MutableList<CatalogProductModel>> {

        return UABigBurgerKotlinApp.service.getCatalogProducts()
            .map { e ->
                ProductsMapper.mapCatalogProducts(e)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    }


}