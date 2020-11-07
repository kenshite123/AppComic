package com.ggg.home.ui.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ggg.common.utils.OnEventControlListener
import com.ggg.home.R
import com.ggg.home.data.model.ChapterHadRead
import com.ggg.home.data.model.ChapterModel
import com.ggg.home.data.model.ComicWithCategoryModel
import com.ggg.home.ui.custom.ItemChapterView
import com.ggg.home.utils.Constant
import java.lang.ref.WeakReference

class ListChapterAdapter : RecyclerView.Adapter<ListChapterAdapter.ViewHolder> {
    lateinit var weakContext: WeakReference<Context>
    lateinit var listener: OnEventControlListener
    lateinit var listChapters: List<ChapterHadRead>
    var comicWithCategoryModel: ComicWithCategoryModel? = null

    constructor(context: Context, listener: OnEventControlListener, listChapters: List<ChapterHadRead>, comicWithCategoryModel: ComicWithCategoryModel?){
        this.weakContext = WeakReference(context)
        this.listener = listener
        this.listChapters = listChapters
        this.comicWithCategoryModel = comicWithCategoryModel
    }

    fun notifyData(listChapters: List<ChapterHadRead>) {
        this.listChapters = listChapters
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(weakContext.get()).inflate(R.layout.item_bound_item_list_chapters, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return this.listChapters.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chapterHadRead = listChapters[position]
        holder.itemChapterView.setData(chapterHadRead = chapterHadRead,
                comicId = comicWithCategoryModel?.comicModel?.id ?: -1, position = position,
                listener = listener)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemChapterView: ItemChapterView = itemView.findViewById(R.id.itemChapterView)
    }
}