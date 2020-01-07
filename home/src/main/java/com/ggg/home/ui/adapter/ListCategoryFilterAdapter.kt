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
import com.ggg.home.data.view.CategoryFilterItemView
import com.ggg.home.utils.Constant
import java.lang.ref.WeakReference

class ListCategoryFilterAdapter: RecyclerView.Adapter<ListCategoryFilterAdapter.ViewHolder> {

    lateinit var weakContext: WeakReference<Context>
    lateinit var listener: OnEventControlListener
    lateinit var listCategories: List<CategoryFilterItemView>

    constructor(context: Context, listener: OnEventControlListener, listCategories: List<CategoryFilterItemView>) {
        this.weakContext = WeakReference(context)
        this.listener = listener
        this.listCategories = listCategories
    }

    fun notifyData(listCategories: List<CategoryFilterItemView>) {
        this.listCategories = listCategories
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(weakContext.get()).inflate(R.layout.item_status_type_category_filter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listCategories.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val categoryFilterItemView = listCategories[position]
        if (categoryFilterItemView.isAll) {
            holder.tvStatusType.text = weakContext.get()!!.getString(R.string.TEXT_ALL)
        } else {
            holder.tvStatusType.text = categoryFilterItemView.categoryModel.name
        }

        if (categoryFilterItemView.isSelected) {
            holder.tvStatusType.setBackgroundResource(R.drawable.bg_item_filter_selected)
            holder.tvStatusType.setTextColor(Color.parseColor("#128bff"))
        } else {
            holder.tvStatusType.setBackgroundResource(R.drawable.bg_item_filter_unselected)
            holder.tvStatusType.setTextColor(Color.parseColor("#5b5b5b"))
        }

        holder.tvStatusType.setOnClickListener {
            listener.onEvent(Constant.ACTION_CLICK_ON_ITEM_CATEGORY_FILTER, null, position)
        }
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var tvStatusType: TextView = view.findViewById(R.id.tvStatusType)
    }
}