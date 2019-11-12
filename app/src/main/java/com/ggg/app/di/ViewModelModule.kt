package com.ggg.app.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ggg.app.ui.init.InitViewModel
import com.ggg.common.di.ViewModelKey

import com.ggg.common.utils.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(InitViewModel::class)
    abstract fun bindInitViewModel(viewModel: InitViewModel): ViewModel

}
