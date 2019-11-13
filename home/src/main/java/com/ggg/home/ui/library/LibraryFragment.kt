package com.ggg.home.ui.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.ggg.home.R
import com.ggg.home.ui.main.HomeBaseFragment
import timber.log.Timber

class LibraryFragment : HomeBaseFragment() {
    private lateinit var viewModel: LibraryViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_library, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.d("onActivityCreated")
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LibraryViewModel::class.java)
        navigationController.setTitle("Library")
    }

    override fun initObserver() {

    }

    override fun initEvent() {
    }

    companion object {
        val TAG = "LibraryFragment"
        @JvmStatic
        fun create() = LibraryFragment()
    }
}