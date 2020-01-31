package com.ggg.home.ui.adapter

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.PagerAdapter
import com.ggg.common.utils.OnEventControlListener
import com.ggg.common.utils.StringUtil
import com.ggg.home.R
import com.ggg.home.data.model.CategoryModel
import com.ggg.home.data.model.ComicModel
import com.ggg.home.data.model.ComicWithCategoryModel
import com.ggg.home.data.view.CategoryFilterItemView
import com.ggg.home.data.view.StatusTypeFilterItemView
import com.ggg.home.ui.custom.CategoryFilterView
import com.ggg.home.ui.custom.StatusTypeFilterView
import com.ggg.home.utils.Constant
import java.lang.ref.WeakReference

class PagerCategoryAndLatestUpdateAdapter : PagerAdapter, OnEventControlListener {

    lateinit var weakContext: WeakReference<Context>
    lateinit var listener: OnEventControlListener
    var listTitle: ArrayList<String> = arrayListOf(StringUtil.getString(R.string.TEXT_LATEST_UPDATE), StringUtil.getString(R.string.TEXT_ALL))

    var pageLatestUpdate = 0
    var isLoadMoreLatestUpdate = true
    var listComicLatestUpdate: List<ComicModel> = listOf()
    var isLoadAllDataLatestUpdate = false

    var pageCategory = 0
    var isLoadMoreCategory = true
    var listComicFilter: List<ComicWithCategoryModel> = listOf()
    var listLatestUpdateFilter: List<ComicModel> = listOf()
    var listCategories: List<CategoryModel> = listOf()
    var isLoadAllDataCategory = false
    var currentPositionStatusSelected = 0
    var currentPositionTypeSelected = 0
    var listCategoryIdSelected = listOf(-1L)

    var listStatusFilterItemView = listOf<StatusTypeFilterItemView>()
    var listTypeFilterItemView = listOf<StatusTypeFilterItemView>()
    var listCategoryFilterItemView = listOf<CategoryFilterItemView>()
    private var isType = false
    var pastVisibleItemsLatestUpdate = 0
    var pastVisibleItemsCategory = 0

    private lateinit var categoryFilterView: CategoryFilterView
    private lateinit var statusTypeFilterView: StatusTypeFilterView
    private lateinit var ctlCategory: ConstraintLayout
    private lateinit var tvCategories: TextView
    private lateinit var ctlStatus: ConstraintLayout
    private lateinit var tvStatus: TextView
    private lateinit var ctlType: ConstraintLayout
    private lateinit var tvType: TextView

    constructor(context: Context, listener: OnEventControlListener) {
        this.weakContext = WeakReference(context)
        this.listener = listener
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return listTitle[position]
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return listTitle.count()
    }

    fun notifyDataLatestUpdate(listComicLatestUpdate: List<ComicModel>, isLoadAllDataLatestUpdate: Boolean, pastVisibleItemsLatestUpdate: Int) {
        this.listComicLatestUpdate = listComicLatestUpdate
        if (isLoadMoreLatestUpdate) {
            isLoadMoreLatestUpdate = false
        }
        this.isLoadAllDataLatestUpdate = isLoadAllDataLatestUpdate
        this.pastVisibleItemsLatestUpdate = pastVisibleItemsLatestUpdate
        notifyDataSetChanged()
    }

    fun notifyDataListComicFilter(listCategories: List<CategoryModel>,
                                  listComicFilter: List<ComicWithCategoryModel>,
                                  isLoadAllDataCategory: Boolean, pastVisibleItemsCategory: Int,
                                  listCategoryIdSelected: List<Long>, statusSelected: String, typeSelected: String) {
        this.listCategories = listCategories
        this.listCategoryIdSelected = listCategoryIdSelected
        currentPositionStatusSelected = when (statusSelected) {
            Constant.FILTER_COMIC_STATUS_ALL -> 0
            Constant.FILTER_COMIC_STATUS_UPDATING -> 1
            else -> 2
        }

        currentPositionTypeSelected = when (typeSelected) {
            Constant.FILTER_COMIC_TYPE_POPULAR -> 0
            Constant.FILTER_COMIC_TYPE_NEW -> 1
            else -> 2
        }

        val list = mutableListOf(
                CategoryFilterItemView(CategoryModel(), isSelected = listCategoryIdSelected.indexOf(-1L) >= 0, isAll = true)
        )

        for (i in 0 until listCategories.count()) {
            val categoryModel = listCategories[i]
            list.add(CategoryFilterItemView(categoryModel, isSelected = listCategoryIdSelected.indexOf(categoryModel.id) >= 0, isAll = false))
        }
        this.listCategoryFilterItemView = list.toList()

        this.listComicFilter = listComicFilter
        if (isLoadMoreCategory) {
            isLoadMoreCategory = false
        }
        this.pastVisibleItemsCategory = pastVisibleItemsCategory
        this.isLoadAllDataCategory = isLoadAllDataCategory
        notifyDataSetChanged()
    }

    fun notifyDataListLatestUpdateFilter(listCategories: List<CategoryModel>,
                                         listLatestUpdateFilter: List<ComicModel>,
                                         isLoadAllDataCategory: Boolean, pastVisibleItemsCategory: Int,
                                         listCategoryIdSelected: List<Long>, statusSelected: String, typeSelected: String) {
        this.listCategories = listCategories
        this.listCategoryIdSelected = listCategoryIdSelected

        currentPositionStatusSelected = when (statusSelected) {
            Constant.FILTER_COMIC_STATUS_ALL -> 0
            Constant.FILTER_COMIC_STATUS_UPDATING -> 1
            else -> 2
        }

        currentPositionTypeSelected = when (typeSelected) {
            Constant.FILTER_COMIC_TYPE_POPULAR -> 0
            Constant.FILTER_COMIC_TYPE_NEW -> 1
            else -> 2
        }
        val list = mutableListOf(
                CategoryFilterItemView(CategoryModel(), isSelected = this.listCategoryIdSelected.indexOf(-1L) >= 0, isAll = true)
        )

        for (i in 0 until listCategories.count()) {
            val categoryModel = listCategories[i]
            list.add(CategoryFilterItemView(categoryModel, isSelected = this.listCategoryIdSelected.indexOf(categoryModel.id) >= 0, isAll = false))
        }
        this.listCategoryFilterItemView = list.toList()

        this.listLatestUpdateFilter = listLatestUpdateFilter
        if (isLoadMoreCategory) {
            isLoadMoreCategory = false
        }
        this.pastVisibleItemsCategory = pastVisibleItemsCategory
        this.isLoadAllDataCategory = isLoadAllDataCategory
        notifyDataSetChanged()
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View
        if (position == 0) {
            view = LayoutInflater.from(weakContext.get()!!).inflate(R.layout.item_tab_latest_update, container, false)
            processLatestUpdate(view)
        } else {
            view = LayoutInflater.from(weakContext.get()!!).inflate(R.layout.item_tab_category, container, false)
            processCategory(view)
        }

        container.addView(view)
        return view
    }

    private fun processLatestUpdate(view: View) {
        val gridLayoutManager = GridLayoutManager(weakContext.get(), 3)
        var listComicAdapter = ListComicAdapter(weakContext.get()!!, listener, listComicLatestUpdate, true)

        val swipeRefreshLayout: SwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        val rvListComic: RecyclerView = view.findViewById(R.id.rvListComic)
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
            listener.onEvent(Constant.ACTION_PULL_TO_REFRESH_LIST_COMIC_LATEST_UPDATE, null, null)
        }

        rvListComic.scrollToPosition(pastVisibleItemsLatestUpdate)

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
                            val hm = hashMapOf(
                                    "pastVisibleItemsLatestUpdate" to pastVisibleItems,
                                    "pageLatestUpdate" to pageLatestUpdate
                            )
                            listener.onEvent(Constant.ACTION_LOAD_LIST_COMIC_LATEST_UPDATE, null, hm)
                        }
                    }
                }
            }
        })
    }

    private fun processCategory(view: View) {
        val gridLayoutManager = GridLayoutManager(weakContext.get(), 3)
        categoryFilterView = view.findViewById(R.id.categoryFilterView)
        statusTypeFilterView = view.findViewById(R.id.statusTypeFilterView)
        ctlCategory = view.findViewById(R.id.ctlCategory)
        tvCategories = view.findViewById(R.id.tvCategories)
        ctlStatus = view.findViewById(R.id.ctlStatus)
        tvStatus = view.findViewById(R.id.tvStatus)
        ctlType = view.findViewById(R.id.ctlType)
        tvType = view.findViewById(R.id.tvType)
        val rvListComic: RecyclerView = view.findViewById(R.id.rvListComic)

        listStatusFilterItemView = listOf(
                StatusTypeFilterItemView(Constant.FILTER_COMIC_STATUS_ALL, isSelected = currentPositionStatusSelected == 0, isType = false),
                StatusTypeFilterItemView(Constant.FILTER_COMIC_STATUS_UPDATING, isSelected = currentPositionStatusSelected == 1, isType = false),
                StatusTypeFilterItemView(Constant.FILTER_COMIC_STATUS_UPDATED, isSelected = currentPositionStatusSelected == 2, isType = false)
        )

        listTypeFilterItemView = listOf(
                StatusTypeFilterItemView(Constant.FILTER_COMIC_TYPE_POPULAR, isSelected = currentPositionTypeSelected == 0, isType = true),
                StatusTypeFilterItemView(Constant.FILTER_COMIC_TYPE_NEW, isSelected = currentPositionTypeSelected == 1, isType = true),
                StatusTypeFilterItemView(Constant.FILTER_COMIC_TYPE_UPDATED, isSelected = currentPositionTypeSelected == 2, isType = true)
        )

        val listComicAdapter = if (listTypeFilterItemView[currentPositionTypeSelected].statusType == Constant.FILTER_COMIC_TYPE_UPDATED) {
            ListComicAdapter(weakContext.get()!!, listener, listLatestUpdateFilter, true)
        } else {
            ListComicAdapter(weakContext.get()!!, listener, listComicFilter)
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

        rvListComic.scrollToPosition(pastVisibleItemsCategory)

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
                            listener.onEvent(Constant.ACTION_LOAD_LIST_COMIC_FILTER, null, hm)
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

    override fun onEvent(eventAction: Int, control: View?, data: Any?) {
        when (eventAction) {
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

            else -> {}
        }
    }

    fun loadListComicFilter() {
        val dataLoadApi = hashMapOf(
                "isLoadMore" to false,
                "statusSelected" to listStatusFilterItemView[currentPositionStatusSelected].statusType,
                "typeSelected" to listTypeFilterItemView[currentPositionTypeSelected].statusType,
                "listCategoryIdSelected" to listCategoryIdSelected
        )
        pageCategory = 0
        listener.onEvent(Constant.ACTION_LOAD_LIST_COMIC_BY_FILTER, null, dataLoadApi)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }
}