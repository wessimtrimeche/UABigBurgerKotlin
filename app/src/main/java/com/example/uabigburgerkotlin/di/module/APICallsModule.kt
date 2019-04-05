package com.example.uabigburgerkotlin.di.module

import com.example.uabigburgerkotlin.BuildConfig
import com.example.uabigburgerkotlin.data.APICalls
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module(includes = [NetworkModule::class])
class APICallsModule {


    @Provides
    @Singleton
    fun getAPICalls(retrofit: Retrofit): APICalls = retrofit.create(APICalls::class.java)


    @Provides
    @Singleton
    fun getRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }
}