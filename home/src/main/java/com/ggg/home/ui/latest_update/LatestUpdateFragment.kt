package com.ggg.home.ui.latest_update

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.ggg.home.R
import com.ggg.home.ui.main.HomeBaseFragment
import timber.log.Timber

class LatestUpdateFragment : HomeBaseFragment() {
    private lateinit var viewModel: LatestUpdateViewModel
    var isFirstLoad = true

    companion object {
        val TAG = "LatestUpdateFragment"
        @JvmStatic
        fun create() = LatestUpdateFragment()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_latest_update, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.d("onActivityCreated")
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LatestUpdateViewModel::class.java)
        showActionBar()
        hideBottomNavView()
        setTitleActionBar(R.string.TEXT_COMIC_LATEST_UPDATE)

        initViews()
        initEvent()
    }

    private fun initViews() {

    }

    override fun initObserver() {

    }

    override fun initEvent() {
    }

    override fun onResume() {
        super.onResume()
        if (isFirstLoad) {
            initObserver()
            isFirstLoad = false
        }
    }
}