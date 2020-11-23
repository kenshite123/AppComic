package com.ggg.home.ui.my_comment

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ggg.common.GGGAppInterface
import com.ggg.common.utils.StringUtil
import com.ggg.common.vo.Status
import com.ggg.home.R
import com.ggg.home.data.model.CommentModel
import com.ggg.home.data.model.response.LoginResponse
import com.ggg.home.ui.adapter.ListMyCommentAdapter
import com.ggg.home.ui.main.HomeBaseFragment
import com.ggg.home.ui.user.UserViewModel
import com.ggg.home.utils.Constant
import kotlinx.android.synthetic.main.fragment_my_comment.*
import timber.log.Timber

class MyCommentFragment : HomeBaseFragment() {
    private lateinit var viewModel: MyCommentViewModel
    var isFirstLoad = true
    private var listComment: List<CommentModel> = listOf()
    lateinit var listMyCommentAdapter: ListMyCommentAdapter
    lateinit var llManager: LinearLayoutManager
    var isLoadMore = true
    var items = 50
    var page = 0
    var positionCommentDeleted = 0

    var loginResponse: LoginResponse? = null
    var token = ""

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
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MyCommentViewModel::class.java)
        isLoadMore = true
        showActionBar()
        hideBottomNavView()
        setTitleActionBar(R.string.TEXT_MY_COMMENT)

        loginResponse = GGGAppInterface.gggApp.loginResponse as LoginResponse?
        token = loginResponse?.tokenType + loginResponse?.accessToken

        initViews()
        initEvent()
        loadDataMyComment()
    }

    private fun initViews() {
        listComment = listOf()
        llManager = LinearLayoutManager(context!!, RecyclerView.VERTICAL, false)
        listMyCommentAdapter = ListMyCommentAdapter(context!!, this, listComment)
        rvListMyComment.setHasFixedSize(false)
        rvListMyComment.layoutManager = llManager
        rvListMyComment.adapter = listMyCommentAdapter
    }

    override fun initObserver() {
        viewModel.getListMyCommentResult.observe(this, Observer {
            loading(it)
            if (it.status == Status.SUCCESS) {
                it.data?.let {
                    val list = this.listComment.toMutableList()
                    list.addAll(it)
                    this.listComment = list.toList()
                    listMyCommentAdapter.notifyData(this.listComment)
                    isLoadMore = it.count() >= items
                }
            } else if (it.status == Status.ERROR) {
                it.message?.let {
                    showDialog(it)
                }
            }
        })

        viewModel.deleteCommentResult.observe(this, Observer {
            loading(it)
            if (it.status == Status.SUCCESS) {
                val list = this.listComment.toMutableList()
                list.removeAt(this.positionCommentDeleted)
                this.listComment = list.toList()
                listMyCommentAdapter.notifyData(this.listComment)
            } else if (it.status == Status.ERROR) {
                it.message?.let {
                    showDialog(it)
                }
            }
        })
    }

    private fun loadDataMyComment() {
        val data = hashMapOf(
                "token" to token,
                "limit" to items,
                "offset" to page
        )

        viewModel.getListMyComment(data)
    }

    override fun initEvent() {
        rvListMyComment.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (isLoadMore) {
                    if (dy > 0) {
                        val visibleItemCount = 1
                        val totalItemCount = llManager.itemCount
                        val pastVisibleItems = llManager.findLastCompletelyVisibleItemPosition()

                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            isLoadMore = true
                            page++
                            loadDataMyComment()
                        }
                    }
                }
            }
        })
    }

    override fun onEvent(eventAction: Int, control: View?, data: Any?) {
        when(eventAction) {
            Constant.ACTION_CLICK_ON_BUTTON_DELETE_COMMENT -> {
                showConfirmDialog(
                        StringUtil.getString(R.string.TEXT_CONFIRM_DELETE_COMMENT),
                        "Cancel", DialogInterface.OnClickListener { dialogInterface, _ -> dialogInterface.dismiss() },
                        "OK", DialogInterface.OnClickListener { dialogInterface, _ -> run {
                    dialogInterface.dismiss()
                    val position = data as Int
                    this.positionCommentDeleted = position
                    val commentModel = listComment.get(position)
                    val data = hashMapOf(
                            "token" to token,
                            "commentId" to commentModel.commentId
                    )

                    viewModel.deleteComment(data)
                }})
            }

            Constant.ACTION_CLICK_ON_ITEM_MY_COMMENT -> {
                val commentId = data as Long
                navigationController.showReply(commentId)
            }

            Constant.ACTION_CLICK_ON_COMIC -> {
                val comicId = data as Long
                navigationController.showComicDetail(comicId.toString())
            }

            else -> super.onEvent(eventAction, control, data)
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