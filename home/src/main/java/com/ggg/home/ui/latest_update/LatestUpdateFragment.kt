package com.ggg.home.ui.latest_update

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ggg.common.vo.Status
import com.ggg.home.R
import com.ggg.home.data.model.ComicWithCategoryModel
import com.ggg.home.ui.adapter.ListComicAdapter
import com.ggg.home.ui.main.HomeBaseFragment
import com.ggg.home.utils.Constant
import kotlinx.android.synthetic.main.fragment_latest_update.*
import timber.log.Timber

class LatestUpdateFragment : HomeBaseFragment() {
    private lateinit var viewModel: LatestUpdateViewModel
    var isFirstLoad = true
    lateinit var listComicAdapter: ListComicAdapter
    lateinit var gridLayoutManager : GridLayoutManager
    var listComic: List<ComicWithCategoryModel> = listOf()
    var items: Int = 30
    var page: Int = 0
    var isLoadMore = true
    var isFirstLoadDataApi = true
    var isLoadAllData = false

    companion object {
        val TAG = "LatestUpdateFragment"
        @JvmStatic
        fun create() = LatestUpdateFragment()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_latest_update, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.d("onActivityCreated")
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LatestUpdateViewModel::class.java)
        isLoadMore = true
        isFirstLoadDataApi = true
        isLoadAllData = false

        showActionBar()
        hideBottomNavView()
        setTitleActionBar(R.string.TEXT_COMIC_LATEST_UPDATE)

        initViews()
        initEvent()
        loadData()
    }

    private fun initViews() {
        listComic = listOf()
        gridLayoutManager = GridLayoutManager(context!!, 3)
        listComicAdapter = ListComicAdapter(context!!, this, this.listComic)
        rvListComic.setHasFixedSize(false)
        rvListComic.layoutManager = gridLayoutManager
        rvListComic.adapter = listComicAdapter
    }

    override fun initObserver() {
        viewModel.getListLatestUpdateResult.observe(this, androidx.lifecycle.Observer {
            loading(it)
            if (it.status == Status.SUCCESS || it.status == Status.SUCCESS_DB || it.status == Status.ERROR) {
                if (it.status == Status.SUCCESS_DB) {
                    if (!isLoadAllData) {
                        if (it.data.isNullOrEmpty()) {
                            if (isFirstLoadDataApi) {
                                showLoading()
                            }
                        } else {
                            isFirstLoadDataApi = false
                            it.data?.let {
                                if (isLoadMore) {
                                    isLoadMore = false
                                    val list = this.listComic.toMutableList()
                                    list.addAll(it)

                                    this.listComic = list.toList()

                                    listComicAdapter.notifyData(this.listComic)
                                    isLoadAllData = it.size < items
                                } else {
                                    this.listComic = it
                                    listComicAdapter.notifyData(this.listComic)
                                }
                            }
                        }
                    }
                }

                it.data?.let {
                    if (isFirstLoadDataApi) {
                        this.listComic = it
                        listComicAdapter.notifyData(this.listComic)
                    }
                    isLoadAllData = it.size < items
                }
            }
//            loading(it)
//            if (it.status == Status.SUCCESS || it.status == Status.SUCCESS_DB || it.status == Status.ERROR) {
//                if (it.status == Status.SUCCESS_DB && it.data.isNullOrEmpty()) {
//                    showLoading()
//                }
//
//                it.data?.let {
//                    this.listComicLatestUpdate = it
//                    listComicAdapter.notifyData(this.listComicLatestUpdate)
//                }
//            }
        })
    }

    override fun initEvent() {
        rvListComic.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!isLoadAllData && !isLoadMore) {
                    if (dy > 0) {
                        val visibleItemCount = 3
                        val totalItemCount = gridLayoutManager.itemCount
                        val pastVisibleItems = gridLayoutManager.findLastCompletelyVisibleItemPosition()

                        Timber.d("visibleItemCount: ${visibleItemCount} " +
                                "- totalItemCount: ${totalItemCount} " +
                                "- pastVisibleItems: ${pastVisibleItems}")

                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            isLoadMore = true
                            page++
                            loadData()
                        }
                    }
                }
            }
        })
    }

    private fun loadData() {
        val dataLatestUpdate = hashMapOf(
                "limit" to items,
                "offset" to page
        )
        viewModel.getListLatestUpdate(dataLatestUpdate)
    }

    override fun onEvent(eventAction: Int, control: View?, data: Any?) {
        when (eventAction) {
            Constant.ACTION_CLICK_ON_COMIC -> {
                val comicWithCategoryModel = data as ComicWithCategoryModel
                navigationController.showComicDetail(comicWithCategoryModel)
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