package com.ggg.home.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ggg.common.GGGAppInterface
import com.ggg.common.utils.OnEventControlListener
import com.ggg.home.R
import com.ggg.home.data.model.ComicDownloadedModel
import com.ggg.home.data.model.ComicModel
import com.ggg.home.utils.Constant
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import java.lang.ref.WeakReference

class ListComicDownloadedAdapter : RecyclerView.Adapter<ListComicDownloadedAdapter.ViewHolder> {
    lateinit var weakContext: WeakReference<Context>
    lateinit var listener: OnEventControlListener
    lateinit var listComic: List<ComicModel>

    constructor(context: Context, listener: OnEventControlListener, listComic: List<ComicModel>) {
        this.weakContext = WeakReference(context)
        this.listener = listener
        this.listComic = listComic
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(weakContext.get()).inflate(R.layout.item_comic, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return this.listComic.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comic = listComic[position]
        Glide.with(weakContext.get()!!)
                .load(comic.imageUrl)
                .placeholder(GGGAppInterface.gggApp.circularProgressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivComic)

        holder.tvComicTitle.text = comic.title
        holder.tvChap.text = comic.latestChapter

        if (comic.percent == 100) {
            holder.ctlProgressDownload.visibility = View.GONE
            holder.ivComic.setOnClickListener {
                listener.onEvent(Constant.ACTION_CLICK_ON_COMIC_DOWNLOADED, it, comic.id)
            }
        } else {
            holder.ctlProgressDownload.visibility = View.VISIBLE
            holder.tvPercent.text = "${comic.percent}%"
            holder.circularProgressBar.apply {
                progressMax = 100F
                progress = comic.percent.toFloat()
            }
            holder.ivComic.setOnClickListener {  }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivComic: ImageView = itemView.findViewById(R.id.ivComic)
        var tvComicTitle: TextView = itemView.findViewById(R.id.tvComicTitle)
        var tvChap: TextView = itemView.findViewById(R.id.tvChap)
        var ctlProgressDownload: ConstraintLayout = itemView.findViewById(R.id.ctlProgressDownload)
        var circularProgressBar: CircularProgressBar = itemView.findViewById(R.id.circularProgressBar)
        var tvPercent: TextView = itemView.findViewById(R.id.tvPercent)
    }
}