package com.ggg.home.ui.category_and_latest_update

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ggg.common.ui.BaseActivity
import com.ggg.common.utils.Utils
import com.ggg.common.vo.Status
import com.ggg.home.R
import com.ggg.home.data.model.CategoryModel
import com.ggg.home.data.model.ComicModel
import com.ggg.home.data.model.ComicWithCategoryModel
import com.ggg.home.data.view.CategoryFilterItemView
import com.ggg.home.data.view.StatusTypeFilterItemView
import com.ggg.home.ui.adapter.ListComicAdapter
import com.ggg.home.ui.custom.CategoryFilterView
import com.ggg.home.ui.custom.StatusTypeFilterView
import com.ggg.home.ui.main.HomeBaseFragment
import com.ggg.home.utils.Constant
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import kotlinx.android.synthetic.main.item_tab_category.*
import timber.log.Timber


class CategoryFragment : HomeBaseFragment() {
    private lateinit var viewModel: CategoryAndLatestUpdateViewModel
    var isFirstLoad = true
    private lateinit var categoryFilterView: CategoryFilterView
    private lateinit var statusTypeFilterView: StatusTypeFilterView
    private var networkChange: MutableLiveData<Boolean> = MutableLiveData()

    private var listCategoryIdSelected = listOf(-1L)
    private var statusSelected = Constant.FILTER_COMIC_STATUS_ALL
    private var typeSelected = Constant.FILTER_COMIC_TYPE_POPULAR
    private var listCategories: MutableList<CategoryModel> = mutableListOf()
    private var listComicFilter = mutableListOf<ComicWithCategoryModel>()
    private var listComicFilterOnline = mutableListOf<ComicModel>()
    private var listLatestUpdateFilter = mutableListOf<ComicModel>()
    private var listStatusFilterItemView = mutableListOf<StatusTypeFilterItemView>()
    private var listTypeFilterItemView = mutableListOf<StatusTypeFilterItemView>()
    private var listCategoryFilterItemView = mutableListOf<CategoryFilterItemView>()

    private lateinit var listComicAdapter: ListComicAdapter
    private lateinit var gridLayoutManager: GridLayoutManager

    private var isLoadOnline = false
    private var pageCategory = 0
    private var isLoadAllDataCategory = false
    private var isLoadMoreCategory = false
    private var pastVisibleItemsCategory = 0
    private var pastVisibleItemsLatestUpdate = 0
    private var isType = false

    private var currentPositionStatusSelected = 0
    private var currentPositionTypeSelected = 0

    companion object {
        const val TAG = "CategoryFragment"
        @JvmStatic
        fun create() : CategoryFragment {
            val fragment = CategoryFragment()
            fragment.isFirstLoad = true
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.item_tab_category, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.d("onActivityCreated")
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CategoryAndLatestUpdateViewModel::class.java)

        initViews()
        initEvent()

        viewModel.getAllListCategories()
        loadDataCategory()
    }

    private fun initViews() {
        categoryFilterView = view!!.findViewById(R.id.categoryFilterView)
        statusTypeFilterView = view!!.findViewById(R.id.statusTypeFilterView)

        this.isLoadOnline = Utils.isAvailableNetwork(activity!!)
        val intentFilter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        context!!.registerReceiver(NetworkChangeReceiver(networkChange), intentFilter)

//        val adRequest = AdRequest.Builder().build()
//        adView.loadAd(adRequest)
//        adView.adListener = object : AdListener() {
//            override fun onAdImpression() {
//                super.onAdImpression()
//            }
//
//            override fun onAdClicked() {
//                super.onAdClicked()
//            }
//
//            override fun onAdFailedToLoad(p0: LoadAdError?) {
//                super.onAdFailedToLoad(p0)
//            }
//
//            override fun onAdClosed() {
//                super.onAdClosed()
//            }
//
//            override fun onAdOpened() {
//                super.onAdOpened()
//            }
//
//            override fun onAdLoaded() {
//                super.onAdLoaded()
//                adView.visibility = View.VISIBLE
//            }
//        }

        listStatusFilterItemView = mutableListOf(
                StatusTypeFilterItemView(Constant.FILTER_COMIC_STATUS_ALL, isSelected = currentPositionStatusSelected == 0, isType = false),
                StatusTypeFilterItemView(Constant.FILTER_COMIC_STATUS_UPDATING, isSelected = currentPositionStatusSelected == 1, isType = false),
                StatusTypeFilterItemView(Constant.FILTER_COMIC_STATUS_UPDATED, isSelected = currentPositionStatusSelected == 2, isType = false)
        )

        listTypeFilterItemView = mutableListOf(
                StatusTypeFilterItemView(Constant.FILTER_COMIC_TYPE_POPULAR, isSelected = currentPositionTypeSelected == 0, isType = true),
                StatusTypeFilterItemView(Constant.FILTER_COMIC_TYPE_NEW, isSelected = currentPositionTypeSelected == 1, isType = true),
                StatusTypeFilterItemView(Constant.FILTER_COMIC_TYPE_UPDATED, isSelected = currentPositionTypeSelected == 2, isType = true)
        )

        gridLayoutManager = GridLayoutManager(context, 3)
        listComicAdapter = if (listTypeFilterItemView[currentPositionTypeSelected].statusType == Constant.FILTER_COMIC_TYPE_UPDATED) {
            ListComicAdapter(context!!, this, listLatestUpdateFilter, true)
        } else {
            if (isLoadOnline) {
                ListComicAdapter(context!!, this, listComicFilterOnline, true)
            } else {
                ListComicAdapter(context!!, this, listComicFilter)
            }
        }

        rvListComic.setHasFixedSize(false)
        rvListComic.layoutManager = gridLayoutManager
        rvListComic.adapter = listComicAdapter

        if (listCategoryFilterItemView.isNotEmpty() && listCategoryFilterItemView[0].isSelected) {
            tvCategories.text = "All"
        } else {
            val mutableList = mutableListOf<String>()
            for (i in 0 until listCategoryFilterItemView.count()) {
                val categoryFilterItemView = listCategoryFilterItemView[i]
                if (categoryFilterItemView.isSelected) {
                    mutableList.add(categoryFilterItemView.categoryModel.name)
                }
            }
            tvCategories.text = TextUtils.join(",", mutableList)
        }
        tvType.text = listTypeFilterItemView[currentPositionTypeSelected].statusType
        if (listStatusFilterItemView[currentPositionStatusSelected].statusType == Constant.FILTER_COMIC_STATUS_UPDATED) {
            tvStatus.text = "Completed"
        } else {
            tvStatus.text = listStatusFilterItemView[currentPositionStatusSelected].statusType
        }
    }

    override fun initObserver() {
        viewModel.getAllListCategoriesResult.observe(this, Observer {
            if (it.status == Status.SUCCESS || it.status == Status.SUCCESS_DB || it.status == Status.ERROR) {
                it.data?.let {
                    if (it.isNotEmpty()) {
                        listCategoryFilterItemView = mutableListOf()
                        this.listCategories = it.toMutableList()
                        for (i in 0 until this.listCategories.count()) {
                            val categoryModel = listCategories[i]
                            listCategoryFilterItemView.add(CategoryFilterItemView(categoryModel, isSelected = listCategoryIdSelected.indexOf(categoryModel.id) >= 0, isAll = false))
                        }
                    }
                }
            }
        })

        viewModel.getListComicByFilterResult.observe(this, Observer {
            loadingWithConnection(it)

            if (it.status == Status.SUCCESS || (it.status == Status.SUCCESS_DB &&
                            !Utils.isAvailableNetwork(activity as BaseActivity)) || it.status == Status.ERROR) {
                it.data?.let {
                    if (!isLoadMoreCategory) {
                        this.listComicFilter = mutableListOf()
                    }
                    isLoadAllDataCategory = it.count() < 30
                    isLoadMoreCategory = false
                    this.listComicFilter.addAll(it)
                    listComicAdapter.notifyData(this.listComicFilter)
                }
            }
        })

        viewModel.getListComicByFilterOnlineResult.observe(this, Observer {
            loading(it)

            if (it.status == Status.SUCCESS || it.status == Status.ERROR) {
                it.data?.let {
                    if (!isLoadMoreCategory) {
                        this.listComicFilterOnline = mutableListOf()
                    }
                    isLoadAllDataCategory = it.count() < 30
                    isLoadMoreCategory = false
                    this.listComicFilterOnline.addAll(it)
                    listComicAdapter.notifyDataSearch(this.listComicFilterOnline)
                }
            }
        })

        viewModel.getListLatestUpdateWithFilterResult.observe(this, Observer {
            loading(it)

            if (it.status == Status.SUCCESS) {
                it.data?.let {
                    if (!isLoadMoreCategory) {
                        this.listLatestUpdateFilter = mutableListOf()
                    }
                    isLoadAllDataCategory = it.count() < 30
                    isLoadMoreCategory = false
                    this.listLatestUpdateFilter.addAll(it)
                    this.listComicAdapter.notifyDataSearch(listLatestUpdateFilter)
                }
            } else if (it.status == Status.ERROR) {
                isLoadMoreCategory = false
                it.message?.let {
                    showDialog(it)
                    this.listComicAdapter.notifyDataSearch(mutableListOf())
                }
            }
        })

        networkChange.observe(this, Observer {
            this.isLoadOnline = Utils.isAvailableNetwork(activity!!)
//            showMsg(if (this.isLoadOnline) "true" else "false")
            listComicAdapter = if (listTypeFilterItemView[currentPositionTypeSelected].statusType == Constant.FILTER_COMIC_TYPE_UPDATED) {
                ListComicAdapter(context!!, this, listLatestUpdateFilter, true)
            } else {
                if (isLoadOnline) {
                    ListComicAdapter(context!!, this, listComicFilterOnline, true)
                } else {
                    ListComicAdapter(context!!, this, listComicFilter)
                }
            }

            rvListComic.setHasFixedSize(false)
            rvListComic.layoutManager = gridLayoutManager
            rvListComic.adapter = listComicAdapter
        })
    }

    override fun initEvent() {
        rvListComic.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!isLoadAllDataCategory && !isLoadMoreCategory) {
                    if (dy > 0) {
                        val visibleItemCount = 3
                        val totalItemCount = gridLayoutManager.itemCount
                        val pastVisibleItems = gridLayoutManager.findLastCompletelyVisibleItemPosition()

                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            isLoadMoreCategory = true
                            pageCategory++
                            val hm = hashMapOf(
                                    "isLoadMore" to true,
                                    "pageCategory" to pageCategory,
                                    "pastVisibleItems" to pastVisibleItems
                            )
                            onEvent(Constant.ACTION_LOAD_LIST_COMIC_FILTER, null, hm)
                        }
                    }
                }
            }
        })

        ctlStatus.setOnClickListener {
            statusTypeFilterView.visibility = View.VISIBLE
            statusTypeFilterView.bringToFront()
            this.isType = false
            statusTypeFilterView.setData(this, listStatusFilterItemView, this.isType)
            statusTypeFilterView.reloadViews()
        }

        ctlType.setOnClickListener {
            statusTypeFilterView.visibility = View.VISIBLE
            statusTypeFilterView.bringToFront()
            this.isType = true
            statusTypeFilterView.setData(this, listTypeFilterItemView, this.isType)
            statusTypeFilterView.reloadViews()
        }

        ctlCategory.setOnClickListener {
            categoryFilterView.visibility = View.VISIBLE
            categoryFilterView.bringToFront()
            categoryFilterView.setData(this, listCategoryFilterItemView)
            categoryFilterView.reloadViews()
        }
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

            Constant.ACTION_CLOSE_STATUS_TYPE_FILTER_SELECT_VIEW -> {
                statusTypeFilterView.visibility = View.GONE
            }

            Constant.ACTION_CLOSE_CATEGORY_FILTER_SELECT_VIEW -> {
                categoryFilterView.visibility = View.GONE
                val listCategoryFilterView = data as List<CategoryFilterItemView>
                if (listCategoryFilterView.isNotEmpty() && listCategoryFilterView[0].isSelected) {
                    listCategoryIdSelected = listOf(-1L)
                    tvCategories.text = "All"
                } else {
                    val mutableList = mutableListOf<String>()
                    listCategoryIdSelected = listOf()
                    for (i in 0 until listCategoryFilterView.count()) {
                        val categoryFilterItemView = listCategoryFilterItemView[i]
                        if (categoryFilterItemView.isSelected) {
                            val list = listCategoryIdSelected.toMutableList()
                            list.add(categoryFilterItemView.categoryModel.id)
                            mutableList.add(categoryFilterItemView.categoryModel.name)
                            listCategoryIdSelected = list.toList()
                        }
                    }
                    tvCategories.text = TextUtils.join(", ", mutableList)
                }
                loadListComicFilter()
            }

            Constant.ACTION_CLICK_ON_ITEM_STATUS_TYPE_FILTER -> {
                statusTypeFilterView.visibility = View.GONE
                val position = data as Int
                if (this.isType) {
                    this.currentPositionTypeSelected = position
                    for (i in 0 until listTypeFilterItemView.count()) {
                        val statusTypeFilterItemView = listTypeFilterItemView[i]
                        statusTypeFilterItemView.isSelected = i == currentPositionTypeSelected
                    }
                    tvType.text = listTypeFilterItemView[currentPositionTypeSelected].statusType
                    loadListComicFilter()
                } else {
                    this.currentPositionStatusSelected = position
                    for (i in 0 until listStatusFilterItemView.count()) {
                        val statusTypeFilterItemView = listStatusFilterItemView[i]
                        statusTypeFilterItemView.isSelected = i == currentPositionStatusSelected
                    }
                    if (listStatusFilterItemView[currentPositionStatusSelected].statusType == Constant.FILTER_COMIC_STATUS_UPDATED) {
                        tvStatus.text = "Completed"
                    } else {
                        tvStatus.text = listStatusFilterItemView[currentPositionStatusSelected].statusType
                    }
                    loadListComicFilter()
                }
            }

            else -> super.onEvent(eventAction, control, data)
        }
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
            if (Utils.isAvailableNetwork(activity!!)) {
                viewModel.getAllListComicByFilterOnline(dataFilter)
            } else {
                viewModel.getAllListComicByFilter(dataFilter)
            }
        }
    }

    private fun loadListComicFilter() {
        val dataLoadApi = hashMapOf(
                "isLoadMore" to false,
                "statusSelected" to listStatusFilterItemView[currentPositionStatusSelected].statusType,
                "typeSelected" to listTypeFilterItemView[currentPositionTypeSelected].statusType,
                "listCategoryIdSelected" to listCategoryIdSelected
        )
        pageCategory = 0
        onEvent(Constant.ACTION_LOAD_LIST_COMIC_BY_FILTER, null, dataLoadApi)
    }

    class NetworkChangeReceiver : BroadcastReceiver {
        private var networkChange: MutableLiveData<Boolean> = MutableLiveData()
        constructor(networkChange: MutableLiveData<Boolean>) {
            this.networkChange = networkChange
        }
        override fun onReceive(context: Context?, intent: Intent) {
            networkChange.postValue(true)
        }
    }
}