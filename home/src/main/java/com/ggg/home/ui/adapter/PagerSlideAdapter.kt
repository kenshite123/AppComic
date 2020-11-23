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
import com.ggg.home.data.model.ComicModel
import com.ggg.home.data.model.ComicWithCategoryModel
import com.ggg.home.utils.Constant
import java.lang.ref.WeakReference

class PagerSlideAdapter : RecyclerView.Adapter<PagerSlideAdapter.ViewHolder> {
    lateinit var weakContext: WeakReference<Context>
    lateinit var listener: OnEventControlListener
    lateinit var listBanners: List<ComicWithCategoryModel>

    constructor(context: Context, listener: OnEventControlListener, listBanners: List<ComicWithCategoryModel>) {
        this.weakContext = WeakReference(context)
        this.listener = listener
        this.listBanners = listBanners
    }

    fun notifyData(listBanners: List<ComicWithCategoryModel>) {
        this.listBanners = listBanners
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(weakContext.get()).inflate(R.layout.item_slide_pager, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listBanners.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comicWithCategoryModel = listBanners[position]
        val banner = comicWithCategoryModel.comicModel!!
        Glide.with(weakContext.get()!!)
                .load(banner.bigImageUrl)
                .placeholder(GGGAppInterface.gggApp.circularProgressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivSlide)

        holder.ivSlide.setOnClickListener {
            listener.onEvent(Constant.ACTION_CLICK_ON_COMIC, it, banner.id)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivSlide : ImageView = itemView.findViewById(R.id.ivSlide)
    }
}