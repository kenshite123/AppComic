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

class ListComicFollowAdapter : RecyclerView.Adapter<ListComicFollowAdapter.ViewHolder> {

    lateinit var weakContext: WeakReference<Context>
    lateinit var listener: OnEventControlListener
    lateinit var listComic: List<ComicModel>

    private var isEdit = false

    constructor(context: Context, listener: OnEventControlListener, listComic: List<ComicModel>, isEdit: Boolean = false) {
        this.weakContext = WeakReference(context)
        this.listener = listener
        this.listComic = listComic
        this.isEdit = isEdit
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(weakContext.get()).inflate(R.layout.item_comic_library, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listComic.count()
    }

    override fun onBindViewHolder(itemViewHolder: ViewHolder, position: Int) {
        val comic = listComic[position]
        Glide.with(weakContext.get()!!)
                .load(comic.imageUrl)
                .placeholder(GGGAppInterface.gggApp.circularProgressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(itemViewHolder.ivComic)

        itemViewHolder.tvComicTitle.text = comic.title
        itemViewHolder.tvChap.text = comic.latestChapter

        if (this.isEdit) {
            if (comic.isSelected) {
                itemViewHolder.ivChecked.visibility = View.VISIBLE
                itemViewHolder.ivChecked.setImageResource(R.drawable.icon_checked)
            } else {
//                itemViewHolder.ivChecked.setImageResource(R.drawable.icon_uncheck)
                itemViewHolder.ivChecked.visibility = View.GONE
            }
        } else {
            itemViewHolder.ivChecked.visibility = View.GONE
        }

        itemViewHolder.ivComic.setOnClickListener {
            if (this.isEdit) {
                comic.isSelected = !comic.isSelected
                notifyItemChanged(position)
                listener.onEvent(Constant.ACTION_SELECT_OR_DESELECT_COMIC_TO_EDIT, null, position)
            } else {
                listener.onEvent(Constant.ACTION_CLICK_ON_COMIC, null, comic.id)
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {
        var ivComic: ImageView = itemView.findViewById(R.id.ivComic)
        var ivChecked: ImageView = itemView.findViewById(R.id.ivChecked)
        var tvComicTitle: TextView = itemView.findViewById(R.id.tvComicTitle)
        var tvChap: TextView = itemView.findViewById(R.id.tvChap)
    }
}