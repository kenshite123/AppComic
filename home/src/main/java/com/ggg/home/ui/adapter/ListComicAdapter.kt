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
import com.ggg.home.data.model.ComicModel
import java.lang.ref.WeakReference

class ListComicAdapter : RecyclerView.Adapter<ListComicAdapter.ViewHolder> {

    lateinit var weakContext: WeakReference<Context>
    lateinit var listener: OnEventControlListener
    lateinit var listComic: ArrayList<ComicModel>

    constructor(weakContext: WeakReference<Context>, listener: OnEventControlListener, listComic: ArrayList<ComicModel>) {
        this.weakContext = weakContext
        this.listener = listener
        this.listComic = listComic
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(weakContext.get()).inflate(R.layout.item_comic, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listComic.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comic = listComic[position]
        Glide.with(weakContext.get())
                .load(comic.thumb)
                .placeholder(GGGAppInterface.gggApp.circularProgressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.ivComic)

        holder.tvComicTitle.text = comic.title
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {
        var ivComic: ImageView = itemView.findViewById(R.id.ivComic)
        var tvComicTitle: TextView = itemView.findViewById(R.id.tvComicTitle)
    }
}