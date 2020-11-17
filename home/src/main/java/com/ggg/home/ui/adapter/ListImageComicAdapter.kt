package com.ggg.home.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.signature.ObjectKey
import com.ggg.common.GGGAppInterface
import com.ggg.common.utils.OnEventControlListener
import com.ggg.home.R
import com.ggg.home.utils.Constant
import java.io.File
import java.lang.ref.WeakReference

class ListImageComicAdapter : RecyclerView.Adapter<ListImageComicAdapter.ViewHolder> {
    lateinit var weakContext: WeakReference<Context>
    lateinit var listener: OnEventControlListener
    lateinit var listImageComic: List<String>
    var isDownloaded: Boolean = false
    var comicId: Long = 0
    var chapterId: Long = 0

    constructor(context: Context, listener: OnEventControlListener, listImageComic: List<String>,
                isDownloaded: Boolean, comicId: Long, chapterId: Long) {
        this.weakContext = WeakReference(context)
        this.listener = listener
        this.listImageComic = listImageComic
        this.isDownloaded = isDownloaded
        this.comicId = comicId
        this.chapterId = chapterId
    }

    fun notifyData(listImageComic: List<String>, isDownloaded: Boolean, comicId: Long, chapterId: Long) {
        this.listImageComic = listImageComic
        this.isDownloaded = isDownloaded
        this.comicId = comicId
        this.chapterId = chapterId
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

        if (!isDownloaded) {
            Glide.with(weakContext.get()!!)
                    .load(imageURL)
                    .placeholder(GGGAppInterface.gggApp.circularProgressDrawable)
                    .apply(RequestOptions()
                            .override(Target.SIZE_ORIGINAL)
                            .format(DecodeFormat.PREFER_ARGB_8888))
                    .signature(ObjectKey(Constant.SIGNATURE_IMAGE_CACHE))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.ivComic)
        } else {
            val imgSplit = imageURL.split("/")
            val fileName = "${weakContext.get()!!.filesDir.absolutePath}/DownloadComic/${comicId}/${chapterId}/${imgSplit[imgSplit.count() - 1]}"
            val file = File(fileName)
            if (file.exists()) {
                Glide.with(weakContext.get()!!)
                        .load(file)
                        .placeholder(GGGAppInterface.gggApp.circularProgressDrawable)
                        .apply(RequestOptions()
                                .override(Target.SIZE_ORIGINAL)
                                .format(DecodeFormat.PREFER_ARGB_8888))
                        .into(holder.ivComic)
            } else {
                Glide.with(weakContext.get()!!)
                        .load(imageURL)
                        .placeholder(GGGAppInterface.gggApp.circularProgressDrawable)
                        .apply(RequestOptions()
                                .override(Target.SIZE_ORIGINAL)
                                .format(DecodeFormat.PREFER_ARGB_8888))
                        .signature(ObjectKey(Constant.SIGNATURE_IMAGE_CACHE))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.ivComic)
            }
        }

        holder.ivComic.setOnClickListener {
            listener.onEvent(Constant.ACTION_CLICK_ON_IMAGE_COMIC_TO_SHOW_NAVIGATION, it, null)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivComic: ImageView = itemView.findViewById(R.id.ivComic)
    }
}