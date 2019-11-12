package com.ggg.home.ui.login

import androidx.fragment.app.FragmentManager
import com.ggg.home.R
import com.ggg.home.ui.home.HomeFragment
import com.ggg.home.ui.promotion.PromotionFragment
import com.ncapdevi.fragnav.FragNavController
import java.lang.ref.WeakReference
import javax.inject.Inject

class LoginNavigationController @Inject constructor(activity: LoginActivity) {


    //region fragment

    fun showLogin() {
        val fragment = HomeFragment.create()
        fragNavController.replaceFragment(fragment)

    }

    fun showPassCode() {
        val fragment = PromotionFragment.create()
        fragNavController.pushFragment(fragment)
    }

    //endregion


    //region properties & constructor

    private val containerId = R.id.container
    private lateinit var fragmentManager: FragmentManager
    private var weakActivity: WeakReference<LoginActivity> = WeakReference(activity)
    private lateinit var fragNavController: FragNavController

    init {
        fragmentManager = weakActivity.get()!!.supportFragmentManager
        fragNavController = weakActivity.get()!!.fragNavController
    }

    //endregion

    //region support

    fun getSupportManager(): FragmentManager {
        return fragmentManager
    }

    fun popToBackStack() {
        if (weakActivity.get() != null) {
            hideSoftKeyboard()
            if (fragmentManager.fragments.size > 0)
                fragmentManager.popBackStack()
        }

    }

    private fun hideSoftKeyboard() {
        weakActivity.get()?.hideSoftKeyboard()
    }

    fun setTitle(title: String) {
        weakActivity.get()?.title = title
    }

    //endregion


}