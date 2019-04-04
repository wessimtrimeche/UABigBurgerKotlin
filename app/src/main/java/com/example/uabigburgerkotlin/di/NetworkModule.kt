package com.example.uabigburgerkotlin.di

import android.content.Context
import com.example.uabigburgerkotlin.data.client.ConnectivityInterceptor
import com.facebook.stetho.okhttp3.StethoInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.io.File
import java.util.concurrent.TimeUnit


@Module(includes = [ContextModule::class])
class NetworkModule {


    @Provides
    fun getInterceptor() = StethoInterceptor()


    @Provides
    fun getFile(context: Context): File {
        val file = File(context.cacheDir, "okhttp_cache")
        if (!file.exists())
            file.mkdirs()
        return file
    }

    @Provides
    fun getCache(cacheFile: File): Cache {
        val cache = Cache(cacheFile, 10 * 1000 * 1000)
        return cache
    }

    @Provides
    fun getOkHttpClient(context: Context, interceptor: StethoInterceptor, cache: Cache): OkHttpClient {
        return OkHttpClient.Builder()
            .writeTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .cache(cache)
            .addInterceptor(ConnectivityInterceptor(context))
            .addNetworkInterceptor(interceptor)
            .build()
    }

}