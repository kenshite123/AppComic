package com.ggg.home.di

import com.ggg.home.ui.category.CategoryFragment
import com.ggg.home.ui.comic_detail.ComicDetailFragment
import com.ggg.home.ui.home.HomeFragment
import com.ggg.home.ui.library.LibraryFragment
import com.ggg.home.ui.login.LoginFragment
import com.ggg.home.ui.user.UserFragment
import com.ggg.home.ui.view_comic.ViewComicFragment
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

    @ContributesAndroidInjector
    abstract fun contributeLoginFragmentInjector(): LoginFragment

    @ContributesAndroidInjector
    abstract fun contributeComicDetailFragmentInjector(): ComicDetailFragment

    @ContributesAndroidInjector
    abstract fun contributeViewComicFragmentInjector(): ViewComicFragment
}