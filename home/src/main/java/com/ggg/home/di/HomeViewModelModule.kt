package com.ggg.home.di

import androidx.lifecycle.ViewModel
import com.ggg.common.di.ViewModelKey
import com.ggg.home.ui.home.HomeViewModel
import com.ggg.home.ui.promotion.PromotionViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class HomeViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(homeViewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PromotionViewModel::class)
    abstract fun bindPromotionViewModel(promotionViewModel: PromotionViewModel): ViewModel
}