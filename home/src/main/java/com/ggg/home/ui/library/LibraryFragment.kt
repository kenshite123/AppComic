package com.ggg.home.ui.library

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.ggg.common.GGGAppInterface
import com.ggg.home.R
import com.ggg.home.data.model.ComicDownloadedModel
import com.ggg.home.data.model.ComicModel
import com.ggg.home.data.model.ComicWithCategoryModel
import com.ggg.home.data.model.HistoryModel
import com.ggg.home.data.model.response.LoginResponse
import com.ggg.home.ui.adapter.PagerLibraryAdapter
import com.ggg.home.ui.main.HomeBaseFragment
import com.ggg.home.utils.Constant
import kotlinx.android.synthetic.main.fragment_library.*
import timber.log.Timber

class LibraryFragment : HomeBaseFragment() {
    private lateinit var viewModel: LibraryViewModel
    lateinit var pagerLibraryAdapter: PagerLibraryAdapter
    private var isFirstLoad = true
    private var listHistoryModel: List<HistoryModel> = listOf()
    private var listComicFollow: List<ComicWithCategoryModel> = listOf()
    private var listComicDownloadedModel: List<ComicModel> = listOf()
    private var currentPagePosition = 0
    private var items = 60
    private var page = 0
    private var pageHistory = 0
    private var pageDownloaded = 0
    private var isLoadAllDataHistory = false
    private var isLoadMoreHistory = false
    private var isLoadAllDataDownloaded = false
    private var isLoadMoreDownloaded = false

    companion object {
        val TAG = "LibraryFragment"
        @JvmStatic
        fun create() = LibraryFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_library, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.d("onActivityCreated")
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LibraryViewModel::class.java)
        hideActionBar()
        showBottomNavView()

        initViews()
        initEvent()
    }

    private fun initViews() {
        pagerLibraryAdapter = PagerLibraryAdapter(context!!, this)
        viewPager.adapter = pagerLibraryAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun initObserver() {
        viewModel.getListHistoryResult.observe(this, Observer {
            if (currentPagePosition == 0) {
                loading(it)
            }
            it.data?.let {
                if (isLoadMoreHistory) {
                    val list = this.listHistoryModel.toMutableList()
                    list.addAll(it)
                    this.listHistoryModel = list.toList()

                    isLoadMoreHistory = false
                    isLoadAllDataHistory = it.count() >= items
                } else {
                    this.listHistoryModel = it
                }

                if (currentPagePosition == 0) {
                    pagerLibraryAdapter.notifyData(this.listHistoryModel)
                }
            }
        })

        viewModel.getListComicFollowResult.observe(this, Observer {
            if (currentPagePosition == 1) {
                loading(it)
            }
            it.data?.let {
                this.listComicFollow = it
                if (currentPagePosition == 1) {
                    pagerLibraryAdapter.notifyData(listComicFollow, true)
                }
            }
        })

        viewModel.getListComicDownloadedResult.observe(this, Observer {
            if (currentPagePosition == 2) {
                loading(it)
            }

            it.data?.let {
                if (isLoadMoreDownloaded) {
                    val list = this.listComicDownloadedModel.toMutableList()
                    list.addAll(it)
                    this.listComicDownloadedModel = list.toList()

                    isLoadMoreDownloaded = false
                    isLoadAllDataDownloaded = it.count() >= items
                } else {
                    this.listComicDownloadedModel = it
                }

                if (currentPagePosition == 2) {
                    pagerLibraryAdapter.notifyDataListComicDownloaded(this.listComicDownloadedModel)
                }
            }
        })
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
                    0 -> {
                        pagerLibraryAdapter.notifyData(listHistoryModel)
                    }

                    1 -> {
                        pagerLibraryAdapter.notifyData(listComicFollow, true)
                    }

                    else -> {
                        pagerLibraryAdapter.notifyDataListComicDownloaded(listComicDownloadedModel)
                    }
                }
            }
        })
    }

    private fun loadDataHistory() {
        val data = hashMapOf(
                "limit" to items,
                "offset" to pageHistory
        )
        viewModel.getListHistory(data)
    }

    private fun loadDataListComicFollow() {
        var loginResponse: LoginResponse? = null
        if (GGGAppInterface.gggApp.checkIsLogin()) {
            loginResponse = GGGAppInterface.gggApp.loginResponse as LoginResponse
        }
        val token = loginResponse?.tokenType + loginResponse?.accessToken
        val data = hashMapOf(
                "token" to token,
                "listComicId" to GGGAppInterface.gggApp.listFavoriteId,
                "limit" to 900,
                "offset" to 0
        )
        viewModel.getListComicFollow(data)
    }

    private fun loadDataListComicDownloaded() {
        val data = hashMapOf(
                "limit" to items,
                "offset" to pageDownloaded,
                "listComicId" to GGGAppInterface.gggApp.listDownloadedId
        )
        viewModel.getListComicDownloaded(data)
    }

    override fun onEvent(eventAction: Int, control: View?, data: Any?) {
        when(eventAction) {
            Constant.ACTION_CLICK_ON_COMIC_HISTORY -> {
                val historyModel = data as HistoryModel
                navigationController.showComicDetail(historyModel.comicModel!!.id.toString())
            }

            Constant.ACTION_CLICK_ON_COMIC_WITH_CATEGORY_MODEL -> {
                val comicWithCategoryModel = data as ComicWithCategoryModel
                navigationController.showComicDetail(comicWithCategoryModel)
            }

            Constant.ACTION_CLICK_ON_COMIC_DOWNLOADED -> {
                val comicId = data as Long
                navigationController.showComicDetail(comicId = comicId.toString())
            }

            Constant.ACTION_LOAD_MORE_LIST_COMIC_HISTORY -> {
                if (!isLoadAllDataHistory && !isLoadMoreHistory) {
                    isLoadMoreHistory = true
                    pageHistory++
                    loadDataHistory()
                }
            }

            Constant.ACTION_LOAD_MORE_LIST_COMIC_DOWNLOADED -> {
                if (!isLoadAllDataDownloaded && !isLoadMoreDownloaded) {
                    isLoadMoreDownloaded = true
                    pageDownloaded++
                    loadDataListComicDownloaded()
                }
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

        loadDataHistory()
        loadDataListComicFollow()
        loadDataListComicDownloaded()
    }
}