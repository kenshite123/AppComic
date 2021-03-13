package com.ggg.home.ui.main

import android.content.Intent
import androidx.fragment.app.FragmentManager
import com.ggg.home.R
import com.ggg.home.data.model.*
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
import com.ncapdevi.fragnav.FragNavController
import java.lang.ref.WeakReference
import javax.inject.Inject

class NavigationController @Inject constructor(activity: MainActivity) {

    //region fragment

    fun showHome(){
        val fragment = HomeFragment.create()
        fragNavController.pushFragment(fragment)
    }

    fun showCategory(){
        val fragment = CategoryFragment.create()
        fragNavController.pushFragment(fragment)
    }

    fun showCategoryAndLatestUpdate() {
        val fragment = CategoryAndLatestUpdateFragment.create()
        fragNavController.pushFragment(fragment)
    }

    fun showLatestUpdateNew() {
        val fragment = com.ggg.home.ui.category_and_latest_update.LatestUpdateFragment.create()
        fragNavController.pushFragment(fragment)
    }

    fun showCategoryFragmentNew() {
        val fragment = com.ggg.home.ui.category_and_latest_update.CategoryFragment.create()
        fragNavController.pushFragment(fragment)
    }

    fun showLibrary(){
        val fragment = LibraryFragment.create()
        fragNavController.pushFragment(fragment)
    }

    fun showUser(){
        val fragment = UserFragment.create()
        fragNavController.pushFragment(fragment)
    }

    fun showLogin(){
        val fragment = LoginFragment.create()
        fragNavController.pushFragment(fragment)
    }

    fun showRegister() {
        val fragment = RegisterFragment.create()
        fragNavController.pushFragment(fragment)
    }
    fun showComicDetail(comicWithCategoryModel: ComicWithCategoryModel){
        val fragment = ComicDetailFragment.create(comicWithCategoryModel)
        fragNavController.pushFragment(fragment)
    }

    fun showComicDetail(comicId: String){
        val fragment = ComicDetailFragment.create(comicId)
        fragNavController.pushFragment(fragment)
    }

    fun showViewComic(comicWithCategoryModel: ComicWithCategoryModel, listChapterModel: List<ChapterHadRead>,
                      positionChapter: Int, isLoadLatest: Boolean) {
        val fragment = ViewComicFragment.create(comicWithCategoryModel, listChapterModel, positionChapter, isLoadLatest)
        fragNavController.pushFragment(fragment)
    }

    fun showCategoryDetail(categoryOfComicModel: CategoryOfComicModel) {
        val fragment = CategoryDetailFragment.create(categoryOfComicModel)
        fragNavController.pushFragment(fragment)
    }

    fun showLatestUpdate() {
        val fragment = LatestUpdateFragment.create()
        fragNavController.pushFragment(fragment)
    }

    fun showSearch() {
        val fragment = SearchFragment.create()
        fragNavController.pushFragment(fragment)
    }

    fun showFavorite() {
        val fragment = FavoriteFragment.create()
        fragNavController.pushFragment(fragment)
    }

    fun showRank() {
        val fragment = RankFragment.create()
        fragNavController.pushFragment(fragment)
    }

    fun showChangePassWord() {
        val fragment = ChangePassWordFragment.create()
        fragNavController.pushFragment(fragment)
    }

    fun showReply(commentModel: CommentModel) {
        val fragment = ReplyFragment.create(commentModel)
        fragNavController.pushFragment(fragment)
    }

    fun showReply(commentId: Long) {
        val fragment = ReplyFragment.create(commentId)
        fragNavController.pushFragment(fragment)
    }

    fun showComment(comicId: Long) {
        val fragment = CommentFragment.create(comicId)
        fragNavController.pushFragment(fragment)
    }

    fun showMyComment() {
        val fragment = MyCommentFragment.create()
        fragNavController.pushFragment(fragment)
    }

    fun showCommentOfChap(comicWithCategoryModel: ComicWithCategoryModel, chapterModel: ChapterModel) {
        val fragment = CommentOfChapFragment.create(comicWithCategoryModel, chapterModel)
        fragNavController.pushFragment(fragment)
    }

    fun showLaunchScreen() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        weakActivity.get()!!.startActivity(intent)
    }

    fun showChooseChapToDownloadImage(comicId: Long) {
        val fragment = ChooseChapToDownloadImageFragment.create(comicId)
        fragNavController.pushFragment(fragment)
    }

    //endregion

    //region properties & constructor

    private val containerId = R.id.container
    private var fragmentManager: FragmentManager
    private var weakActivity: WeakReference<MainActivity> = WeakReference(activity)
    private var fragNavController: FragNavController
    init {
        fragmentManager = weakActivity.get()!!.supportFragmentManager
        fragNavController = weakActivity.get()!!.fragNavController
    }

    //endregion

    //region support

    fun getSupportManager(): FragmentManager {
        return fragmentManager
    }

    fun popToRoot(){
        hideSoftKeyboard()
        fragNavController.clearStack()
    }

    fun popToBackStack() {
        weakActivity.get()!!.onBackPressed()
    }
    private fun hideSoftKeyboard() {
        weakActivity.get()?.hideSoftKeyboard()
    }

    //endregion


}