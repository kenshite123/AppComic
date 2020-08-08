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
import com.ggg.home.data.model.ComicWithCategoryModel
import com.ggg.home.utils.Constant
import java.lang.ref.WeakReference
import kotlin.collections.ArrayList

class ListComicAdapter : RecyclerView.Adapter<ListComicAdapter.ViewHolder> {

    lateinit var weakContext: WeakReference<Context>
    lateinit var listener: OnEventControlListener
    lateinit var listComic: List<ComicWithCategoryModel>
    lateinit var listComicSearch: List<ComicModel>
    private var isFromSearch: Boolean = false

    constructor(context: Context, listener: OnEventControlListener, listComic: List<ComicWithCategoryModel>) {
        this.weakContext = WeakReference(context)
        this.listener = listener
        this.listComic = listComic
    }

    constructor(context: Context, listener: OnEventControlListener, listComic: List<ComicModel>,isFromSearch: Boolean) {
        this.weakContext = WeakReference(context)
        this.listener = listener
        this.listComicSearch = listComic
        this.isFromSearch = isFromSearch
    }

    fun notifyData(listComic: List<ComicWithCategoryModel>) {
        this.listComic = listComic
        notifyDataSetChanged()
    }

    fun notifyDataSearch(listComicSearch: List<ComicModel>) {
        this.listComicSearch = listComicSearch
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListComicAdapter.ViewHolder {
        val view = LayoutInflater.from(weakContext.get()).inflate(R.layout.item_comic, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (isFromSearch) {
            listComicSearch.count()
        } else {
            listComic.count()
        }
    }

    override fun onBindViewHolder(itemViewHolder: ViewHolder, position: Int) {
        if (isFromSearch) {
            val comic = listComicSearch[position]
            Glide.with(weakContext.get()!!)
                    .load(comic.imageUrl)
                    .placeholder(GGGAppInterface.gggApp.circularProgressDrawable)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(itemViewHolder.ivComic)

            itemViewHolder.tvComicTitle.text = comic.title
            itemViewHolder.tvChap.text = comic.latestChapter

            itemViewHolder.ivComic.setOnClickListener {
                listener.onEvent(Constant.ACTION_CLICK_ON_COMIC, it, comic.id)
            }
        } else {
            val comicWithCategoryModel = listComic[position]
            val comic = comicWithCategoryModel.comicModel!!
            Glide.with(weakContext.get()!!)
                    .load(comic.imageUrl)
                    .placeholder(GGGAppInterface.gggApp.circularProgressDrawable)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(itemViewHolder.ivComic)

            itemViewHolder.tvComicTitle.text = comic.title
            itemViewHolder.tvChap.text = comic.latestChapter

            itemViewHolder.ivComic.setOnClickListener {
                listener.onEvent(Constant.ACTION_CLICK_ON_COMIC_WITH_CATEGORY_MODEL, it, comicWithCategoryModel)
            }
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {
        var ivComic: ImageView = itemView.findViewById(R.id.ivComic)
        var tvComicTitle: TextView = itemView.findViewById(R.id.tvComicTitle)
        var tvChap: TextView = itemView.findViewById(R.id.tvChap)
    }
}