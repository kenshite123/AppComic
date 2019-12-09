package com.ggg.home.ui.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ggg.common.utils.OnEventControlListener
import com.ggg.home.R
import com.ggg.home.data.model.ChapterHadRead
import com.ggg.home.data.model.ChapterModel
import com.ggg.home.utils.Constant
import java.lang.ref.WeakReference

class ListChapterAdapter : RecyclerView.Adapter<ListChapterAdapter.ViewHolder> {
    lateinit var weakContext: WeakReference<Context>
    lateinit var listener: OnEventControlListener
    lateinit var listChapters: List<ChapterHadRead>

    constructor(context: Context, listener: OnEventControlListener, listChapters: List<ChapterHadRead>){
        this.weakContext = WeakReference(context)
        this.listener = listener
        this.listChapters = listChapters
    }

    fun notifyData(listChapters: List<ChapterHadRead>) {
        this.listChapters = listChapters
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(weakContext.get()).inflate(R.layout.item_list_chapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return this.listChapters.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chapterHadRead = listChapters[position]
        val chapterModel = chapterHadRead.chapterModel!!
        holder.tvChapterName.text = chapterModel.chapterName
        holder.tvUpdateDate.text = chapterModel.dateUpdate

        if (chapterHadRead.ccHadReadModel.isNullOrEmpty()) {
            holder.tvChapterName.setTextColor(Color.parseColor("#161616"))
        } else {
            holder.tvChapterName.setTextColor(Color.parseColor("#949494"))
        }

        holder.llChapters.setOnClickListener {
            listener.onEvent(Constant.ACTION_CLICK_ON_CHAPTER, it, position)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvChapterName: TextView = itemView.findViewById(R.id.tvChapterName)
        var tvUpdateDate: TextView = itemView.findViewById(R.id.tvUpdateDate)
        var llChapters: LinearLayout = itemView.findViewById(R.id.llChapters)
    }
}