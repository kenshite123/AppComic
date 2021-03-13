package com.ggg.home.ui.category_and_latest_update

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.ggg.common.ui.BaseActivity
import com.ggg.home.R
import com.ggg.home.data.model.CategoryModel
import com.ggg.home.data.model.ComicModel
import com.ggg.home.data.model.ComicWithCategoryModel
import com.ggg.home.ui.adapter.PagerAdapter
import com.ggg.home.ui.adapter.PagerCategoryAndLatestUpdateAdapter
import com.ggg.home.ui.main.HomeBaseFragment
import com.ggg.home.utils.Constant
import kotlinx.android.synthetic.main.fragment_category_and_latest_update.*
import timber.log.Timber

class CategoryAndLatestUpdateFragment: HomeBaseFragment() {
    private lateinit var viewModel: CategoryAndLatestUpdateViewModel
    var isFirstLoad = true

    private var pageLatestUpdate = 0
    private var pageCategory = 0
    private var currentPagePosition = 0

    private var isPullToRefreshLatestUpdate = false
    private var listComicLatestUpdate = listOf<ComicModel>()
    lateinit var pagerCategoryAndLatestUpdateAdapter: PagerCategoryAndLatestUpdateAdapter
    lateinit var pagerAdapter: PagerAdapter

    var listCategoryIdSelected = listOf(-1L)
    var statusSelected = Constant.FILTER_COMIC_STATUS_ALL
    var typeSelected = Constant.FILTER_COMIC_TYPE_POPULAR
    var listCategories: List<CategoryModel> = listOf()
    private var listComicFilter = listOf<ComicWithCategoryModel>()
    private var listComicFilterOnline = listOf<ComicModel>()
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
    }

    private fun initViews() {
//        val adRequest = AdRequest.Builder().build()
        val listFragment = mutableListOf<Fragment>(
                LatestUpdateFragment.create(),
                CategoryFragment.create()
        )
        pagerAdapter = PagerAdapter(activity as BaseActivity, listFragment)
        viewPager.adapter = pagerAdapter
//        pagerCategoryAndLatestUpdateAdapter = PagerCategoryAndLatestUpdateAdapter(context!!, this, adRequest)
//        viewPager.adapter = pagerCategoryAndLatestUpdateAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun initObserver() {

    }

    override fun initEvent() {
//        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
//            override fun onPageScrollStateChanged(state: Int) {
//            }
//
//            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//            }
//
//            override fun onPageSelected(position: Int) {
//                currentPagePosition = position
//                when (position) {
//                    0 -> {
//                        currentPagePosition = 0
//                        pagerCategoryAndLatestUpdateAdapter.notifyDataLatestUpdate(listComicLatestUpdate,
//                                isLoadAllDataLatestUpdate, pastVisibleItemsLatestUpdate)
//                    }
//
//                    else -> {
//                        currentPagePosition = 1
//                        if (typeSelected == Constant.FILTER_COMIC_TYPE_UPDATED) {
//                            pagerCategoryAndLatestUpdateAdapter.notifyDataListLatestUpdateFilter(listCategories, listLatestUpdateFilter,
//                                    isLoadAllDataCategory, pastVisibleItemsCategory, listCategoryIdSelected, statusSelected, typeSelected)
//                        } else {
//                            if (Utils.isAvailableNetwork(activity!!)) {
//                                pagerCategoryAndLatestUpdateAdapter.notifyDataListComicFilter(listCategories, listComicFilterOnline, true,
//                                        isLoadAllDataCategory, pastVisibleItemsCategory, listCategoryIdSelected, statusSelected, typeSelected)
//                            } else {
//                                pagerCategoryAndLatestUpdateAdapter.notifyDataListComicFilter(listCategories, listComicFilter,
//                                        isLoadAllDataCategory, pastVisibleItemsCategory, listCategoryIdSelected, statusSelected, typeSelected)
//                            }
//                        }
//                    }
//                }
//            }
//        })
    }

    override fun onResume() {
        super.onResume()
        if (isFirstLoad) {
            initObserver()
            isFirstLoad = false
        }
    }

    override fun onEvent(eventAction: Int, control: View?, data: Any?) {
//        when (eventAction) {
//            Constant.ACTION_PULL_TO_REFRESH_LIST_COMIC_LATEST_UPDATE -> {
//                loadDataLatestUpdate(true)
//            }
//
//            Constant.ACTION_LOAD_LIST_COMIC_LATEST_UPDATE -> {
//                val hm = data as HashMap<String, Any>
//                this.pageLatestUpdate = hm["pageLatestUpdate"] as Int
//                this.pastVisibleItemsLatestUpdate = hm["pastVisibleItemsLatestUpdate"] as Int
//                loadDataLatestUpdate(false)
//            }
//
//            Constant.ACTION_CLICK_ON_COMIC -> {
//                val comicId = data as Long
//                navigationController.showComicDetail(comicId.toString())
//            }
//
//            Constant.ACTION_CLICK_ON_COMIC_WITH_CATEGORY_MODEL -> {
//                val comicWithCategoryModel = data as ComicWithCategoryModel
//                navigationController.showComicDetail(comicWithCategoryModel)
//            }
//
//            Constant.ACTION_LOAD_LIST_COMIC_BY_FILTER -> {
//                val hm = data as HashMap<String, Any>
//                pageCategory = 0
//                this.isLoadMoreCategory = hm["isLoadMore"] as Boolean
//                this.statusSelected = hm["statusSelected"] as String
//                this.typeSelected = hm["typeSelected"] as String
//                this.listCategoryIdSelected = hm["listCategoryIdSelected"] as List<Long>
//                loadDataCategory()
//            }
//
//            Constant.ACTION_LOAD_LIST_COMIC_FILTER -> {
//                val hm = data as HashMap<String, Any>
//                this.isLoadMoreCategory = hm["isLoadMore"] as Boolean
//                this.pageCategory = hm["pageCategory"] as Int
//                this.pastVisibleItemsCategory = hm["pastVisibleItems"] as Int
//                loadDataCategory()
//            }
//
//            else -> super.onEvent(eventAction, control, data)
//        }
    }

//    private fun loadDataLatestUpdate(isPullToRefresh: Boolean) {
//        if (isPullToRefresh) {
//            pageLatestUpdate = 0
//        }
//        this.isPullToRefreshLatestUpdate = isPullToRefresh
//        val dataLatestUpdate = hashMapOf(
//                "limit" to 30,
//                "offset" to pageLatestUpdate
//        )
//        viewModel.getListLatestUpdate(dataLatestUpdate)
//    }
//
//    private fun loadDataCategory() {
//        val dataFilter = hashMapOf(
//                "listCategoryId" to listCategoryIdSelected,
//                "status" to statusSelected,
//                "type" to typeSelected,
//                "limit" to 30,
//                "offset" to pageCategory
//        )
//        if (typeSelected == Constant.FILTER_COMIC_TYPE_UPDATED) {
//            viewModel.getListLatestUpdateWithFilter(dataFilter)
//        } else {
//            if (Utils.isAvailableNetwork(activity!!)) {
//                viewModel.getAllListComicByFilterOnline(dataFilter)
//            } else {
//                viewModel.getAllListComicByFilter(dataFilter)
//            }
//        }
//    }
}