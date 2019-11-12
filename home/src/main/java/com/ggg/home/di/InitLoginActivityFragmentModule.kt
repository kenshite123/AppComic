package com.ggg.home.di

import com.ggg.home.ui.login.login.LoginFragment
import com.ggg.home.ui.login.passcode.PassCodeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class InitLoginActivityFragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeLoginFragmentInjector(): LoginFragment

    @ContributesAndroidInjector
    abstract fun contributePassCodeFragmentInjector(): PassCodeFragment
}