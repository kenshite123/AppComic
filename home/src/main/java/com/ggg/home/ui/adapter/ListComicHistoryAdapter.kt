package com.ggg.home.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ggg.common.GGGAppInterface
import com.ggg.common.utils.OnEventControlListener
import com.ggg.home.R
import com.ggg.home.data.model.HistoryModel
import com.ggg.home.utils.Constant
import java.lang.ref.WeakReference

class ListComicHistoryAdapter : RecyclerView.Adapter<ListComicHistoryAdapter.ViewHolder> {
    lateinit var weakContext: WeakReference<Context>
    lateinit var listener: OnEventControlListener
    lateinit var listHistoryModel: List<HistoryModel>

    constructor(context: Context, listener: OnEventControlListener, listHistoryModel: List<HistoryModel>) {
        this.weakContext = WeakReference(context)
        this.listener = listener
        this.listHistoryModel = listHistoryModel
    }

    fun notifyData(listHistoryModel: List<HistoryModel>) {
        this.listHistoryModel = listHistoryModel
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(weakContext.get()).inflate(R.layout.item_comic, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listHistoryModel.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val historyModel = listHistoryModel[position]
        val comic = historyModel.comicModel!!

        Glide.with(weakContext.get()!!)
                .load(comic.imageUrl)
                .placeholder(GGGAppInterface.gggApp.circularProgressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivComic)

        holder.tvComicTitle.text = comic.title
        holder.tvChap.text = comic.latestChapter

        holder.ivComic.setOnClickListener {
            listener.onEvent(Constant.ACTION_CLICK_ON_COMIC_HISTORY, it, historyModel)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivComic: ImageView = itemView.findViewById(R.id.ivComic)
        var tvComicTitle: TextView = itemView.findViewById(R.id.tvComicTitle)
        var tvChap: TextView = itemView.findViewById(R.id.tvChap)
    }
}