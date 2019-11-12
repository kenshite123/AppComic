package com.ggg.app.di

import com.ggg.app.ui.init.SplashFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by TuanNguyen on 12/12/17.
 */
@Module
abstract class InitActivityFragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeSplashFragmentInjector(): SplashFragment

}