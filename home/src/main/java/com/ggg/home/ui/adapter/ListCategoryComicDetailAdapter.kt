package com.ggg.home.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ggg.common.utils.OnEventControlListener
import com.ggg.home.R
import com.ggg.home.data.model.CategoryModel
import com.ggg.home.data.model.CategoryOfComicModel
import java.lang.ref.WeakReference

class ListCategoryComicDetailAdapter : RecyclerView.Adapter<ListCategoryComicDetailAdapter.ViewHolder> {
    lateinit var weakContext: WeakReference<Context>
    lateinit var listener: OnEventControlListener
    lateinit var listCategories: List<CategoryOfComicModel>

    constructor(context: Context, listener: OnEventControlListener, listCategories: List<CategoryOfComicModel>) {
        this.weakContext = WeakReference(context)
        this.listener = listener
        this.listCategories = listCategories
    }

    fun notifyData(listCategories: List<CategoryOfComicModel>) {
        this.listCategories = listCategories
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(weakContext.get()).inflate(R.layout.item_category_comic_detail, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listCategories.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val categoryOfComicModel = listCategories[position]
        holder.tvCategoryName.text = categoryOfComicModel.categoryName
        holder.tvCategoryName.setOnClickListener {

        }
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var tvCategoryName: TextView = itemView.findViewById(R.id.tvCategoryName)
    }
}