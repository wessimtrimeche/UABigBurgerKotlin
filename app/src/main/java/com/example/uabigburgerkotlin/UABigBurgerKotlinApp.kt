package com.example.uabigburgerkotlin


import android.app.Application
import android.content.Context
import bigburger.ua.com.uabigburger.data.local.ProductDatabase
import bigburger.ua.com.uabigburger.data.provider.SharedPreferencesProvider
import com.example.uabigburgerkotlin.data.APICalls
import com.example.uabigburgerkotlin.data.client.RetrofitClient
import com.facebook.stetho.BuildConfig
import com.facebook.stetho.Stetho

class UABigBurgerKotlinApp : Application() {

    // TODO: static field keep reference for object which may cause leak memory which result crash :(
// TODO: try to use dagger and keep one reference for it to be used using @Singleton Annotations
    override fun onCreate() {
        super.onCreate()
        initStethoMonitoring()
        initInstances()
    }

    companion object {
        lateinit var productDatabase: ProductDatabase
        lateinit var preferences: SharedPreferencesProvider
        lateinit var service: APICalls
    }

    private fun initStethoMonitoring() {
        if (BuildConfig.DEBUG)
            Stetho.initializeWithDefaults(this)
    }

    //TODO: As you use dagger there is no need for those initialisations
    private fun initInstances() {
        productDatabase = ProductDatabase.getRoomDatabaseInstance(applicationContext)
        preferences = SharedPreferencesProvider.getSharedPreferencesInstance(
            applicationContext.getSharedPreferences(
                SharedPreferencesProvider.NAME,
                Context.MODE_PRIVATE
            )
        )
        service = RetrofitClient.getRetrofitInstance(applicationContext).create(APICalls::class.java)
    }
}