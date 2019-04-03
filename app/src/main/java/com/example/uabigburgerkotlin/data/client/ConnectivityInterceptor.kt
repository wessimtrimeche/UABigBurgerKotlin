package com.example.uabigburgerkotlin.data.client

import android.content.Context
import android.net.ConnectivityManager
import com.example.uabigburgerkotlin.R
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ConnectivityInterceptor(private val context: Context) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        if (!isConnectedToNetwork(context)) {

            throw NoConnectivityException(context)
        } else {
            val response = chain.proceed(chain.request())
            return response
        }
    }

    companion object {

        fun isConnectedToNetwork(context: Context): Boolean {
            lateinit var connectivityManager: ConnectivityManager
            connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val activeNetwork = connectivityManager.activeNetworkInfo
            var isConnected = activeNetwork != null && activeNetwork.isConnected


            return isConnected
        }
    }
}


internal class NoConnectivityException(var context: Context) : IOException() {

    override val message: String?
        get() = context.getString(R.string.network_error)
}