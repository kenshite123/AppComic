package com.ggg.home.di

import com.ggg.home.ui.category.CategoryFragment
import com.ggg.home.ui.home.HomeFragment
import com.ggg.home.ui.library.LibraryFragment
import com.ggg.home.ui.user.UserFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class InitHomeActivityFragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeHomeFragmentInjector(): HomeFragment

    @ContributesAndroidInjector
    abstract fun contributeCategoryFragmentInjector(): CategoryFragment

    @ContributesAndroidInjector
    abstract fun contributeLibraryFragmentInjector(): LibraryFragment

    @ContributesAndroidInjector
    abstract fun contributeUserFragmentInjector(): UserFragment
}