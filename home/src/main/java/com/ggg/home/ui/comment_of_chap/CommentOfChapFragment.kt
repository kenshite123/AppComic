package com.ggg.home.ui.comment_of_chap

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ggg.common.GGGAppInterface
import com.ggg.common.utils.CommonUtils
import com.ggg.common.utils.DateTimeUtil
import com.ggg.common.utils.StringUtil
import com.ggg.common.vo.Status
import com.ggg.home.R
import com.ggg.home.data.model.ChapterModel
import com.ggg.home.data.model.ComicWithCategoryModel
import com.ggg.home.data.model.CommentModel
import com.ggg.home.data.model.UserCommentModel
import com.ggg.home.data.model.post_param.WriteCommentBody
import com.ggg.home.data.model.response.LoginResponse
import com.ggg.home.ui.adapter.ListCommentAdapter
import com.ggg.home.ui.main.HomeBaseFragment
import com.ggg.home.utils.Constant
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_comment_of_chap.*
import org.jetbrains.anko.bundleOf
import timber.log.Timber
import java.util.concurrent.TimeUnit

class CommentOfChapFragment : HomeBaseFragment() {
    private lateinit var viewModel: CommentOfChapViewModel
    var isFirstLoad = true
    private var loginResponse: LoginResponse? = null
    lateinit var writeCommentBody: WriteCommentBody
    lateinit var comicWithCategoryModel: ComicWithCategoryModel
    lateinit var chapterModel: ChapterModel
    lateinit var listCommentAdapter: ListCommentAdapter
    lateinit var llManager: LinearLayoutManager
    var listComments: List<CommentModel> = listOf()
    var isLoadMore = true
    var items = 50L
    var page = 0L

    companion object {
        val TAG = "CommentOfChapFragment"
        @JvmStatic
        fun create(comicWithCategoryModel: ComicWithCategoryModel, chapterModel: ChapterModel): CommentOfChapFragment {
            val fragment = CommentOfChapFragment()
            val bundle = bundleOf(
                    "comicWithCategoryModel" to comicWithCategoryModel,
                    "chapterModel" to chapterModel
            )
            fragment.arguments = bundle
            return fragment
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comment_of_chap, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.d("onActivityCreated")
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CommentOfChapViewModel::class.java)
        showActionBar()
        hideBottomNavView()

        comicWithCategoryModel = arguments!!["comicWithCategoryModel"] as ComicWithCategoryModel
        chapterModel = arguments!!["chapterModel"] as ChapterModel
        if (GGGAppInterface.gggApp.loginResponse != null) {
            loginResponse = GGGAppInterface.gggApp.loginResponse as LoginResponse
        }

        setTitleActionBar(StringUtil.getString(R.string.TEXT_COMMENT_OF_CHAP, chapterModel.chapterName))

        initViews()
        initEvent()
        loadData()
    }

    private fun initViews() {
        listComments = listOf()
        llManager = LinearLayoutManager(context!!, RecyclerView.VERTICAL, false)
        listCommentAdapter = ListCommentAdapter(context!!, this, listComments, true)
        rvListComments.setHasFixedSize(false)
        rvListComments.layoutManager = llManager
        rvListComments.adapter = listCommentAdapter
    }

    override fun initObserver() {
        val disposable = RxTextView.textChanges(edComment)
                .skip(1)
                .debounce(100, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.isEmpty()) {
                        ivSend.setColorFilter(Color.parseColor("#cdcdcd"))
                    } else {
                        ivSend.setColorFilter(Color.parseColor("#f2606b"))
                    }
                }, {
                    Timber.e(it)
                })
        messageEvent.add(disposable)

        viewModel.getListCommentOfChapResult.observe(this, Observer {
            loading(it)
            if (it.status == Status.SUCCESS) {
                it.data?.let {
                    val list = this.listComments.toMutableList()
                    list.addAll(it)
                    this.listComments = list.toList()
                    listCommentAdapter.notifyData(this.listComments)

                    isLoadMore = it.count() >= items
                }
            } else if (it.status == Status.ERROR) {
                it.message?.let {
                    showDialog(it)
                }
            }
        })

        viewModel.writeCommentResult.observe(this, Observer {
            loading(it)
            if (it.status == Status.SUCCESS) {
                it.data?.let {
                    val list = this.listComments.toMutableList()
                    val commentModel = CommentModel()
                    commentModel.commentId = it.commentId
                    commentModel.content = writeCommentBody.content
                    commentModel.createdAt = DateTimeUtil.getCurrentDateTime()
                    val userComment = UserCommentModel()
                    userComment.userId = loginResponse?.user?.id!!
                    userComment.nickname = loginResponse?.user?.fullName!!
                    userComment.imageUrl = loginResponse?.user?.imageUrl!!
                    commentModel.userComment = userComment
                    commentModel.replies = listOf()

                    list.add(0, commentModel)
                    this.listComments = list.toList()
                    edComment.text.clear()
                    listCommentAdapter.notifyData(this.listComments)
                }
            } else if (it.status == Status.ERROR) {
                it.message?.let {
                    showDialog(it)
                }
            }
        })
    }

    override fun initEvent() {
        rvListComments.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
                            loadData()
                        }
                    }
                }
            }
        })

        ivSend.setOnClickListener {
            hideSoftKeyboard()
            if (checkValidSendComment()) {
                val token = loginResponse!!.tokenType + loginResponse!!.accessToken
                writeCommentBody = WriteCommentBody()
                writeCommentBody.topicType = Constant.TOPIC_TYPE_COMMENT
                writeCommentBody.comicId = this.chapterModel.comicId
                writeCommentBody.chapterId = this.chapterModel.chapterId
                writeCommentBody.parentId = 0
                writeCommentBody.content = edComment.text.toString()

                val data = hashMapOf<String, Any>(
                        "token" to token,
                        "writeCommentBody" to writeCommentBody
                )

                viewModel.writeComment(data)
            }
        }
    }

    private fun loadData() {
        val data = hashMapOf(
                "comicId" to chapterModel.comicId,
                "chapterId" to chapterModel.chapterId,
                "limit" to items,
                "offset" to page
        )

        viewModel.getListCommentOfChap(data)
    }

    override fun onEvent(eventAction: Int, control: View?, data: Any?) {
        when (eventAction) {
            Constant.ACTION_CLICK_ON_LIST_REPLIES_COMMENT -> {
                val commentModel = data as CommentModel
                navigationController.showReply(commentModel)
            }

            else -> super.onEvent(eventAction, control, data)
        }
    }

    private fun checkValidSendComment() : Boolean {
        if (!CommonUtils.isInternetAvailable()) {
            showDialog(R.string.TEXT_ERROR_NO_CONNECTION)
            return false
        }

        if (edComment.text.toString().isEmpty()) {
            showMsg(R.string.TEXT_ERROR_NO_CONTENT_YET)
            return false
        }

        if (loginResponse == null) {
            showConfirmDialog(StringUtil.getString(R.string.TEXT_ERROR_NO_LOGIN_TO_COMMENT),
                    StringUtil.getString(R.string.TEXT_REGISTER), DialogInterface.OnClickListener { dialogInterface, _ ->
                dialogInterface.dismiss()
                navigationController.showRegister()
            }, "OK", DialogInterface.OnClickListener { dialogInterface, _ ->
                dialogInterface.dismiss()
            })
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