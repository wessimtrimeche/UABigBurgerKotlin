package com.example.uabigburgerkotlin.data.provider

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SharedPreferencesProvider @Inject constructor(context: Context) {
    val sharedPreferencesProvider: SharedPreferences

    init {
        this.sharedPreferencesProvider = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferencesProvider.getBoolean(key, defaultValue)
    }

    fun putBoolean(key: String, value: Boolean) {
        val editor = sharedPreferencesProvider.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    companion object {
        var NAME = "SHARED_PREFERENCES"

    }
}