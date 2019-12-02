package com.ggg.home.ui.comment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.ggg.home.R
import com.ggg.home.ui.main.HomeBaseFragment
import kotlinx.android.synthetic.main.fragment_comment.*
import org.jetbrains.anko.bundleOf
import timber.log.Timber

class CommentFragment : HomeBaseFragment() {
    private lateinit var viewModel: CommentViewModel
    var isFirstLoad = true

    companion object {
        val TAG = "CommentFragment"
        @JvmStatic
        fun create(comicId: Long) : CommentFragment {
            val fragment = CommentFragment()
            val bundle = bundleOf(
                    "comicId" to comicId
            )
            fragment.arguments = bundle
            return fragment
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.d("onActivityCreated")
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CommentViewModel::class.java)
        showActionBar()
        hideBottomNavView()
        setTitleActionBar(R.string.TEXT_COMMENT)

        initViews()
        initEvent()
    }

    private fun initViews() {
        fabSend.bringToFront()
    }

    override fun initObserver() {

    }

    override fun initEvent() {
        fabSend.setOnClickListener {
            if (edComment.text.toString().isNullOrEmpty()) {
                showMsg(R.string.TEXT_ERROR_NO_CONTENT_YET)
            } else {
                
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (isFirstLoad) {
            initObserver()
            isFirstLoad = false
        }
    }
}