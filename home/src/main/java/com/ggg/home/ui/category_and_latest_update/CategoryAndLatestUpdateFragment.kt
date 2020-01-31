package com.ggg.home.ui.category_and_latest_update

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.ggg.common.ui.BaseActivity
import com.ggg.common.vo.Status
import com.ggg.home.R
import com.ggg.home.data.model.CategoryModel
import com.ggg.home.data.model.ComicModel
import com.ggg.home.data.model.ComicWithCategoryModel
import com.ggg.home.ui.adapter.PagerCategoryAndLatestUpdateAdapter
import com.ggg.home.ui.main.HomeBaseFragment
import com.ggg.home.utils.Constant
import com.ggg.common.utils.Utils
import kotlinx.android.synthetic.main.fragment_category_and_latest_update.*
import timber.log.Timber

class CategoryAndLatestUpdateFragment: HomeBaseFragment() {
    private lateinit var viewModel: CategoryAndLatestUpdateViewModel
    var isFirstLoad = true

    private var pageLatestUpdate = 0
    private var pageCategory = 0
    private var currentPagePosition = 0

    var isPullToRefreshLatestUpdate = false
    private var listComicLatestUpdate = listOf<ComicModel>()
    lateinit var pagerCategoryAndLatestUpdateAdapter: PagerCategoryAndLatestUpdateAdapter

    var listCategoryIdSelected = listOf(-1L)
    var statusSelected = Constant.FILTER_COMIC_STATUS_ALL
    var typeSelected = Constant.FILTER_COMIC_TYPE_POPULAR
    var listCategories: List<CategoryModel> = listOf()
    private var listComicFilter = listOf<ComicWithCategoryModel>()
    private var listLatestUpdateFilter = listOf<ComicModel>()

    var isLoadAllDataLatestUpdate = false
    var isLoadAllDataCategory = false
    var isLoadMoreCategory = false
    var pastVisibleItemsCategory = 0
    var pastVisibleItemsLatestUpdate = 0

    companion object {
        const val TAG = "CategoryAndLatestUpdateFragment"
        @JvmStatic
        fun create() : CategoryAndLatestUpdateFragment {
            val fragment = CategoryAndLatestUpdateFragment()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category_and_latest_update, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.d("onActivityCreated")
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CategoryAndLatestUpdateViewModel::class.java)
        hideActionBar()
        showBottomNavView()

        initViews()
        initEvent()

        currentPagePosition = 0
        viewModel.getAllListCategories()
        loadDataCategory()
        loadDataLatestUpdate(false)
    }

    private fun initViews() {
        pagerCategoryAndLatestUpdateAdapter = PagerCategoryAndLatestUpdateAdapter(context!!, this)
        viewPager.adapter = pagerCategoryAndLatestUpdateAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun initObserver() {
        viewModel.getAllListCategoriesResult.observe(this, Observer {
            if (it.status == Status.SUCCESS || it.status == Status.SUCCESS_DB || it.status == Status.ERROR) {
                it.data?.let {
                    if (it.isNotEmpty()) {
                        this.listCategories = it
                    }
                }
            }
        })

        viewModel.getListLatestUpdateResult.observe(this, Observer {
            if (currentPagePosition == 0) {
                loading(it)
            }

            if (it.status == Status.SUCCESS) {
                it.data?.let {
                    if (isPullToRefreshLatestUpdate) {
                        this.listComicLatestUpdate = listOf()
                        isPullToRefreshLatestUpdate = false
                    }
                    val list = this.listComicLatestUpdate.toMutableList()
                    list.addAll(it)
                    isLoadAllDataLatestUpdate = it.count() < 30
                    this.listComicLatestUpdate = list.toList()
                    if (currentPagePosition == 0) {
                        pagerCategoryAndLatestUpdateAdapter.notifyDataLatestUpdate(this.listComicLatestUpdate,
                                isLoadAllDataLatestUpdate, pastVisibleItemsLatestUpdate)
                    }
                }
            } else if (it.status == Status.ERROR) {
                it.message?.let {
                    showDialog(it)
                }
            }
        })

        viewModel.getListComicByFilterResult.observe(this, Observer {
            if (currentPagePosition == 1) {
                loadingWithConnection(it)
            }

            if (it.status == Status.SUCCESS || (it.status == Status.SUCCESS_DB && !Utils.isAvailableNetwork(activity as BaseActivity)) || it.status == Status.ERROR) {
                it.data?.let {
                    if (!isLoadMoreCategory) {
                        this.listComicFilter = listOf()
                    }
                    isLoadAllDataCategory = it.count() < 30
                    val list = this.listComicFilter.toMutableList()
                    list.addAll(it)
                    this.listComicFilter = list.toList()
                    if (currentPagePosition == 1) {
                        pagerCategoryAndLatestUpdateAdapter.notifyDataListComicFilter(this.listCategories, this.listComicFilter,
                                isLoadAllDataCategory, pastVisibleItemsCategory, listCategoryIdSelected, statusSelected, typeSelected)
                    }
                }
            }
        })

        viewModel.getListLatestUpdateWithFilterResult.observe(this, Observer {
            if (currentPagePosition == 1) {
                loading(it)
            }

            if (it.status == Status.SUCCESS) {
                it.data?.let {
                    if (!isLoadMoreCategory) {
                        this.listLatestUpdateFilter = listOf()
                    }
                    isLoadAllDataCategory = it.count() < 30
                    val list = this.listLatestUpdateFilter.toMutableList()
                    list.addAll(it)
                    this.listLatestUpdateFilter = list.toList()
                    if (currentPagePosition == 1) {
                        pagerCategoryAndLatestUpdateAdapter.notifyDataListLatestUpdateFilter(this.listCategories, this.listLatestUpdateFilter,
                                isLoadAllDataCategory, pastVisibleItemsCategory, listCategoryIdSelected, statusSelected, typeSelected)
                    }
                }
            } else if (it.status == Status.ERROR) {
                it.message?.let {
                    showDialog(it)
                    if (currentPagePosition == 1) {
                        pagerCategoryAndLatestUpdateAdapter.notifyDataListLatestUpdateFilter(this.listCategories, listOf(),
                                isLoadAllDataCategory, pastVisibleItemsCategory, listCategoryIdSelected, statusSelected, typeSelected)
                    }
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
                        currentPagePosition = 0
                        pagerCategoryAndLatestUpdateAdapter.notifyDataLatestUpdate(listComicLatestUpdate,
                                isLoadAllDataLatestUpdate, pastVisibleItemsLatestUpdate)
                    }

                    else -> {
                        currentPagePosition = 1
                        if (typeSelected == Constant.FILTER_COMIC_TYPE_UPDATED) {
                            pagerCategoryAndLatestUpdateAdapter.notifyDataListLatestUpdateFilter(listCategories, listLatestUpdateFilter,
                                    isLoadAllDataCategory, pastVisibleItemsCategory, listCategoryIdSelected, statusSelected, typeSelected)
                        } else {
                            pagerCategoryAndLatestUpdateAdapter.notifyDataListComicFilter(listCategories, listComicFilter,
                                    isLoadAllDataCategory, pastVisibleItemsCategory, listCategoryIdSelected, statusSelected, typeSelected)
                        }
                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (isFirstLoad) {
            initObserver()
            isFirstLoad = false
        }
    }

    override fun onEvent(eventAction: Int, control: View?, data: Any?) {
        when (eventAction) {
            Constant.ACTION_PULL_TO_REFRESH_LIST_COMIC_LATEST_UPDATE -> {
                loadDataLatestUpdate(true)
            }

            Constant.ACTION_LOAD_LIST_COMIC_LATEST_UPDATE -> {
                val hm = data as HashMap<String, Any>
                this.pageLatestUpdate = hm["pageLatestUpdate"] as Int
                this.pastVisibleItemsLatestUpdate = hm["pastVisibleItemsLatestUpdate"] as Int
                loadDataLatestUpdate(false)
            }

            Constant.ACTION_CLICK_ON_COMIC -> {
                val comicId = data as Long
                navigationController.showComicDetail(comicId.toString())
            }

            Constant.ACTION_CLICK_ON_COMIC_WITH_CATEGORY_MODEL -> {
                val comicWithCategoryModel = data as ComicWithCategoryModel
                navigationController.showComicDetail(comicWithCategoryModel)
            }

            Constant.ACTION_LOAD_LIST_COMIC_BY_FILTER -> {
                val hm = data as HashMap<String, Any>
                pageCategory = 0
                this.isLoadMoreCategory = hm["isLoadMore"] as Boolean
                this.statusSelected = hm["statusSelected"] as String
                this.typeSelected = hm["typeSelected"] as String
                this.listCategoryIdSelected = hm["listCategoryIdSelected"] as List<Long>
                loadDataCategory()
            }

            Constant.ACTION_LOAD_LIST_COMIC_FILTER -> {
                val hm = data as HashMap<String, Any>
                this.isLoadMoreCategory = hm["isLoadMore"] as Boolean
                this.pageCategory = hm["pageCategory"] as Int
                this.pastVisibleItemsCategory = hm["pastVisibleItems"] as Int
                loadDataCategory()
            }

            else -> super.onEvent(eventAction, control, data)
        }
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

    private fun loadDataCategory() {
        val dataFilter = hashMapOf(
                "listCategoryId" to listCategoryIdSelected,
                "status" to statusSelected,
                "type" to typeSelected,
                "limit" to 30,
                "offset" to pageCategory
        )
        if (typeSelected == Constant.FILTER_COMIC_TYPE_UPDATED) {
            viewModel.getListLatestUpdateWithFilter(dataFilter)
        } else {
            viewModel.getAllListComicByFilter(dataFilter)
        }
    }
}