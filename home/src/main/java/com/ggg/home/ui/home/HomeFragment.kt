package com.ggg.home.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.ggg.home.R
import com.ggg.home.ui.main.HomeBaseFragment
import kotlinx.android.synthetic.main.fragment_home.*
import timber.log.Timber

class HomeFragment : HomeBaseFragment() {

    private lateinit var viewModel: HomeViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.d("onActivityCreated")
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)
        navigationController.setTitle("Home")
    }

    override fun initObserver() {

    }

    override fun initEvent() {
        demoBtn.setOnClickListener {
            navigationController.showPromotion()
        }
    }

    companion object {
        val TAG = "HomeFragment"
        @JvmStatic
        fun create() =
                HomeFragment()
    }
}
