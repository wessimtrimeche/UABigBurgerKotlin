package com.example.uabigburgerkotlin.data

import com.example.uabigburgerkotlin.data.remote.mapper.ProductsMapper
import com.example.uabigburgerkotlin.data.remote.model.CatalogProductModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataManager @Inject constructor(private val apiCalls: APICalls) {


    fun getCatalogProducts(): Observable<MutableList<CatalogProductModel>> {

        return apiCalls.getCatalogProducts()
            .map { e ->
                ProductsMapper.mapCatalogProducts(e)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    }
}