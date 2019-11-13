package com.ggg.home.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ggg.common.di.Injectable
import com.ggg.common.ui.BaseFragment
import com.ggg.common.ui.utils.BaseCellAdapter
import javax.inject.Inject

open class HomeBaseFragment: BaseFragment() {
    @Inject
    lateinit var navigationController: NavigationController
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
    open fun initObserver(){

    }
    open fun initEvent(){

    }
}