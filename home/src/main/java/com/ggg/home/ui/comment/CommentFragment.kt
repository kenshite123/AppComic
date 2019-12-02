package com.ggg.home.ui.comment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ggg.common.GGGAppInterface
import com.ggg.common.vo.Status
import com.ggg.home.R
import com.ggg.home.data.model.post_param.WriteCommentBody
import com.ggg.home.data.model.response.LoginResponse
import com.ggg.home.ui.main.HomeBaseFragment
import com.ggg.home.utils.Constant
import kotlinx.android.synthetic.main.fragment_comment.*
import org.jetbrains.anko.bundleOf
import timber.log.Timber
import kotlin.math.log

class CommentFragment : HomeBaseFragment() {
    private lateinit var viewModel: CommentViewModel
    var isFirstLoad = true
    private var loginResponse: LoginResponse? = null
    var comicId: Long = 0

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

        comicId = arguments!!["comicId"] as Long

        if (GGGAppInterface.gggApp.loginResponse != null) {
            loginResponse = GGGAppInterface.gggApp.loginResponse as LoginResponse
        }

        initViews()
        initEvent()
    }

    private fun initViews() {
        fabSend.bringToFront()
    }

    override fun initObserver() {
        viewModel.writeCommentResult.observe(this, Observer {
            loading(it)
            if (it.status == Status.SUCCESS) {
                showMsg("Success")
                navigationController.popToBackStack()
            } else if (it.status == Status.ERROR) {
                it.message?.let {
                    showDialog(it)
                }
            }
        })
    }

    override fun initEvent() {
        fabSend.setOnClickListener {
            if (checkValidSendComment()) {
                val token = loginResponse!!.tokenType + loginResponse!!.accessToken
                var writeCommentBody = WriteCommentBody()
                writeCommentBody.topicType = Constant.TOPIC_TYPE_COMMENT
                writeCommentBody.comicId = this.comicId
                writeCommentBody.content = edComment.text.toString()

                val data = hashMapOf<String, Any>(
                        "token" to token,
                        "writeCommentBody" to writeCommentBody
                )

                viewModel.writeComment(data)
            }
        }
    }

    private fun checkValidSendComment() : Boolean {
        if (edComment.text.toString().isEmpty()) {
            showMsg(R.string.TEXT_ERROR_NO_CONTENT_YET)
            return false
        }

        if (loginResponse == null) {
            showDialog(R.string.TEXT_ERROR_NO_LOGIN_TO_COMMENT)
            return false
        }

        return true
    }

    override fun onResume() {
        super.onResume()
        if (isFirstLoad) {
            initObserver()
            isFirstLoad = false
        }
    }
}