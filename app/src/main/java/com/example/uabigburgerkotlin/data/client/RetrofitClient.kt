package com.example.uabigburgerkotlin.data.client

import android.content.Context
import com.example.uabigburgerkotlin.BuildConfig
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {

    companion object {
        lateinit var retrofit: Retrofit

        fun getRetrofitInstance(context: Context): Retrofit {
            retrofit = retrofit2.Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(createOkHttpClient(context))
                .build()

            return retrofit
        }

        private fun createOkHttpClient(context: Context): OkHttpClient {
            val okHttpBuilder = OkHttpClient.Builder()
                .addNetworkInterceptor(provideNetworkInterceptor())
                .addInterceptor(ConnectivityInterceptor(context))
            return okHttpBuilder.build()
        }

        private fun provideNetworkInterceptor(): Interceptor {
            return StethoInterceptor()
        }
    }


}