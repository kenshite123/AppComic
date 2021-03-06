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
import com.ggg.home.utils.Constant
import java.lang.ref.WeakReference

class ListCategoryComicDetailAdapter : RecyclerView.Adapter<ListCategoryComicDetailAdapter.ViewHolder> {
    lateinit var weakContext: WeakReference<Context>
    lateinit var listener: OnEventControlListener
    var listCategories: List<CategoryOfComicModel>? = null
    var isRanking: Boolean = false

    constructor(context: Context, listener: OnEventControlListener, listCategories: List<CategoryOfComicModel>?, isRanking: Boolean) {
        this.weakContext = WeakReference(context)
        this.listener = listener
        this.listCategories = listCategories
        this.isRanking = isRanking
    }

    fun notifyData(listCategories: List<CategoryOfComicModel>?, isRanking: Boolean) {
        this.listCategories = listCategories
        this.isRanking = isRanking
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = if (isRanking) {
            LayoutInflater.from(weakContext.get()).inflate(R.layout.item_category_for_rank, parent, false)
        } else {
            LayoutInflater.from(weakContext.get()).inflate(R.layout.item_category_comic_detail, parent, false)
        }
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        listCategories?.let {
            return it.count()
        }
        return 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val categoryOfComicModel = listCategories?.get(position)
        holder.tvCategoryName.text = categoryOfComicModel?.categoryName
        holder.tvCategoryName.setOnClickListener {
            listener.onEvent(Constant.ACTION_CLICK_ON_ITEM_CATEGORY_OF_COMIC_DETAIL, it, categoryOfComicModel)
        }
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var tvCategoryName: TextView = itemView.findViewById(R.id.tvCategoryName)
    }
}