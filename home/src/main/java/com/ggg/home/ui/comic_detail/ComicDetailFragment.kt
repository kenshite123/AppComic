package com.ggg.home.ui.comic_detail

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
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ggg.common.GGGAppInterface
import com.ggg.common.utils.CommonUtils
import com.ggg.common.utils.StringUtil
import com.ggg.common.vo.Status
import com.ggg.home.R
import com.ggg.home.data.model.*
import com.ggg.home.data.model.response.LoginResponse
import com.ggg.home.ui.adapter.ListCategoryComicDetailAdapter
import com.ggg.home.ui.adapter.PagerComicDetailAdapter
import com.ggg.home.ui.main.HomeBaseFragment
import com.ggg.home.utils.Constant
import com.ggg.common.utils.Utils
import kotlinx.android.synthetic.main.fragment_comic_detail.*
import org.jetbrains.anko.bundleOf
import timber.log.Timber

class ComicDetailFragment : HomeBaseFragment() {
    private lateinit var viewModel: ComicDetailViewModel
    var isFirstLoad = true
    lateinit var comicWithCategoryModel: ComicWithCategoryModel
    lateinit var listCategoryComicDetailAdapter: ListCategoryComicDetailAdapter
    lateinit var pagerComicDetailAdapter: PagerComicDetailAdapter
    lateinit var listChapters: List<ChapterHadRead>
    var listComments: List<CommentModel> = listOf()
    var currentPagePosition = 0

    var itemsComment: Long = 50
    var pageComment: Long = 0

    var isLoadComicInfo = false
    var comicId: Long = 0
    var isFollow = false
    var isLoadMoreComment: Boolean = true
    var isLoadLatest = true

    companion object {
        val TAG = "ComicDetailFragment"
        @JvmStatic
        fun create(comicWithCategoryModel: ComicWithCategoryModel) : ComicDetailFragment {
            val bundle = bundleOf(
                    "comicWithCategoryModel" to comicWithCategoryModel
            )
            val comicDetailFragment = ComicDetailFragment()
            comicDetailFragment.arguments = bundle
            return comicDetailFragment
        }

        @JvmStatic
        fun create(comicId: String) : ComicDetailFragment {
            val bundle = bundleOf(
                    "comicId" to comicId
            )
            val comicDetailFragment = ComicDetailFragment()
            comicDetailFragment.arguments = bundle
            return comicDetailFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comic_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.d("onActivityCreated")
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ComicDetailViewModel::class.java)
        showActionBar()
        hideBottomNavView()

        if (null == arguments?.get("comicWithCategoryModel")) {
            isLoadComicInfo = true
            val comicIdString = arguments?.get("comicId").toString()
            comicId = comicIdString.toLong()
            loadComicInfoByComicId(comicId)
        } else {
            isLoadComicInfo = false
            comicWithCategoryModel = arguments?.get("comicWithCategoryModel") as ComicWithCategoryModel
            initViews()
            initEvent()
            loadListChapter()
            loadListComment()
        }
    }

    private fun initViews() {
        listComments = listOf()
        currentPagePosition = 0
        val comic = comicWithCategoryModel.comicModel
        comic?.title?.let { setTitleActionBar(it) }

        Glide.with(context!!)
                .load(comic?.imageUrl)
                .placeholder(GGGAppInterface.gggApp.circularProgressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivComic)

        if (!comic?.authorsString.isNullOrEmpty()) {
            tvAuthor.text = comic?.authorsString!!
        } else {
            tvAuthor.text = StringUtil.getString(R.string.TEXT_STATUS_UNCOMPLETED)
        }

        if (comic?.status == Constant.STATUS_COMPLETED) {
            tvStatus.text = StringUtil.getString(R.string.TEXT_STATUS_COMPLETED)
        } else {
            tvStatus.text = StringUtil.getString(R.string.TEXT_STATUS_UNCOMPLETED)
        }

        tvViews.text = comic?.viewed?.let { Utils.formatNumber(it) }

        val listFollow = GGGAppInterface.gggApp.listFavoriteId
        if (!listFollow.isEmpty()) {
            if (listFollow.contains(comicWithCategoryModel.comicModel!!.id.toString())) {
                isFollow = true
                btnFollow.setText(R.string.TEXT_UNFOLLOW)
                btnFollow.setBackgroundColor(resources.getColor(R.color.colorPrimary))
            } else {
                isFollow = false
                btnFollow.setText(R.string.TEXT_FOLLOW)
                btnFollow.setBackgroundColor(Color.parseColor("#ffab02"))
            }
        } else {
            isFollow = false
            btnFollow.setText(R.string.TEXT_FOLLOW)
            btnFollow.setBackgroundColor(Color.parseColor("#ffab02"))
        }

        listCategoryComicDetailAdapter = ListCategoryComicDetailAdapter(context!!, this, comicWithCategoryModel.categories, false)
        rvListCategory.setHasFixedSize(true)
//        rvListCategory.layoutManager = GridLayoutManager(context!!, 3)
        rvListCategory.layoutManager = LinearLayoutManager(context!!, RecyclerView.HORIZONTAL, false)
        rvListCategory.adapter = listCategoryComicDetailAdapter

        pagerComicDetailAdapter = PagerComicDetailAdapter(context!!, this)
        viewPager.adapter = pagerComicDetailAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun initEvent() {
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                currentPagePosition = position
                when (position) {
                    0 -> { // list chapters
                        pagerComicDetailAdapter.notifyData(listChapters, isLoadLatest, !isLoadLatest)
                    }

                    1 -> { // description
                        pagerComicDetailAdapter.notifyData(comicWithCategoryModel)
                    }

                    2 -> { // list comments
                        pagerComicDetailAdapter.notifyData(listComments, isLoadMoreComment)
                    }
                }
            }
        })

        btnReadNow.setOnClickListener {
            insertCCHadRead(0)
            navigationController.showViewComic(comicWithCategoryModel, listChapters, 0, isLoadLatest)
        }

        btnFollow.setOnClickListener {
            if (isFollow) {
                unFavoriteComic()
            } else {
                favoriteComic()
            }
        }
    }

    private fun loadListChapter() {
        viewModel.getListChapters(comicWithCategoryModel.comicModel!!.id)
    }

    private fun loadListComment() {
        val dataRequestListComment = hashMapOf(
                "comicId" to comicWithCategoryModel.comicModel!!.id,
                "limit" to itemsComment,
                "offset" to pageComment
        )

        if (CommonUtils.isInternetAvailable()) {
            viewModel.getListComments(dataRequestListComment)
        }
    }

    private fun initObserve() {
        viewModel.getListChaptersResult.observe(this, Observer {
            if (currentPagePosition == 0) {
                loading(it)
            }
            if (it.status == Status.SUCCESS || it.status == Status.SUCCESS_DB || it.status == Status.ERROR) {
                if (it.status == Status.SUCCESS_DB && currentPagePosition == 0 && it.data.isNullOrEmpty()) {
                    showLoading()
                }

                it.data?.let {
                    this.listChapters = it
                    if (currentPagePosition == 0) {
                        isLoadLatest = true
                        pagerComicDetailAdapter.notifyData(this.listChapters, isLoadLatest, !isLoadLatest)
                    }
                }
            }
        })

        viewModel.getListCommentsResult.observe(this, Observer {
            if (currentPagePosition == 2) {
                loading(it)
            }

            if (it.status == Status.SUCCESS) {
                it.data?.let {
                    val list = this.listComments.toMutableList()
                    list.addAll(it)
                    this.listComments = list.toList()
                    isLoadMoreComment = it.count() >= itemsComment
                    if (currentPagePosition == 2) {
                        pagerComicDetailAdapter.notifyData(this.listComments, isLoadMoreComment)
                    }

                }
            } else if (it.status == Status.ERROR) {
                it.message?.let {
                    if (currentPagePosition == 2) {
                        showDialog(it)
                    } else {
                        showMsg(it)
                    }
                }
            }
        })

        viewModel.getGetComicInfoResult.observe(this, Observer {
            loading(it)
            if (it.status == Status.SUCCESS || it.status == Status.SUCCESS_DB || it.status == Status.ERROR) {
                it.data?.let {
                    this.comicWithCategoryModel = it
                    initViews()
                    initEvent()
                    loadListChapter()
                    loadListComment()
                }
            }
        })

        viewModel.favoriteComicResult.observe(this, Observer {
            loading(it)
            if (it.status == Status.SUCCESS) {
                isFollow = true
                btnFollow.setText(R.string.TEXT_UNFOLLOW)
                btnFollow.setBackgroundColor(resources.getColor(R.color.colorPrimary))
            } else {
                it.message?.let {
                    showDialog(it)
                }
            }
        })

        viewModel.unFavoriteComicResult.observe(this, Observer {
            loading(it)
            if (it.status == Status.SUCCESS) {
                isFollow = false
                btnFollow.setText(R.string.TEXT_FOLLOW)
                btnFollow.setBackgroundColor(Color.parseColor("#ffab02"))
            } else {
                it.message?.let {
                    showDialog(it)
                }
            }
        })
    }

    override fun onEvent(eventAction: Int, control: View?, data: Any?) {
        when (eventAction) {
            Constant.ACTION_CLICK_ON_CHAPTER -> {
                val positionChapter = data as Int
                insertCCHadRead(positionChapter)
                navigationController.showViewComic(comicWithCategoryModel, listChapters, positionChapter, isLoadLatest)
            }

            Constant.ACTION_CLICK_ON_ITEM_CATEGORY_OF_COMIC_DETAIL -> {
                val categoryOfComicModel = data as CategoryOfComicModel
                navigationController.showCategoryDetail(categoryOfComicModel)
            }

            Constant.ACTION_CLICK_ON_LIST_REPLIES_COMMENT -> {
                val commentModel = data as CommentModel
                navigationController.showReply(commentModel)
            }

            Constant.ACTION_CLICK_ON_BUTTON_COMMENT_IN_COMIC_DETAIL -> {
                navigationController.showComment(comicWithCategoryModel.comicModel!!.id)
            }

            Constant.ACTION_LOAD_MORE_COMMENT_OF_COMIC_DETAIL -> {
                isLoadMoreComment = true
                pageComment++
                loadListComment()
            }

            Constant.ACTION_CLICK_ON_BUTTON_LOAD_LATEST_CHAPTER -> {
                if (!isLoadLatest) {
                    isLoadLatest = true
                    this.listChapters = this.listChapters.reversed()
                    pagerComicDetailAdapter.notifyData(this.listChapters, isLoadLatest, !isLoadLatest)
                }
            }

            Constant.ACTION_CLICK_ON_BUTTON_LOAD_OLDEST_CHAPTER -> {
                if (isLoadLatest) {
                    isLoadLatest = false
                    this.listChapters = this.listChapters.reversed()
                    pagerComicDetailAdapter.notifyData(this.listChapters, isLoadLatest, !isLoadLatest)
                }
            }

            else -> super.onEvent(eventAction, control, data)
        }
    }

    private fun insertCCHadRead(positionChapter: Int) {
        comicWithCategoryModel.comicModel?.let {
            val ccHadReadModel = CCHadReadModel()
            ccHadReadModel.comicId = it.id
            ccHadReadModel.chapterId = listChapters[positionChapter].chapterModel!!.chapterId
            ccHadReadModel.lastModified = System.currentTimeMillis()
            viewModel.insertCCHadRead(ccHadReadModel)
        }
    }

    private fun loadComicInfoByComicId(comicId: Long) {
        viewModel.getComicInfo(comicId)
    }

    private fun favoriteComic() {
        if (GGGAppInterface.gggApp.checkIsLogin()) {
            val loginResponse = GGGAppInterface.gggApp.loginResponse as LoginResponse
            val token = "${loginResponse.tokenType}${loginResponse.accessToken}"
            val data = hashMapOf(
                    "token" to token,
                    "comicId" to comicWithCategoryModel.comicModel!!.id
            )

            viewModel.favoriteComic(data)
        } else {
            showConfirmDialog(R.string.TEXT_ERROR_NO_LOGIN_TO_FOLLOW_COMIC,
                    R.string.TEXT_CANCEL, DialogInterface.OnClickListener { dialogInterface, _ -> dialogInterface.dismiss() },
                    R.string.TEXT_REGISTER, DialogInterface.OnClickListener { dialogInterface, _ -> run {
                dialogInterface.dismiss()
                navigationController.showRegister()
            }},
                    R.string.TEXT_LOGIN, DialogInterface.OnClickListener { dialogInterface, _ -> run {
                dialogInterface.dismiss()
                navigationController.showLogin()
            }})
        }

//        GGGAppInterface.gggApp.addComicToFavorite(comicWithCategoryModel.comicModel!!.id)
    }

    private fun unFavoriteComic() {
        if (GGGAppInterface.gggApp.checkIsLogin()) {
            val loginResponse = GGGAppInterface.gggApp.loginResponse as LoginResponse
            val token = "${loginResponse.tokenType}${loginResponse.accessToken}"
            val data = hashMapOf(
                    "token" to token,
                    "comicId" to comicWithCategoryModel.comicModel!!.id
            )

            viewModel.unFavoriteComic(data)
        } else {
            showConfirmDialog(R.string.TEXT_ERROR_NO_LOGIN_TO_UNFOLLOW_COMIC,
                    R.string.TEXT_CANCEL, DialogInterface.OnClickListener { dialogInterface, _ -> dialogInterface.dismiss() },
                    R.string.TEXT_REGISTER, DialogInterface.OnClickListener { dialogInterface, _ -> run {
                dialogInterface.dismiss()
                navigationController.showRegister()
            }},
                    R.string.TEXT_LOGIN, DialogInterface.OnClickListener { dialogInterface, _ -> run {
                dialogInterface.dismiss()
                navigationController.showLogin()
            }})
        }

//        GGGAppInterface.gggApp.removeComicToFavorite(comicWithCategoryModel.comicModel!!.id)
    }

    override fun onResume() {
        super.onResume()
        if (isFirstLoad) {
            initObserve()
            isFirstLoad = false
        }
    }
}