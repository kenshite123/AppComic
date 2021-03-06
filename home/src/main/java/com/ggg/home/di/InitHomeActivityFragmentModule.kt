package com.ggg.home.di

import com.ggg.home.ui.category.CategoryFragment
import com.ggg.home.ui.category_and_latest_update.CategoryAndLatestUpdateFragment
import com.ggg.home.ui.category_detail.CategoryDetailFragment
import com.ggg.home.ui.change_password.ChangePassWordFragment
import com.ggg.home.ui.choose_chap_to_download_image.ChooseChapToDownloadImageFragment
import com.ggg.home.ui.comic_detail.ComicDetailFragment
import com.ggg.home.ui.comment.CommentFragment
import com.ggg.home.ui.comment_of_chap.CommentOfChapFragment
import com.ggg.home.ui.favorite.FavoriteFragment
import com.ggg.home.ui.home.HomeFragment
import com.ggg.home.ui.latest_update.LatestUpdateFragment
import com.ggg.home.ui.library.LibraryFragment
import com.ggg.home.ui.login.LoginFragment
import com.ggg.home.ui.my_comment.MyCommentFragment
import com.ggg.home.ui.rank.RankFragment
import com.ggg.home.ui.register.RegisterFragment
import com.ggg.home.ui.reply.ReplyFragment
import com.ggg.home.ui.search.SearchFragment
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
    abstract fun comtributeRegisterFragmentInjector(): RegisterFragment

    @ContributesAndroidInjector
    abstract fun contributeComicDetailFragmentInjector(): ComicDetailFragment

    @ContributesAndroidInjector
    abstract fun contributeViewComicFragmentInjector(): ViewComicFragment

    @ContributesAndroidInjector
    abstract fun contributeCategoryDetailFragmentInjector(): CategoryDetailFragment

    @ContributesAndroidInjector
    abstract fun contributeSearchFragmentInjector(): SearchFragment

    @ContributesAndroidInjector
    abstract fun contributeLatestUpdateFragmentInjector(): LatestUpdateFragment

    @ContributesAndroidInjector
    abstract fun contributeFavoriteFragmentInjector(): FavoriteFragment

    @ContributesAndroidInjector
    abstract fun contributeRankFragmentInjector(): RankFragment

    @ContributesAndroidInjector
    abstract fun contributeChangePassWordFragmentInjector(): ChangePassWordFragment

    @ContributesAndroidInjector
    abstract fun contributeReplyFragmentInjector(): ReplyFragment

    @ContributesAndroidInjector
    abstract fun contributeCommentFragmentInjector(): CommentFragment

    @ContributesAndroidInjector
    abstract fun contributeMyCommentFragmentInjector(): MyCommentFragment

    @ContributesAndroidInjector
    abstract fun contributeCommentOfChapFragmentInjector(): CommentOfChapFragment

    @ContributesAndroidInjector
    abstract fun contributeCategoryAndLatestUpdateFragmentInjector(): CategoryAndLatestUpdateFragment

    @ContributesAndroidInjector
    abstract fun contributeChooseChapToDownloadImageFragmentInjector(): ChooseChapToDownloadImageFragment

    @ContributesAndroidInjector
    abstract fun contributeLatestUpdateFragmentNewInjector(): com.ggg.home.ui.category_and_latest_update.LatestUpdateFragment

    @ContributesAndroidInjector
    abstract fun contributeCategoryFragmentNewInjector(): com.ggg.home.ui.category_and_latest_update.CategoryFragment
}