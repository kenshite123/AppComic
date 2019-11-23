package com.ggg.home.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ggg.common.vo.Status
import com.ggg.home.R
import com.ggg.home.data.model.CategoryModel
import com.ggg.home.ui.adapter.ListCategoryAdapter
import com.ggg.home.ui.main.HomeBaseFragment
import com.ggg.home.utils.Constant
import kotlinx.android.synthetic.main.fragment_category.*
import timber.log.Timber

class CategoryFragment : HomeBaseFragment() {
    private lateinit var viewModel: CategoryViewModel
    lateinit var listCategoryAdapter: ListCategoryAdapter
    lateinit var listCategories: List<CategoryModel>

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
        initObserver()
        initEvent()
        loadData()
    }

    private fun initViews() {
        listCategoryAdapter = ListCategoryAdapter(context!!, this, listOf())
        rvListCategory.setHasFixedSize(false)
        rvListCategory.layoutManager = LinearLayoutManager(context!!, RecyclerView.HORIZONTAL, false)
        rvListCategory.adapter = listCategoryAdapter
    }

    override fun initObserver() {
        viewModel.getAllListCategoriesResult.observe(this, Observer {
            loading(it)
            if (it.status == Status.SUCCESS || it.status == Status.ERROR) {
                it.data?.let {
                    if (it.isNotEmpty()) {
                        it.first().isChoose = true
                        this.listCategories = it
                        listCategoryAdapter.notifyData(it)
                    }
                }
            }
        })
    }

    override fun initEvent() {
    }

    private fun loadData() {
        viewModel.getAllListCategories()
    }

    override fun onEvent(eventAction: Int, control: View?, data: Any?) {
        when (eventAction) {
            Constant.ACTION_CLICK_ON_LIST_CATEGORY -> {
                val position = data as Int
                for (i in 0 until this.listCategories.count()) {
                    this.listCategories[i].isChoose = i == position
                }

                listCategoryAdapter.notifyDataSetChanged()
            }

            else -> {
                super.onEvent(eventAction, control, data)
            }
        }
    }

    fun loadListComicByCategory(categoryId: Long) {

    }

    companion object {
        val TAG = "CategoryFragment"
        @JvmStatic
        fun create() = CategoryFragment()
    }
}