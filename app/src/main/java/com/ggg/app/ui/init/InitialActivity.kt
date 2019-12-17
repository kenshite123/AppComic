package com.ggg.app.ui.init

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleRegistry
import com.ggg.app.R
import com.ggg.home.utils.Constant
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import timber.log.Timber
import javax.inject.Inject

class InitialActivity : AppCompatActivity() , HasSupportFragmentInjector {
    private val registry = LifecycleRegistry(this)
    override fun getLifecycle(): LifecycleRegistry {
        return registry
    }

    @Inject lateinit var dispatch: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return dispatch
    }

    @Inject lateinit var navigationController: NavigationController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial)
        supportActionBar?.hide()

        val extras = intent.extras
        val type = extras?.getString("type").toString()
        var comicId = extras?.getString("comicId").toString()

        var isShowComicDetail = false
        if (Constant.TYPE_SHOW_COMIC_DETAIL == type) {
            isShowComicDetail = true
        }

        if (isShowComicDetail) {
            navigationController.showSplash(isShowComicDetail, comicId)
        } else {
            navigationController.showSplash()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 15 && resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, "Hello World", Toast.LENGTH_SHORT).show()
        }
    }
}
