package com.ggg.home.ui.main

import android.app.Activity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleRegistry
import com.ggg.common.ui.BaseActivity
import com.ggg.home.R
import com.ggg.home.ui.category.CategoryFragment
import com.ggg.home.ui.home.HomeFragment
import com.ggg.home.ui.library.LibraryFragment
import com.ggg.home.ui.user.UserFragment
import com.ncapdevi.fragnav.FragNavController
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity(), HasSupportFragmentInjector, FragNavController.RootFragmentListener {


    private val registry = LifecycleRegistry(this)
    override fun getLifecycle(): LifecycleRegistry {
        return registry
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return dispatch
    }

    override val numberOfRootFragments: Int = 1

    override fun getRootFragment(index: Int): Fragment {
        when (index) {
            0 -> return HomeFragment.create()
        }
        throw IllegalStateException("Need to send an index that we know")
    }

    @Inject
    lateinit var navigationController: NavigationController
    var fragNavController: FragNavController = FragNavController(this.supportFragmentManager, R.id.container)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fragments = listOf(
                HomeFragment.create())

        fragNavController.rootFragments = fragments
        fragNavController.initialize(0, savedInstanceState)

        initEvents()

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        fragNavController.onSaveInstanceState(outState!!)

    }

    override fun onBackPressed() {
        when {
            fragNavController.isRootFragment.not() -> {
                fragNavController.popFragment()
                hideSoftKeyboard()

                fragNavController.currentFrag?.let {
                    when (it::class.java.simpleName) {
                        HomeFragment.TAG -> {
                            bottomNavView.selectedItemId = R.id.navHome
                        }

                        CategoryFragment.TAG -> {
                            bottomNavView.selectedItemId = R.id.navCategory
                        }

                        LibraryFragment.TAG -> {
                            bottomNavView.selectedItemId = R.id.navLib
                        }

                        UserFragment.TAG -> {
                            bottomNavView.selectedItemId = R.id.navUser
                        }

                        else -> {

                        }
                    }
                }
            }
            else -> {
                normalBack()
            }
        }
    }

    fun normalBack() {
        super.onBackPressed()
    }

    fun hideSoftKeyboard() {
        if (this.currentFocus != null) {
            val inputMethodManager = this.getSystemService(
                    Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                    this.currentFocus!!.windowToken, 0)
        }
    }

    fun setTitle(title: String) {
//        toolbar_title.text = title
    }

    private fun initEvents() {
        bottomNavView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navHome -> {
                    fragNavController.currentFrag?.let {
                        if (it::class.java.simpleName != HomeFragment.TAG) {
                            navigationController.showHome()
                        }
                    }
                    true
                }

                R.id.navCategory -> {
                    fragNavController.currentFrag?.let {
                        if (it::class.java.simpleName != CategoryFragment.TAG) {
                            navigationController.showCategory()
                        }
                    }
                    true
                }

                R.id.navLib -> {
                    fragNavController.currentFrag?.let {
                        if (it::class.java.simpleName != LibraryFragment.TAG) {
                            navigationController.showLibrary()
                        }
                    }
                    true
                }

                R.id.navUser -> {
                    fragNavController.currentFrag?.let {
                        if (it::class.java.simpleName != UserFragment.TAG) {
                            navigationController.showUser()
                        }
                    }
                    true
                }

                else -> false
            }
        }
    }
}
