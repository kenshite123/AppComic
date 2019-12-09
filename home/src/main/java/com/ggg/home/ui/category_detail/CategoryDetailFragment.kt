package com.ggg.home.ui.category_detail

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
import com.ggg.home.data.model.CategoryModel
import com.ggg.home.data.model.CategoryOfComicModel
import com.ggg.home.data.model.ComicWithCategoryModel
import com.ggg.home.ui.adapter.ListComicAdapter
import com.ggg.home.ui.main.HomeBaseFragment
import com.ggg.home.utils.Constant
import kotlinx.android.synthetic.main.fragment_category_detail.*
import org.jetbrains.anko.bundleOf
import timber.log.Timber

class CategoryDetailFragment : HomeBaseFragment() {
    private lateinit var viewModel: CategoryDetailViewModel
    var isFirstLoad = true
    lateinit var categoryModel: CategoryOfComicModel

    lateinit var listComicAdapter: ListComicAdapter
    var listComicByCategory: List<ComicWithCategoryModel> = arrayListOf()
    lateinit var gridLayoutManager : GridLayoutManager

    var page: Long = 0
    var items: Long = 30

    var isFirstLoadDataApi = true
    var isLoadAllData = false
    var isLoadMore = true

    companion object {
        val TAG = "CategoryDetailFragment"
        @JvmStatic
        fun create(categoryOfComicModel: CategoryOfComicModel) : CategoryDetailFragment {
            val fragment = CategoryDetailFragment()
            val bundle = bundleOf(
                    "categoryOfComicModel" to categoryOfComicModel
            )
            fragment.arguments = bundle
            return fragment
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.d("onActivityCreated")
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CategoryDetailViewModel::class.java)
        isFirstLoad = true
        showActionBar()
        hideBottomNavView()
        categoryModel = arguments!!["categoryOfComicModel"] as CategoryOfComicModel
        setTitleActionBar(categoryModel.categoryName)

        initViews()
        initEvent()
        loadData()
    }

    private fun initViews() {
        listComicByCategory = listOf()
        gridLayoutManager = GridLayoutManager(context!!, 3)
        listComicAdapter = ListComicAdapter(context!!, this, this.listComicByCategory)
        rvListComic.setHasFixedSize(true)
        rvListComic.layoutManager = gridLayoutManager
        rvListComic.adapter = listComicAdapter
    }

    override fun initObserver() {
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

                if (isFirstLoadDataApi) {
                    it.data?.let {
                        this.listComicByCategory = it
                        listComicAdapter.notifyData(this.listComicByCategory)
                    }
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
                        val pastVisibleItems = gridLayoutManager.findLastCompletelyVisibleItemPosition()

                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            isLoadMore = true
                            page++
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
                "categoryId" to this.categoryModel.categoryId,
                "limit" to items,
                "offset" to page
        )
        viewModel.getListComicByCategory(data)
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