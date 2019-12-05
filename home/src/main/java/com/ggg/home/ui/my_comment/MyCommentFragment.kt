package com.ggg.home.ui.my_comment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.ggg.home.R
import com.ggg.home.ui.main.HomeBaseFragment
import com.ggg.home.ui.user.UserViewModel
import timber.log.Timber

class MyCommentFragment : HomeBaseFragment() {
    private lateinit var viewModel: UserViewModel
    var isFirstLoad = true

    companion object {
        val TAG = "MyCommentFragment"
        @JvmStatic
        fun create() = MyCommentFragment()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_comment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.d("onActivityCreated")
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel::class.java)
        showActionBar()
        hideBottomNavView()
        setTitleActionBar(R.string.TEXT_MY_COMMENT)

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