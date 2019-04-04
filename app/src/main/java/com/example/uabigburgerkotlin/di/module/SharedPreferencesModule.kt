package com.example.uabigburgerkotlin.di.module

import android.content.Context
import com.example.uabigburgerkotlin.data.provider.SharedPreferencesProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ContextModule::class])
class SharedPreferencesModule {


    @Provides
    @Singleton
    internal fun providePreferencesHelper(context: Context): SharedPreferencesProvider =
        SharedPreferencesProvider(context)


}