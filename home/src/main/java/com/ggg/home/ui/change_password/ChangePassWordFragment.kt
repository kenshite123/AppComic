package com.ggg.home.ui.change_password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.ggg.home.R
import com.ggg.home.ui.main.HomeBaseFragment
import kotlinx.android.synthetic.main.fragment_change_password.*
import timber.log.Timber

class ChangePassWordFragment: HomeBaseFragment() {
    private lateinit var viewModel: ChangePassWordViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_change_password, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.d("onActivityCreated")
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ChangePassWordViewModel::class.java)

        initViews()
        initObserver()
        initEvent()
    }

    private fun initViews() {
        hideActionBar()
        hideBottomNavView()

    }

    override fun initObserver() {

    }

    override fun initEvent() {
        btnChangePass.setOnClickListener {
//            viewModel.getUserId()
        }
    }

    companion object{
        val TAG = "ChangePassWordFragment"
        @JvmStatic
        fun create() = ChangePassWordFragment()
    }

}