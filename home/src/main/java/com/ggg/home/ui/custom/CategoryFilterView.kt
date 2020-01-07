package com.ggg.home.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.ggg.common.utils.OnEventControlListener
import com.ggg.home.R
import com.ggg.home.data.view.CategoryFilterItemView
import com.ggg.home.ui.adapter.ListCategoryFilterAdapter
import com.ggg.home.utils.Constant
import kotlinx.android.synthetic.main.layout_category_filter.view.*
import java.lang.ref.WeakReference

class CategoryFilterView: ConstraintLayout, OnEventControlListener {
    lateinit var mContext: WeakReference<Context>
    lateinit var listener: OnEventControlListener
    var listCategoryFilterView: List<CategoryFilterItemView> = listOf()
    lateinit var listCategoryFilterAdapter: ListCategoryFilterAdapter

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        mContext = WeakReference(context)
        View.inflate(context, R.layout.layout_category_filter, this)
        initViews()
        addEvents()
    }

    fun setData(listener: OnEventControlListener, listCategoryFilterView: List<CategoryFilterItemView>) {
        this.listener = listener
        this.listCategoryFilterView = listCategoryFilterView
    }

    private fun initViews() {

    }

    private fun addEvents() {
        tvReset.setOnClickListener {
            resetCategorySelected()
        }

        tvDone.setOnClickListener {
            listener.onEvent(Constant.ACTION_CLOSE_CATEGORY_FILTER_SELECT_VIEW, null, listCategoryFilterView)
        }

        v1.setOnClickListener {
            listener.onEvent(Constant.ACTION_CLOSE_CATEGORY_FILTER_SELECT_VIEW, null, listCategoryFilterView)
        }

        ctlFilter.setOnClickListener {

        }
    }

    private fun resetCategorySelected() {
        for (i in 0 until listCategoryFilterView.count()) {
            val categoryFilterItemView = listCategoryFilterView[i]
            categoryFilterItemView.isSelected = categoryFilterItemView.isAll
        }

        listCategoryFilterAdapter.notifyData(this.listCategoryFilterView)
    }

    fun reloadViews() {
        listCategoryFilterAdapter = ListCategoryFilterAdapter(mContext.get()!!, this, listCategoryFilterView)
        rvCategory.setHasFixedSize(false)
        rvCategory.layoutManager = GridLayoutManager(mContext.get(), 3)
        rvCategory.adapter = listCategoryFilterAdapter
    }

    override fun onEvent(eventAction: Int, control: View?, data: Any?) {
        when (eventAction) {
            Constant.ACTION_CLICK_ON_ITEM_CATEGORY_FILTER -> {
                val position = data as Int
                if (position == 0) {
                    resetCategorySelected()
                } else {
                    listCategoryFilterView[0].isSelected = false
                    val categoryFilterItemView = listCategoryFilterView[position]
                    categoryFilterItemView.isSelected = !categoryFilterItemView.isSelected

                    val list = listCategoryFilterView.filter { it.isSelected == true }
                    if (list.isEmpty()) {
                        resetCategorySelected()
                    }

                    listCategoryFilterAdapter.notifyData(this.listCategoryFilterView)
                }
            }

            else -> {}
        }
    }
}
