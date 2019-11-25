package com.ggg.home.di

import androidx.lifecycle.ViewModel
import com.ggg.common.di.ViewModelKey
import com.ggg.home.ui.category.CategoryViewModel
import com.ggg.home.ui.comic_detail.ComicDetailViewModel
import com.ggg.home.ui.home.HomeViewModel
import com.ggg.home.ui.library.LibraryViewModel
import com.ggg.home.ui.login.LoginViewModel
import com.ggg.home.ui.user.UserViewModel
import com.ggg.home.ui.view_comic.ViewComicViewModel
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
    @ViewModelKey(CategoryViewModel::class)
    abstract fun bindCategoryViewModel(categoryViewModel: CategoryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LibraryViewModel::class)
    abstract fun bindLibraryViewModel(libraryViewModel: LibraryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UserViewModel::class)
    abstract fun bindUserViewModel(userViewModel: UserViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(loginViewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ComicDetailViewModel::class)
    abstract fun bindComicDetailViewModel(comicDetailViewModel: ComicDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ViewComicViewModel::class)
    abstract fun bindViewComicViewModel(viewComicViewModel: ViewComicViewModel): ViewModel

}