package com.example.uabigburgerkotlin.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class ContextModule(private val context: Context) {

    @Provides
    @Singleton
    fun getContext() = context

}