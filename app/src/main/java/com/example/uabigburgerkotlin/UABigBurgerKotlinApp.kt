package com.example.uabigburgerkotlin


import android.app.Application
import com.example.uabigburgerkotlin.di.component.DaggerUABigBurgerAppComponent
import com.example.uabigburgerkotlin.di.component.UABigBurgerAppComponent
import com.example.uabigburgerkotlin.di.module.ContextModule
import com.facebook.stetho.BuildConfig
import com.facebook.stetho.Stetho

class UABigBurgerKotlinApp : Application() {

    companion object {
        lateinit var uaBigBurgerAppComponent: UABigBurgerAppComponent
    }

    override fun onCreate() {
        super.onCreate()
        initStethoMonitoring()
        uaBigBurgerAppComponent = DaggerUABigBurgerAppComponent.builder()
            .contextModule(ContextModule(this))
            .build()
        uaBigBurgerAppComponent.inject(this)

    }


    private fun initStethoMonitoring() {
        if (BuildConfig.DEBUG)
            Stetho.initializeWithDefaults(this)
    }

}