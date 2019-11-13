package com.ggg.home.ui.main

import androidx.fragment.app.FragmentManager
import com.ggg.home.R
import com.ggg.home.ui.category.CategoryFragment
import com.ggg.home.ui.home.HomeFragment
import com.ggg.home.ui.library.LibraryFragment
import com.ggg.home.ui.user.UserFragment
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
        when {
            fragNavController.isRootFragment.not() -> {
                hideSoftKeyboard()
                fragNavController.popFragment()
            }
            else -> {

            }
        }
    }
    private fun hideSoftKeyboard() {
        weakActivity.get()?.hideSoftKeyboard()
    }
    fun setTitle(title: String) {
        weakActivity.get()?.setTitle(title)
    }

    //endregion


}