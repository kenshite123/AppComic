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
import com.ggg.common.vo.Status
import com.ggg.home.R
import com.ggg.home.data.model.ComicModel
import com.ggg.home.data.model.ComicWithCategoryModel
import com.ggg.home.ui.adapter.ListComicAdapter
import com.ggg.home.ui.adapter.PagerSlideAdapter
import com.ggg.home.ui.main.HomeBaseFragment
import com.ggg.home.utils.Constant
import kotlinx.android.synthetic.main.fragment_home.*
import timber.log.Timber
import java.util.*

class HomeFragment : HomeBaseFragment() {

    private lateinit var viewModel: HomeViewModel
    lateinit var pagerSlideAdapter: PagerSlideAdapter
    var listBanners: List<ComicWithCategoryModel> = arrayListOf()
    lateinit var listComicAdapter: ListComicAdapter
    var listComicLatestUpdate: List<ComicWithCategoryModel> = arrayListOf()
    val pagerSnapHelper = PagerSnapHelper()

    lateinit var timer: Timer
    lateinit var timerTask: TimerTask
    var currentPage = 0
    var isLoadBannerAlready = false
    var isFirstLoad = false

    companion object {
        val TAG = "HomeFragment"
        private val DELAY_MS: Long = 500
        private val PERIOD_MS: Long = 3000
        @JvmStatic
        fun create() = HomeFragment()
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

        initViews()
        initEvent()
        loadData()
    }

    private fun initViews() {
        pagerSlideAdapter = PagerSlideAdapter(context!!, this, listBanners)
        rvSlide.setHasFixedSize(false)
        val layoutManager = LinearLayoutManager(context!!, RecyclerView.HORIZONTAL, false)
        layoutManager.stackFromEnd = true
        rvSlide.layoutManager = layoutManager
        rvSlide.adapter = pagerSlideAdapter
        pagerSnapHelper.attachToRecyclerView(rvSlide)

        listComicAdapter = ListComicAdapter(context!!, this,  listComicLatestUpdate)
        rvListComic.setHasFixedSize(true)
        rvListComic.layoutManager = GridLayoutManager(context!!, 3)
        rvListComic.adapter = listComicAdapter
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
        if (!isFirstLoad) {
            initObserver()
            isFirstLoad = true
        }
        initTimerToSlide()
    }

    override fun initObserver() {
        viewModel.getBannersResult.observe(this, androidx.lifecycle.Observer {
            if (it.status == Status.SUCCESS || it.status == Status.ERROR) {
                it.data?.let {
                    this.listBanners = it.distinctBy { it.comicModel?.id }
                    isLoadBannerAlready = true
                    pagerSlideAdapter.notifyData(this.listBanners)
                    indicator.attachToRecyclerView(rvSlide, pagerSnapHelper)
                }
            }
        })

        viewModel.getListLatestUpdateResult.observe(this, androidx.lifecycle.Observer {
            loading(it)
            if (it.status == Status.SUCCESS || it.status == Status.ERROR) {
                it.data?.let {
                    this.listComicLatestUpdate = it.distinctBy { it.comicModel?.id }
                    listComicAdapter.notifyData(this.listComicLatestUpdate)
                }
            }
        })
    }

    override fun initEvent() {
        ivRank.setOnClickListener {

        }

        ivLatestUpdate.setOnClickListener {

        }

        ivSearch.setOnClickListener {

        }
    }

    override fun onEvent(eventAction: Int, control: View?, data: Any?) {
        when (eventAction) {
            Constant.ACTION_CLICK_ON_SLIDE -> {
                val comicWithCategoryModel = data as ComicWithCategoryModel
                showDialog(comicWithCategoryModel.comicModel!!.title)
            }

            Constant.ACTION_CLICK_ON_COMIC -> {
                val comicWithCategoryModel = data as ComicWithCategoryModel
                showDialog(comicWithCategoryModel.comicModel!!.title)
            }

            else -> {

            }
        }
    }
}
