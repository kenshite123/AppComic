package com.ggg.home.di

import android.app.Application
import androidx.room.Room
import com.ggg.home.data.local.HomeDB
import com.ggg.home.data.remote.HomeRetrofitProvider
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import javax.inject.Singleton

@Module(includes = arrayOf(HomeViewModelModule::class, HomeActivitiesModule::class))
class HomeModule {

    @Provides
    @Singleton
    fun provideRetrofitProvider(cache: Cache): HomeRetrofitProvider {
        return HomeRetrofitProvider(cache)
    }

    @Singleton
    @Provides
    fun provideConnectDb(app: Application): HomeDB {
        return Room.databaseBuilder(app, HomeDB::class.java, "home.db")
                .addMigrations(HomeDB.migration12, HomeDB.migration23)
                .build()
    }

}