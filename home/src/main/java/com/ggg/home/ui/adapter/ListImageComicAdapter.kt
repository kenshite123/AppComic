package com.ggg.home.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ggg.common.GGGAppInterface
import com.ggg.common.utils.OnEventControlListener
import com.ggg.home.R
import com.ggg.home.utils.Constant
import java.lang.ref.WeakReference

class ListImageComicAdapter : RecyclerView.Adapter<ListImageComicAdapter.ViewHolder> {
    lateinit var weakContext: WeakReference<Context>
    lateinit var listener: OnEventControlListener
    lateinit var listImageComic: List<String>

    constructor(context: Context, listener: OnEventControlListener, listImageComic: List<String>) {
        this.weakContext = WeakReference(context)
        this.listener = listener
        this.listImageComic = listImageComic
    }

    fun notifyData(listImageComic: List<String>) {
        this.listImageComic = listImageComic
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(weakContext.get()).inflate(R.layout.item_list_image_comic, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return this.listImageComic.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageURL = listImageComic[position]

        Glide.with(weakContext.get())
                .load(imageURL)
                .placeholder(GGGAppInterface.gggApp.circularProgressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivComic)

        holder.ivComic.setOnClickListener {
            listener.onEvent(Constant.ACTION_CLICK_ON_IMAGE_COMIC_TO_SHOW_NAVIGATION, it, null)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivComic: ImageView = itemView.findViewById(R.id.ivComic)
    }
}