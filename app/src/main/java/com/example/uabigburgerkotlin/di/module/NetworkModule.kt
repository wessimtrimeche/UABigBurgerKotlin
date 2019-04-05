package com.example.uabigburgerkotlin.di.module

import android.content.Context
import com.example.uabigburgerkotlin.data.client.ConnectivityInterceptor
import com.facebook.stetho.okhttp3.StethoInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module(includes = [ContextModule::class])
class NetworkModule {


    @Provides
    @Singleton
    fun getFile(context: Context): File {
        val file = File(context.cacheDir, "okhttp_cache")
        if (!file.exists())
            file.mkdirs()
        return file
    }

    @Provides
    @Singleton
    fun getCache(cacheFile: File): Cache {
        val cache = Cache(cacheFile, 10 * 1000 * 1000)
        return cache
    }

    @Provides
    @Singleton
    fun getOkHttpClient(context: Context, cache: Cache): OkHttpClient {
        return OkHttpClient.Builder()
            .writeTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .cache(cache)
            .addInterceptor(ConnectivityInterceptor(context))
            .addNetworkInterceptor(StethoInterceptor())
            .build()
    }

}