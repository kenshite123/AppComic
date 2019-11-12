package com.ggg.app.di

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.ggg.home.di.HomeModule
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import javax.inject.Singleton

/**
 * Created by TuanNguyen on 12/12/17.
 */
@Module(includes = arrayOf(ViewModelModule::class, HomeModule::class))
class AppModules {

    @Provides @Singleton fun sharedPrefs(app: Application): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(app)
    }

    @Provides
    @Singleton
    fun provideOkHttpCache(application: Application): Cache {
        val cacheSize = 10 * 1024 * 1024 // 10 MiB
        val cache = Cache(application.cacheDir, cacheSize.toLong())
        return cache
    }
}