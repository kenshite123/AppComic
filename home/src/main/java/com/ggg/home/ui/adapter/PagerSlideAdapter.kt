package com.ggg.home.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ggg.home.R

class PagerSlideAdapter : RecyclerView.Adapter<PagerSlideAdapter.ViewHolder> {
    lateinit var context: Context
    lateinit var listImage: ArrayList<String>

    constructor(context: Context, listImage: ArrayList<String>) {
        this.context = context
        this.listImage = listImage
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_slide_pager, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listImage.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val url = listImage[position]
        Glide.with(context)
                .load(url)
                .thumbnail(Glide.with(context).load(R.drawable.loading))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.ivSlide)
    }

    class ViewHolder : RecyclerView.ViewHolder {
        var ivSlide : ImageView
        constructor(itemView: View) : super(itemView) {
            ivSlide = itemView.findViewById(R.id.ivSlide)
        }
    }
}