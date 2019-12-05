package com.ggg.app.ui.init

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleRegistry
import com.ggg.app.R
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
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
        navigationController.showSplash()
    }
}
