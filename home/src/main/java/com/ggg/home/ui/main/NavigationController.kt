package com.ggg.home.ui.main

import androidx.fragment.app.FragmentManager
import com.ggg.home.R
import com.ggg.home.data.model.ChapterHadRead
import com.ggg.home.data.model.ChapterModel
import com.ggg.home.data.model.ComicWithCategoryModel
import com.ggg.home.ui.category.CategoryFragment
import com.ggg.home.ui.comic_detail.ComicDetailFragment
import com.ggg.home.ui.home.HomeFragment
import com.ggg.home.ui.library.LibraryFragment
import com.ggg.home.ui.login.LoginFragment
import com.ggg.home.ui.register.RegisterFragment
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

    fun showViewComic(comicWithCategoryModel: ComicWithCategoryModel, listChapterModel: List<ChapterHadRead>, positionChapter: Int) {
        val fragment = ViewComicFragment.create(comicWithCategoryModel, listChapterModel, positionChapter)
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