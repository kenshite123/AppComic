package com.ggg.home.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ggg.common.vo.Status
import com.ggg.home.R
import com.ggg.home.data.model.CategoryModel
import com.ggg.home.data.model.ComicWithCategoryModel
import com.ggg.home.ui.adapter.ListCategoryAdapter
import com.ggg.home.ui.adapter.ListComicAdapter
import com.ggg.home.ui.main.HomeBaseFragment
import com.ggg.home.utils.Constant
import kotlinx.android.synthetic.main.fragment_category.*
import timber.log.Timber

class CategoryFragment : HomeBaseFragment() {
    private lateinit var viewModel: CategoryViewModel
    lateinit var listCategoryAdapter: ListCategoryAdapter
    lateinit var listCategories: List<CategoryModel>
    lateinit var listComicAdapter: ListComicAdapter
    var listComicByCategory: List<ComicWithCategoryModel> = listOf()

    var isFirstLoad = true
    var page: Long = 0
    var items: Long = 30
    var isLoadMore = true
    var positionOfCategorySelected = 0

    var isFirstLoadDataApi = true
    var isLoadAllData = false
    lateinit var gridLayoutManager : GridLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.d("onActivityCreated")
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CategoryViewModel::class.java)
        hideActionBar()
        showBottomNavView()

        initViews()
        initEvent()
        loadData()
    }

    private fun initViews() {
        listComicByCategory = listOf()
        listCategoryAdapter = ListCategoryAdapter(context!!, this, listOf())
        rvListCategory.setHasFixedSize(false)
        rvListCategory.layoutManager = LinearLayoutManager(context!!, RecyclerView.HORIZONTAL, false)
        rvListCategory.adapter = listCategoryAdapter

        gridLayoutManager = GridLayoutManager(context!!, 3)
        listComicAdapter = ListComicAdapter(context!!, this, this.listComicByCategory)
        rvListComic.setHasFixedSize(true)
        rvListComic.layoutManager = gridLayoutManager
        rvListComic.adapter = listComicAdapter
    }

    override fun initObserver() {
        viewModel.getAllListCategoriesResult.observe(this, Observer {
            loading(it)
            if (it.status == Status.SUCCESS || it.status == Status.SUCCESS_DB || it.status == Status.ERROR) {
                if (it.status == Status.SUCCESS_DB && it.data.isNullOrEmpty()) {
                    showLoading()
                }

                it.data?.let {
                    if (it.isNotEmpty()) {
                        it.first().isChoose = true
                        this.listCategories = it
                        this.page = 0
                        this.positionOfCategorySelected = 0
                        isFirstLoadDataApi = true
                        isLoadAllData = false
                        isLoadMore = false
                        listCategoryAdapter.notifyData(it)
                        loadListComicByCategory()
                    }
                }
            }
        })

        viewModel.getListComicByCategoryResult.observe(this, Observer {
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
                                    val list = this.listComicByCategory.toMutableList()
                                    list.addAll(it)

                                    this.listComicByCategory = list.toList()

                                    listComicAdapter.notifyData(this.listComicByCategory)
                                    isLoadAllData = it.size < items
                                } else {
                                    this.listComicByCategory = it
                                    listComicAdapter.notifyData(this.listComicByCategory)
                                }
                            }
                        }
                    }
                }

                it.data?.let {
                    if (isFirstLoadDataApi) {
                        this.listComicByCategory = it
                        listComicAdapter.notifyData(this.listComicByCategory)
                    }
                    isLoadAllData = it.size < items
                }
            }
        })
    }

    override fun initEvent() {
        rvListComic.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    if (!isLoadAllData && !isLoadMore) {
                        val visibleItemCount = 3
                        val totalItemCount = gridLayoutManager.itemCount
                        val pastVisibleItems = gridLayoutManager.findLastCompletelyVisibleItemPosition()

                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            isLoadMore = true
                            page++
                            loadListComicByCategory()
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
        viewModel.getAllListCategories()
    }

    override fun onEvent(eventAction: Int, control: View?, data: Any?) {
        when (eventAction) {
            Constant.ACTION_CLICK_ON_LIST_CATEGORY -> {
                val position = data as Int
                if (position != positionOfCategorySelected) {
                    this.positionOfCategorySelected = position
                    this.page = 0
                    isFirstLoadDataApi = true
                    isLoadAllData = false
                    isLoadMore = false
                    for (i in 0 until this.listCategories.count()) {
                        this.listCategories[i].isChoose = i == position
                    }

                    listCategoryAdapter.notifyDataSetChanged()
                    rvListComic.scrollToPosition(0)
                    loadListComicByCategory()
                }
            }

            Constant.ACTION_CLICK_ON_COMIC_WITH_CATEGORY_MODEL -> {
                val comicWithCategoryModel = data as ComicWithCategoryModel
                navigationController.showComicDetail(comicWithCategoryModel)
            }

            else -> {
                super.onEvent(eventAction, control, data)
            }
        }
    }

    private fun loadListComicByCategory() {
        val listCategoryId = listOf(listCategories[positionOfCategorySelected].id)
        val data = hashMapOf(
                "listCategoryId" to listCategoryId,
                "limit" to items,
                "offset" to page
        )
        viewModel.getListComicByCategory(data)
    }

    override fun onResume() {
        super.onResume()
        if (isFirstLoad) {
            initObserver()
            isFirstLoad = false
        }
    }

    companion object {
        val TAG = "CategoryFragment"
        @JvmStatic
        fun create() = CategoryFragment()
    }
}