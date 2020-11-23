package com.ggg.home.ui.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ggg.common.utils.OnEventControlListener
import com.ggg.home.R
import com.ggg.home.data.view.StatusTypeFilterItemView
import com.ggg.home.utils.Constant
import java.lang.ref.WeakReference

class ListStatusTypeFilterAdapter : RecyclerView.Adapter<ListStatusTypeFilterAdapter.ViewHolder> {
    lateinit var weakContext: WeakReference<Context>
    lateinit var listener: OnEventControlListener
    lateinit var listStatusTypeFilterItemView: List<StatusTypeFilterItemView>
    var isType = false

    constructor(context: Context, listener: OnEventControlListener, listStatusTypeFilterItemView: List<StatusTypeFilterItemView>) {
        this.weakContext = WeakReference(context)
        this.listener = listener
        this.listStatusTypeFilterItemView = listStatusTypeFilterItemView
    }

    fun notifyData(listStatusTypeFilterItemView: List<StatusTypeFilterItemView>, isType: Boolean) {
        this.listStatusTypeFilterItemView = listStatusTypeFilterItemView
        this.isType = isType
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(weakContext.get()).inflate(R.layout.item_status_type_category_filter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return this.listStatusTypeFilterItemView.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val statusTypeFilterItemView = listStatusTypeFilterItemView[position]
        if (statusTypeFilterItemView.statusType == Constant.FILTER_COMIC_STATUS_UPDATED && !isType) {
            holder.tvStatusType.text = "Completed"
        } else {
            holder.tvStatusType.text = statusTypeFilterItemView.statusType
        }
        if (statusTypeFilterItemView.isSelected) {
            holder.tvStatusType.setBackgroundResource(R.drawable.bg_item_filter_selected)
            holder.tvStatusType.setTextColor(Color.parseColor("#128bff"))
        } else {
            holder.tvStatusType.setBackgroundResource(R.drawable.bg_item_filter_unselected)
            holder.tvStatusType.setTextColor(Color.parseColor("#5b5b5b"))
        }

        holder.tvStatusType.setOnClickListener {
            listener.onEvent(Constant.ACTION_CLICK_ON_ITEM_STATUS_TYPE_FILTER, null, position)
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvStatusType: TextView = view.findViewById(R.id.tvStatusType)
    }
}