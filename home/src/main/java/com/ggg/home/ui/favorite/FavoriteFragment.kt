package com.ggg.home.ui.favorite

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
import com.ggg.home.data.model.ComicWithCategoryModel
import com.ggg.home.ui.adapter.ListComicAdapter
import com.ggg.home.ui.main.HomeBaseFragment
import com.ggg.home.utils.Constant
import kotlinx.android.synthetic.main.fragment_favorite.*
import timber.log.Timber

class FavoriteFragment : HomeBaseFragment() {
    private lateinit var viewModel: FavoriteViewModel
    var isFirstLoad = true

    lateinit var listComicAdapter: ListComicAdapter
    var listComic: List<ComicWithCategoryModel> = arrayListOf()
    var isLoadMore = false
    var items = 10
    var offset = 0
    lateinit var gridLayoutManager : GridLayoutManager
    var isFirstLoadDataApi = true
    var isLoadAllData = false

    companion object {
        val TAG = "FavoriteFragment"
        @JvmStatic
        fun create() : FavoriteFragment {
            val fragment = FavoriteFragment()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.d("onActivityCreated")
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(FavoriteViewModel::class.java)
        showActionBar()
        hideBottomNavView()
        setTitleActionBar(R.string.TEXT_FAVORITE)

        initViews()
        initEvent()
        loadData()
    }

    private fun initViews() {
        gridLayoutManager = GridLayoutManager(context!!, 3)
        listComicAdapter = ListComicAdapter(context!!, this, listComic)
        rvListComic.setHasFixedSize(false)
        rvListComic.layoutManager = gridLayoutManager
        rvListComic.adapter = listComicAdapter
    }

    override fun initObserver() {
        viewModel.getListFavoriteComicResult.observe(this, Observer {
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
        })

    }

    override fun initEvent() {
        rvListComic.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    if (!isLoadAllData && !isLoadMore) {
                        val visibleItemCount = 3
                        val totalItemCount = gridLayoutManager.itemCount
                        val pastVisiblesItems = gridLayoutManager.findLastCompletelyVisibleItemPosition()

                        Timber.d("visibleItemCount: ${visibleItemCount} " +
                                "- totalItemCount: ${totalItemCount} " +
                                "- pastVisiblesItems: ${pastVisiblesItems}")

                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            isLoadMore = true
                            offset++
                            loadData()
                        }
                    }
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
    }

    private fun loadData() {
        val data = hashMapOf(
                "limit" to items,
                "offset" to offset
        )

        viewModel.getListFavoriteComic(data)
    }

    override fun onEvent(eventAction: Int, control: View?, data: Any?) {
        when (eventAction) {
            Constant.ACTION_CLICK_ON_COMIC -> {
                val comicWithCategoryModel = data as ComicWithCategoryModel
                navigationController.showComicDetail(comicWithCategoryModel)
            }

            else -> {

            }
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