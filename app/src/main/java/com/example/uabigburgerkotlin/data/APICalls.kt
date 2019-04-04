package com.example.uabigburgerkotlin.data

import com.example.uabigburgerkotlin.data.remote.dto.ECatalogProduct
import io.reactivex.Observable
import retrofit2.http.GET

interface APICalls {

    @GET("catalog")
    fun getCatalogProducts(): Observable<MutableList<ECatalogProduct>>

}