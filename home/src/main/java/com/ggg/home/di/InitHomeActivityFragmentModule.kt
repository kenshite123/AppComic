package com.ggg.home.di

import com.ggg.home.ui.home.HomeFragment
import com.ggg.home.ui.promotion.PromotionFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class InitHomeActivityFragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeHomeFragmentInjector(): HomeFragment

    @ContributesAndroidInjector
    abstract fun contributePromotionFragmentInjector(): PromotionFragment
}