package com.example.uabigburgerkotlin.di.module

import android.arch.persistence.room.Room
import android.content.Context
import com.example.uabigburgerkotlin.R
import com.example.uabigburgerkotlin.data.local.ProductDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module(includes = [ContextModule::class])
class DatabaseModule {

    @Provides
    @Singleton
    fun getProductDatabase(context: Context): ProductDatabase {

        val productDatabase = Room.databaseBuilder(
            context,
            ProductDatabase::class.java,
            context.getString(R.string.product_database_name)
        ).build()

        return productDatabase

    }

}