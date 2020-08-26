package com.ggg.home.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ggg.common.utils.OnEventControlListener
import com.ggg.home.R
import com.ggg.home.data.model.ChapterModel
import com.ggg.home.utils.Constant
import java.lang.ref.WeakReference

class ListChapterDownloadImageAdapter : RecyclerView.Adapter<ListChapterDownloadImageAdapter.ViewHolder> {
    lateinit var weakContext: WeakReference<Context>
    lateinit var listener: OnEventControlListener
    lateinit var listChapters: List<ChapterModel>

    constructor(context: Context, listener: OnEventControlListener, listChapters: List<ChapterModel>){
        this.weakContext = WeakReference(context)
        this.listener = listener
        this.listChapters = listChapters
    }

    fun notifyData(listChapters: List<ChapterModel>) {
        this.listChapters = listChapters
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(weakContext.get()).inflate(R.layout.item_list_chapter_download_image, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return this.listChapters.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chapterModel = listChapters[position]
        if (chapterModel.isSelected) {
            holder.tvChapter.setBackgroundResource(R.drawable.bg_chapter_selected)
        } else {
            holder.tvChapter.setBackgroundResource(R.drawable.bg_chapter_unselected)
        }

        holder.tvChapter.text = chapterModel.chapterName
        holder.tvChapter.setOnClickListener {
            chapterModel.isSelected = !chapterModel.isSelected
            notifyItemChanged(position)
            listener.onEvent(Constant.ACTION_CLICK_ON_CHAPTER_TO_DOWNLOAD_IMAGE, null, null)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvChapter: TextView = itemView.findViewById(R.id.tvChapter)
    }
}