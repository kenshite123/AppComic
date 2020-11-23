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
import com.ggg.home.data.model.CategoryModel
import com.ggg.home.utils.Constant
import java.lang.ref.WeakReference

class ListCategoryAdapter : RecyclerView.Adapter<ListCategoryAdapter.ViewHolder> {

    lateinit var weakContext: WeakReference<Context>
    lateinit var listener: OnEventControlListener
    lateinit var listCategories: List<CategoryModel>

    constructor(context: Context, listener: OnEventControlListener, listCategories: List<CategoryModel>) {
        this.weakContext = WeakReference(context)
        this.listener = listener
        this.listCategories = listCategories
    }

    fun notifyData(listCategories: List<CategoryModel>) {
        this.listCategories = listCategories
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(weakContext.get()).inflate(R.layout.item_list_category, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listCategories.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val categoryModel = listCategories[position]
        holder.tvCategoryName.text = categoryModel.name

        if (categoryModel.isChoose) {
            holder.tvCategoryName.setTextColor(Color.WHITE)
            holder.tvCategoryName.setBackgroundResource(R.drawable.bg_item_list_category)
        } else {
            holder.tvCategoryName.setTextColor(Color.parseColor("#757575"))
            holder.tvCategoryName.setBackgroundColor(Color.WHITE)
        }

        holder.tvCategoryName.setOnClickListener {
            listener.onEvent(Constant.ACTION_CLICK_ON_LIST_CATEGORY, it, position)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvCategoryName: TextView = itemView.findViewById(R.id.tvCategoryName)
    }
}