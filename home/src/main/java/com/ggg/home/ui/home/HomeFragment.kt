package com.ggg.home.ui.home

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.ggg.common.GGGAppInterface
import com.ggg.common.vo.Status
import com.ggg.home.R
import com.ggg.home.data.model.ComicModel
import com.ggg.home.data.model.ComicWithCategoryModel
import com.ggg.home.ui.adapter.ListComicAdapter
import com.ggg.home.ui.adapter.PagerSlideAdapter
import com.ggg.home.ui.main.HomeBaseFragment
import com.ggg.home.ui.main.MainActivity
import com.ggg.home.utils.Constant
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.adView
import org.jetbrains.anko.bundleOf
import timber.log.Timber
import java.util.*

class HomeFragment : HomeBaseFragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var pagerSlideAdapter: PagerSlideAdapter
    private var listBanners: List<ComicWithCategoryModel> = arrayListOf()
    lateinit var listComicAdapter: ListComicAdapter
    var listComicLatestUpdate: List<ComicModel> = arrayListOf()
    private val pagerSnapHelper = PagerSnapHelper()

    lateinit var timer: Timer
    lateinit var timerTask: TimerTask
    var currentPage = 0
    var isLoadBannerAlready = false
    var isFirstLoad = true

    companion object {
        val TAG = "HomeFragment"
        private val DELAY_MS: Long = 500
        private val PERIOD_MS: Long = 3000
        @JvmStatic
        fun create() : HomeFragment {
            val fragment = HomeFragment()
            val bundle = bundleOf(
                    "isShowComicDetail" to false
            )
            fragment.arguments = bundle
            return fragment
        }

        @JvmStatic
        fun create(comicId: String) : HomeFragment {
            val fragment = HomeFragment()
            val bundle = bundleOf(
                    "isShowComicDetail" to true,
                    "comicId" to comicId
            )
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.d("onActivityCreated")
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)
        hideActionBar()
        showBottomNavView()

        val isShowComicDetail = arguments!!["isShowComicDetail"] as Boolean
        if (isShowComicDetail) {
            if (GGGAppInterface.gggApp.isFromNotification) {
                val comicId = arguments!!["comicId"].toString()
                navigationController.showComicDetail(comicId)
                GGGAppInterface.gggApp.isFromNotification = false
            } else {
                initViews()
                initEvent()
                loadData()
            }
        } else {
            initViews()
            initEvent()
            loadData()
        }
    }

    private fun initViews() {
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        adView.adListener = object : AdListener() {
            override fun onAdImpression() {
                super.onAdImpression()
            }

            override fun onAdClicked() {
                super.onAdClicked()
            }

            override fun onAdFailedToLoad(p0: LoadAdError?) {
                super.onAdFailedToLoad(p0)
            }

            override fun onAdClosed() {
                super.onAdClosed()
            }

            override fun onAdOpened() {
                super.onAdOpened()
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                adView.visibility = View.VISIBLE
            }
        }

        pagerSlideAdapter = PagerSlideAdapter(context!!, this, listBanners)
        rvSlide.setHasFixedSize(false)
        val layoutManager = LinearLayoutManager(context!!, RecyclerView.HORIZONTAL, false)
        layoutManager.stackFromEnd = true
        rvSlide.layoutManager = layoutManager
        rvSlide.adapter = pagerSlideAdapter
        pagerSnapHelper.attachToRecyclerView(rvSlide)

        listComicAdapter = ListComicAdapter(context!!, this,  listComicLatestUpdate, true)
        rvListComic.setHasFixedSize(true)
        rvListComic.layoutManager = GridLayoutManager(context!!, 3)
        rvListComic.adapter = listComicAdapter

        swipeRefreshLayout.isRefreshing = false
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark)

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = true
            val dataLatestUpdate = hashMapOf(
                    "limit" to 21,
                    "offset" to 0
            )
            viewModel.getListLatestUpdate(dataLatestUpdate)
        }
    }

    private fun initTimerToSlide() {
        val handler = Handler()
        val runnable = Runnable {
            if (currentPage == this.listBanners.count()) {
                currentPage = 0
            }

            if (isVisible) {
                rvSlide.scrollToPosition(currentPage++)
            }
        }
        timer = Timer()
        timerTask = object : TimerTask() {
            override fun run() {
                if (isVisible) {
                    if (isLoadBannerAlready) {
                        handler.post(runnable)
                    }
                }
            }
        }
        timer.schedule(timerTask, DELAY_MS, PERIOD_MS)
    }

    private fun loadData() {
        viewModel.getBanners()

        val dataLatestUpdate = hashMapOf(
                "limit" to 21,
                "offset" to 0
        )
        viewModel.getListLatestUpdate(dataLatestUpdate)
        viewModel.getListComicNotDownloaded()
    }

    override fun onPause() {
        super.onPause()
        Timber.d("onPause")
        timer.cancel()
        timerTask.cancel()
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume")
        if (isFirstLoad) {
            initObserver()
            isFirstLoad = false
        }
        initTimerToSlide()
    }

    override fun initObserver() {
        viewModel.getBannersResult.observe(this, androidx.lifecycle.Observer {
            if (it.status == Status.SUCCESS  || it.status == Status.SUCCESS_DB || it.status == Status.ERROR) {
                it.data?.let {
                    this.listBanners = it
//                    this.listBanners = it.distinctBy { it.comicModel?.id }
                    isLoadBannerAlready = true
                    pagerSlideAdapter.notifyData(this.listBanners)
                    indicator.attachToRecyclerView(rvSlide, pagerSnapHelper)
                }
            }
        })

        viewModel.getListLatestUpdateResult.observe(this, androidx.lifecycle.Observer {
            loading(it)
            if (it.status == Status.SUCCESS) {
                swipeRefreshLayout.isRefreshing = false
                it.data?.let {
                    this.listComicLatestUpdate = it
                    listComicAdapter.notifyDataSearch(this.listComicLatestUpdate)
                }
            } else if (it.status == Status.ERROR) {
                swipeRefreshLayout.isRefreshing = false
                it.message?.let {
                    showDialog(it)
                }
            }
        })

        viewModel.getListComicNotDownloadedResult.observe(this, androidx.lifecycle.Observer {
            if (it.status == Status.SUCCESS) {
                it.data?.let {
                    val listImageDownload = mutableListOf<HashMap<String, Any>>()
                    it.forEach {
                        val data = hashMapOf<String, Any>(
                                "comicId" to it.comicId,
                                "chapterId" to it.chapterId,
                                "imageUrl" to it.srcImg
                        )
                        listImageDownload.add(data)
                    }
                    if (!listImageDownload.isNullOrEmpty()) {
                        val list = it.groupBy { it.chapterId }
                        list.entries.forEach {
                            GGGAppInterface.gggApp.addNewComicDownloadToHashMap(it.key, it.value.count(), 0)
                        }
                        (activity as MainActivity).processDownloadImage(listImageDownload = listImageDownload)
                        viewModel.updateListNotDownloadToDownloading()
                    }
                }
            }
        })
    }

    override fun initEvent() {
        ivRank.setOnClickListener {
            navigationController.showRank()
        }

        ivLatestUpdate.setOnClickListener {
            showScreenById(R.id.navCategory)
        }

        ivFavorite.setOnClickListener {
            navigationController.showFavorite()
        }

        ivSearch.setOnClickListener {
            showScreenById(R.id.navSearch)
        }
    }

    override fun onEvent(eventAction: Int, control: View?, data: Any?) {
        when (eventAction) {
            Constant.ACTION_CLICK_ON_SLIDE -> {
                val comicWithCategoryModel = data as ComicWithCategoryModel
//                showDialog(comicWithCategoryModel.comicModel!!.title)
                navigationController.showComicDetail(comicWithCategoryModel)
            }

            Constant.ACTION_CLICK_ON_COMIC -> {
                val comicId = data as Long
                navigationController.showComicDetail(comicId.toString())
            }

            else -> {

            }
        }
    }
}
