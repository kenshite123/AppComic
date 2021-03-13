package com.ggg.home.ui.category_and_latest_update

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ggg.common.vo.Status
import com.ggg.home.R
import com.ggg.home.data.model.ComicModel
import com.ggg.home.ui.adapter.ListComicAdapter
import com.ggg.home.ui.main.HomeBaseFragment
import com.ggg.home.utils.Constant
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import kotlinx.android.synthetic.main.item_tab_latest_update.*
import timber.log.Timber

class LatestUpdateFragment : HomeBaseFragment() {
    private lateinit var viewModel: CategoryAndLatestUpdateViewModel
    var isFirstLoad = true

    private var pageLatestUpdate = 0
    private var isPullToRefreshLatestUpdate = false
    var listComicLatestUpdate: MutableList<ComicModel> = mutableListOf()
    var isLoadAllDataLatestUpdate = false
    private var isLoadMoreLatestUpdate = false
    private lateinit var listComicAdapter : ListComicAdapter
    private lateinit var gridLayoutManager : GridLayoutManager

    companion object {
        const val TAG = "LatestUpdateFragment"
        @JvmStatic
        fun create() : LatestUpdateFragment {
            val fragment = LatestUpdateFragment()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.item_tab_latest_update, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.d("onActivityCreated")
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CategoryAndLatestUpdateViewModel::class.java)

        initViews()
        initEvent()

        loadDataLatestUpdate(false)
    }

    override fun onResume() {
        super.onResume()
        if (isFirstLoad) {
            initObserver()
            isFirstLoad = false
        }
    }

    private fun initViews() {
        gridLayoutManager = GridLayoutManager(context, 3)
        listComicAdapter = ListComicAdapter(context!!, this, listComicLatestUpdate, true)

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

        rvListComic.setHasFixedSize(false)
        rvListComic.layoutManager = gridLayoutManager
        rvListComic.adapter = listComicAdapter

        swipeRefreshLayout.isRefreshing = false
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark)

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = true
            pageLatestUpdate = 0
            loadDataLatestUpdate(true)
        }
    }

    override fun initObserver() {
        viewModel.getListLatestUpdateResult.observe(this, Observer {
            loading(it)

            if (it.status == Status.SUCCESS) {
                it.data?.let {
                    if (isPullToRefreshLatestUpdate) {
                        this.listComicLatestUpdate = mutableListOf()
                        isPullToRefreshLatestUpdate = false
                    }
                    this.listComicLatestUpdate.addAll(it)
                    isLoadAllDataLatestUpdate = it.count() < 30
                    listComicAdapter.notifyDataSearch(this.listComicLatestUpdate)
                }

                isLoadMoreLatestUpdate = false
                swipeRefreshLayout.isRefreshing = false
            } else if (it.status == Status.ERROR) {
                swipeRefreshLayout.isRefreshing = false
                isLoadMoreLatestUpdate = false
                it.message?.let {
                    showDialog(it)
                }
            }
        })
    }

    override fun initEvent() {
        rvListComic.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!isLoadAllDataLatestUpdate && !isLoadMoreLatestUpdate) {
                    if (dy > 0) {
                        val visibleItemCount = 3
                        val totalItemCount = gridLayoutManager.itemCount
                        val pastVisibleItems = gridLayoutManager.findLastCompletelyVisibleItemPosition()

                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            isLoadMoreLatestUpdate = true
                            pageLatestUpdate++
                            loadDataLatestUpdate(false)
                        }
                    }
                }
            }
        })
    }

    private fun loadDataLatestUpdate(isPullToRefresh: Boolean) {
        if (isPullToRefresh) {
            pageLatestUpdate = 0
        }
        this.isPullToRefreshLatestUpdate = isPullToRefresh
        val dataLatestUpdate = hashMapOf(
                "limit" to 30,
                "offset" to pageLatestUpdate
        )
        viewModel.getListLatestUpdate(dataLatestUpdate)
    }

    override fun onEvent(eventAction: Int, control: View?, data: Any?) {
        when (eventAction) {
            Constant.ACTION_CLICK_ON_COMIC -> {
                val comicId = data as Long
                navigationController.showComicDetail(comicId.toString())
            }

            else -> super.onEvent(eventAction, control, data)
        }
    }
}