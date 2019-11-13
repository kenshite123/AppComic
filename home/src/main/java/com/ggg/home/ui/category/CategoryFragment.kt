package com.ggg.home.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.ggg.home.R
import com.ggg.home.ui.main.HomeBaseFragment
import timber.log.Timber

class CategoryFragment : HomeBaseFragment() {
    private lateinit var viewModel: CategoryViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.d("onActivityCreated")
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CategoryViewModel::class.java)
        navigationController.setTitle("Category")
    }

    override fun initObserver() {

    }

    override fun initEvent() {
    }

    companion object {
        val TAG = "CategoryFragment"
        @JvmStatic
        fun create() = CategoryFragment()
    }
}