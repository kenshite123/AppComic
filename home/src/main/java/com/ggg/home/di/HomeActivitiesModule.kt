package com.ggg.home.di

import com.ggg.home.ui.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class HomeActivitiesModule {

    @HomeActivityScope
    @ContributesAndroidInjector(modules = arrayOf(InitHomeActivityFragmentModule::class))
    abstract fun contributeHomeActivityInjector(): MainActivity

}