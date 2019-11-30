package com.ggg.home.ui.reply

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ggg.common.GGGAppInterface
import com.ggg.home.R
import com.ggg.home.data.model.CommentModel
import com.ggg.home.ui.adapter.ListCommentAdapter
import com.ggg.home.ui.main.HomeBaseFragment
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_reply.*
import kotlinx.android.synthetic.main.item_comment.*
import org.jetbrains.anko.bundleOf
import timber.log.Timber
import java.util.concurrent.TimeUnit

class ReplyFragment : HomeBaseFragment() {
    private lateinit var viewModel: ReplyViewModel
    var isFirstLoad = true
    lateinit var commentModel: CommentModel
    lateinit var listCommentAdapter: ListCommentAdapter

    companion object {
        val TAG = "ReplyFragment"
        @JvmStatic
        fun create(commentModel: CommentModel): ReplyFragment {
            val fragment = ReplyFragment()
            val bundle = bundleOf(
                    "commentModel" to commentModel
            )
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reply, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.d("onActivityCreated")
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ReplyViewModel::class.java)
        showActionBar()
        hideBottomNavView()
        setTitleActionBar(R.string.TEXT_COMMENT_DETAILS)
        commentModel = arguments!!["commentModel"] as CommentModel

        initViews()
        initEvent()
    }

    private fun initViews() {
        tvReplies.visibility = View.GONE
        if (!commentModel.userComment.imageUrl.isEmpty()) {
            Glide.with(context)
                    .load(commentModel.userComment.imageUrl)
                    .placeholder(GGGAppInterface.gggApp.circularProgressDrawable)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(ivAvatar)
        } else {
            ivAvatar.setImageResource(R.drawable.i_avatar)
        }

        tvUsername.text = commentModel.userComment.nickname
        tvCreateAt.text = commentModel.createdAt
        tvContent.text = commentModel.content

        listCommentAdapter = ListCommentAdapter(context!!, this, commentModel.replies, false)
        rvListReplies.setHasFixedSize(false)
        rvListReplies.layoutManager = LinearLayoutManager(context!!, RecyclerView.VERTICAL, false)
        rvListReplies.adapter = listCommentAdapter
    }

    override fun initObserver() {
        val disposable = RxTextView.textChanges(edContent)
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
    }

    override fun initEvent() {
        ivSend.setOnClickListener {

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