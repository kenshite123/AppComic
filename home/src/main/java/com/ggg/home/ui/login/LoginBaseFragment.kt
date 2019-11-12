package com.ggg.home.ui.login

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.ggg.common.ui.BaseFragment
import com.ggg.home.ui.main.NavigationController
import javax.inject.Inject

open class LoginBaseFragment: BaseFragment() {
    @Inject
    lateinit var navigationController: LoginNavigationController
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initObserver()
        initEvent()
    }
    open fun initObserver(){

    }
    open fun initEvent(){

    }
}