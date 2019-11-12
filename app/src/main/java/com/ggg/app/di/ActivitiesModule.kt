package com.ggg.app.di

import com.ggg.app.ui.init.InitialActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by TuanNguyen on 12/12/17.
 */
@Module
abstract class ActivitiesModule {
    @ActivityScope
    @ContributesAndroidInjector(modules = arrayOf(InitActivityFragmentModule::class))
    abstract fun contributeInitialAcitivtyInjector(): InitialActivity
}