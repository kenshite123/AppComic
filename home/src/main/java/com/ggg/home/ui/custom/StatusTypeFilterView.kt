package com.ggg.home.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.ggg.common.utils.OnEventControlListener
import com.ggg.home.R
import com.ggg.home.data.view.StatusTypeFilterItemView
import com.ggg.home.ui.adapter.ListStatusTypeFilterAdapter
import com.ggg.home.utils.Constant
import kotlinx.android.synthetic.main.layout_status_type_filter_view.view.*
import java.lang.ref.WeakReference

class StatusTypeFilterView : ConstraintLayout {
    lateinit var mContext: WeakReference<Context>
    lateinit var listener: OnEventControlListener
    var listStatusTypeFilterItemView = listOf<StatusTypeFilterItemView>()
    var isType: Boolean = false
    lateinit var listStatusTypeFilterAdapter: ListStatusTypeFilterAdapter

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        mContext = WeakReference(context)
        View.inflate(context, R.layout.layout_status_type_filter_view, this)
        initViews()
        addEvents()
    }

    fun setData(listener: OnEventControlListener, listStatusTypeFilterItemView: List<StatusTypeFilterItemView>, isType: Boolean) {
        this.listener = listener
        this.listStatusTypeFilterItemView = listStatusTypeFilterItemView
        this.isType = isType
    }

    private fun initViews() {

    }

    private fun addEvents() {
        v1.setOnClickListener {
            listener.onEvent(Constant.ACTION_CLOSE_STATUS_TYPE_FILTER_SELECT_VIEW, null, null)
        }
    }

    fun reloadViews() {
        listStatusTypeFilterAdapter = ListStatusTypeFilterAdapter(mContext.get()!!, listener, listStatusTypeFilterItemView)
        rvStatusType.setHasFixedSize(false)
        rvStatusType.layoutManager = GridLayoutManager(mContext.get(), 3)
        rvStatusType.adapter = listStatusTypeFilterAdapter

        if (isType) {
            tvTitle.text = mContext.get()!!.getString(R.string.TEXT_SORT_BY)
        } else {
            tvTitle.text = mContext.get()!!.getString(R.string.TEXT_STATUS)
        }
        listStatusTypeFilterAdapter.notifyData(listStatusTypeFilterItemView)
    }
}