package com.ggg.home.di

import androidx.lifecycle.ViewModel
import com.ggg.common.di.ViewModelKey
import com.ggg.home.ui.category.CategoryViewModel
import com.ggg.home.ui.category_detail.CategoryDetailViewModel
import com.ggg.home.ui.change_password.ChangePassWordViewModel
import com.ggg.home.ui.comic_detail.ComicDetailViewModel
import com.ggg.home.ui.comment.CommentViewModel
import com.ggg.home.ui.favorite.FavoriteViewModel
import com.ggg.home.ui.home.HomeViewModel
import com.ggg.home.ui.latest_update.LatestUpdateViewModel
import com.ggg.home.ui.library.LibraryViewModel
import com.ggg.home.ui.login.LoginViewModel
import com.ggg.home.ui.rank.RankViewModel
import com.ggg.home.ui.register.RegisterViewModel
import com.ggg.home.ui.reply.ReplyViewModel
import com.ggg.home.ui.search.SearchViewModel
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
    @ViewModelKey(RegisterViewModel::class)
    abstract fun bindRegisterViewModel(registerViewModel: RegisterViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ComicDetailViewModel::class)
    abstract fun bindComicDetailViewModel(comicDetailViewModel: ComicDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ViewComicViewModel::class)
    abstract fun bindViewComicViewModel(viewComicViewModel: ViewComicViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CategoryDetailViewModel::class)
    abstract fun bindCategoryDetailViewModel(categoryDetailViewModel: CategoryDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindSearchViewModel(searchViewModel: SearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LatestUpdateViewModel::class)
    abstract fun bindLatestUpdateViewModel(latestUpdateViewModel: LatestUpdateViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FavoriteViewModel::class)
    abstract fun bindFavoriteViewModel(favoriteViewModel: FavoriteViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RankViewModel::class)
    abstract fun bindRankViewModel(rankViewModel: RankViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ReplyViewModel::class)
    abstract fun bindReplyViewModel(replyViewModel: ReplyViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CommentViewModel::class)
    abstract fun bindCommentViewModel(commentViewModel: CommentViewModel): ViewModel

    @ViewModelKey(ChangePassWordViewModel::class)
    abstract fun bindChangePassWordViewModel(changePassWordViewModel: ChangePassWordViewModel): ViewModel
}