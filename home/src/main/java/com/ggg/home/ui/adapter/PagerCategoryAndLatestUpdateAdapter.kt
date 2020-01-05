package com.ggg.home.ui.adapter

import android.content.Context
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
    var listCategories: List<CategoryModel> = listOf()
    var isLoadAllDataCategory = false
    var currentPositionStatusSelected = 0
    var currentPositionTypeSelected = 0
    var currentPositionCategorySelected = listOf(0)

    var listStatusFilterItemView = listOf<StatusTypeFilterItemView>()
    var listTypeFilterItemView = listOf<StatusTypeFilterItemView>()
    var listCategoryFilterItemView = listOf<CategoryFilterItemView>()
    private var isType = false

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

    fun notifyDataLatestUpdate(listComicLatestUpdate: List<ComicModel>, isLoadAllDataLatestUpdate: Boolean) {
        this.listComicLatestUpdate = listComicLatestUpdate
        if (isLoadMoreLatestUpdate) {
            isLoadMoreLatestUpdate = false
        }
        this.isLoadAllDataLatestUpdate = isLoadAllDataLatestUpdate
        notifyDataSetChanged()
    }

    fun notifyDataListComicFilter(listCategories: List<CategoryModel>,
                                  listComicFilter: List<ComicWithCategoryModel>, isLoadAllDataCategory: Boolean) {
        this.listCategories = listCategories
        val list = mutableListOf(
                CategoryFilterItemView(CategoryModel(), isSelected = currentPositionCategorySelected.indexOf(0) >= 0, isAll = true)
        )

        for (i in 0 until listCategories.count()) {
            val categoryModel = listCategories[i]
            list.add(CategoryFilterItemView(categoryModel, isSelected = currentPositionCategorySelected.indexOf(i + 1) >= 0, isAll = false))
        }
        this.listCategoryFilterItemView = list.toList()

        this.listComicFilter = listComicFilter
        if (isLoadMoreCategory) {
            isLoadMoreCategory = false
        }
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
                            listener.onEvent(Constant.ACTION_LOAD_LIST_COMIC_LATEST_UPDATE, null, pageLatestUpdate)
                        }
                    }
                }
            }
        })
    }

    private fun processCategory(view: View) {
        val gridLayoutManager = GridLayoutManager(weakContext.get(), 3)
        val listComicAdapter = ListComicAdapter(weakContext.get()!!, listener, listComicFilter)

        statusTypeFilterView = view.findViewById(R.id.statusTypeFilterView)
        ctlCategory = view.findViewById(R.id.ctlCategory)
        tvCategories = view.findViewById(R.id.tvCategories)
        ctlStatus = view.findViewById(R.id.ctlStatus)
        tvStatus = view.findViewById(R.id.tvStatus)
        ctlType = view.findViewById(R.id.ctlType)
        tvType = view.findViewById(R.id.tvType)
        val rvListComic: RecyclerView = view.findViewById(R.id.rvListComic)

        rvListComic.setHasFixedSize(false)
        rvListComic.layoutManager = gridLayoutManager
        rvListComic.adapter = listComicAdapter

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

        tvType.text = listTypeFilterItemView[currentPositionTypeSelected].statusType
        tvStatus.text = listStatusFilterItemView[currentPositionStatusSelected].statusType

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
                            listener.onEvent(Constant.ACTION_LOAD_LIST_COMIC_FILTER, null, pageCategory)
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

        }
    }

    override fun onEvent(eventAction: Int, control: View?, data: Any?) {
        when (eventAction) {
            Constant.ACTION_CLOSE_STATUS_TYPE_FILTER_SELECT_VIEW -> {
                statusTypeFilterView.visibility = View.GONE
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
                } else {
                    this.currentPositionStatusSelected = position
                    for (i in 0 until listStatusFilterItemView.count()) {
                        val statusTypeFilterItemView = listStatusFilterItemView[i]
                        statusTypeFilterItemView.isSelected = i == currentPositionStatusSelected
                    }
                    tvStatus.text = listStatusFilterItemView[currentPositionStatusSelected].statusType
                    
                    val data = hashMapOf(
                            "isLoadMore" to false,
                            "statusSelected" to listStatusFilterItemView[currentPositionStatusSelected].statusType,
                            "typeSelected" to listTypeFilterItemView[currentPositionTypeSelected].statusType
                    )
                    listener.onEvent(Constant.ACTION_LOAD_LIST_COMIC_BY_FILTER, null, data)
                }
            }

            else -> {}
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }
}