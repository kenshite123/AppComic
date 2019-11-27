package com.ggg.home.ui.category_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
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
    var page: Long = 0
    var items: Long = 12
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
        showActionBar()
        hideBottomNavView()
        categoryModel = arguments!!["categoryOfComicModel"] as CategoryOfComicModel
        setTitleActionBar(categoryModel.categoryName)

        initViews()
        initEvent()
        loadData()
    }

    private fun initViews() {
        listComicAdapter = ListComicAdapter(context!!, this, listOf())
        rvListComic.setHasFixedSize(true)
        rvListComic.layoutManager = GridLayoutManager(context!!, 3)
        rvListComic.adapter = listComicAdapter
    }

    override fun initObserver() {
        viewModel.getListComicByCategoryResult.observe(this, Observer {
            loading(it)
            if (it.status == Status.SUCCESS || it.status == Status.ERROR) {
                it.data?.let {
                    isLoadMore = false
                    this.listComicByCategory = it.distinctBy { it.comicModel?.id }
                    listComicAdapter.notifyData(this.listComicByCategory)
                    if (this.listComicByCategory.count() >= items) {
                        isLoadMore = true
                    }
                }
            }
        })
    }

    override fun initEvent() {

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