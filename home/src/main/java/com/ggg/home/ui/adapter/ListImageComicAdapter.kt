package com.ggg.home.ui.adapter

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.signature.ObjectKey
import com.ggg.common.GGGAppInterface
import com.ggg.common.utils.OnEventControlListener
import com.ggg.home.R
import com.ggg.home.data.view.DataImageAndAdsView
import com.ggg.home.utils.Constant
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import kotlinx.android.synthetic.main.fragment_library.*
import java.io.File
import java.lang.ref.WeakReference

class ListImageComicAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    lateinit var weakContext: WeakReference<Context>
    lateinit var listener: OnEventControlListener
    lateinit var listDataImageAndAds: List<DataImageAndAdsView>
    lateinit var adRequest: AdRequest
    var isDownloaded: Boolean = false
    var comicId: Long = 0
    var chapterId: Long = 0

    private val TYPE_VERTICAL = 1
    private val TYPE_HORIZONTAL = 2

    constructor(context: Context, listener: OnEventControlListener, listDataImageAndAds: List<DataImageAndAdsView>,
                isDownloaded: Boolean, comicId: Long, chapterId: Long, adRequest: AdRequest) {
        this.weakContext = WeakReference(context)
        this.listener = listener
        this.listDataImageAndAds = listDataImageAndAds
        this.isDownloaded = isDownloaded
        this.comicId = comicId
        this.chapterId = chapterId
        this.adRequest = adRequest
    }

    fun notifyData(listDataImageAndAds: List<DataImageAndAdsView>,
                   isDownloaded: Boolean, comicId: Long, chapterId: Long) {
        this.listDataImageAndAds = listDataImageAndAds
        this.isDownloaded = isDownloaded
        this.comicId = comicId
        this.chapterId = chapterId
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_VERTICAL) {
            val view = LayoutInflater.from(weakContext.get()).inflate(R.layout.item_list_image_comic, parent, false)
            ViewHolderVertical(view)
        } else {
            val view = LayoutInflater.from(weakContext.get()).inflate(R.layout.item_list_image_comic_horizontal, parent, false)
            ViewHolderHorizontal(view)
        }
    }

    override fun getItemCount(): Int {
        return this.listDataImageAndAds.count()
    }

    override fun getItemViewType(position: Int): Int {
        return if (this.listDataImageAndAds[position].isShowVertical) TYPE_VERTICAL else TYPE_HORIZONTAL
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val dataImageAndAdsView = listDataImageAndAds[position]
        if (getItemViewType(position) == TYPE_VERTICAL) {
            val holder = viewHolder as ViewHolderVertical
            if (dataImageAndAdsView.isAds) {
                holder.ivComic.visibility = View.GONE
                holder.adView.visibility = View.VISIBLE
                holder.adView.loadAd(adRequest)
            } else {
                holder.ivComic.visibility = View.VISIBLE
                holder.adView.visibility = View.GONE

                val imageURL = dataImageAndAdsView.image
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
        } else {
            val holder = viewHolder as ViewHolderHorizontal
            holder.ivComic.visibility = View.VISIBLE
            holder.adView.visibility = View.INVISIBLE
            holder.v1.visibility = View.VISIBLE
            if (!dataImageAndAdsView.isAds) {
                holder.adView.visibility = View.GONE
                holder.v1.visibility = View.GONE
            }

            val imageURL = dataImageAndAdsView.image
            if (!isDownloaded) {
                Glide.with(weakContext.get()!!)
                        .asBitmap()
                        .load(imageURL)
                        .placeholder(GGGAppInterface.gggApp.circularProgressDrawable)
                        .apply(RequestOptions()
                                .override(Target.SIZE_ORIGINAL)
                                .format(DecodeFormat.PREFER_ARGB_8888))
                        .signature(ObjectKey(Constant.SIGNATURE_IMAGE_CACHE))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(object : SimpleTarget<Bitmap>() {
                            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                holder.ivComic.setImageBitmap(resource)
                                if (dataImageAndAdsView.isAds) {
                                    holder.adView.loadAd(adRequest)
                                    holder.adView.adListener = object : AdListener() {
                                        override fun onAdLoaded() {
                                            super.onAdLoaded()
                                            holder.v1.visibility = View.VISIBLE
                                            holder.adView.visibility = View.VISIBLE
                                        }
                                    }
                                }
                            }
                        })
            } else {
                val imgSplit = imageURL.split("/")
                val fileName = "${weakContext.get()!!.filesDir.absolutePath}/DownloadComic/${comicId}/${chapterId}/${imgSplit[imgSplit.count() - 1]}"
                val file = File(fileName)
                if (file.exists()) {
                    Glide.with(weakContext.get()!!)
                            .asBitmap()
                            .load(file)
                            .placeholder(GGGAppInterface.gggApp.circularProgressDrawable)
                            .apply(RequestOptions()
                                    .override(Target.SIZE_ORIGINAL)
                                    .format(DecodeFormat.PREFER_ARGB_8888))
                            .into(object : SimpleTarget<Bitmap>() {
                                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                    holder.ivComic.setImageBitmap(resource)
                                    if (dataImageAndAdsView.isAds) {
                                        holder.adView.loadAd(adRequest)
                                        holder.adView.adListener = object : AdListener() {
                                            override fun onAdLoaded() {
                                                super.onAdLoaded()
                                                holder.v1.visibility = View.VISIBLE
                                                holder.adView.visibility = View.VISIBLE
                                            }
                                        }
                                    }
                                }
                            })
                } else {
                    Glide.with(weakContext.get()!!)
                            .asBitmap()
                            .load(imageURL)
                            .placeholder(GGGAppInterface.gggApp.circularProgressDrawable)
                            .apply(RequestOptions()
                                    .override(Target.SIZE_ORIGINAL)
                                    .format(DecodeFormat.PREFER_ARGB_8888))
                            .signature(ObjectKey(Constant.SIGNATURE_IMAGE_CACHE))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(object : SimpleTarget<Bitmap>() {
                                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                    holder.ivComic.setImageBitmap(resource)
                                    if (dataImageAndAdsView.isAds) {
                                        holder.adView.loadAd(adRequest)
                                        holder.adView.adListener = object : AdListener() {
                                            override fun onAdLoaded() {
                                                super.onAdLoaded()
                                                holder.v1.visibility = View.VISIBLE
                                                holder.adView.visibility = View.VISIBLE
                                            }
                                        }
                                    }
                                }
                            })
                }
            }

            holder.ivComic.setOnClickListener {
                listener.onEvent(Constant.ACTION_CLICK_ON_IMAGE_COMIC_TO_SHOW_NAVIGATION, it, null)
            }
        }
    }

    class ViewHolderVertical(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivComic: ImageView = itemView.findViewById(R.id.ivComic)
        var adView: AdView = itemView.findViewById(R.id.adView)
    }

    class ViewHolderHorizontal(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivComic: ImageView = itemView.findViewById(R.id.ivComic)
        var adView: AdView = itemView.findViewById(R.id.adView)
        var v1: View = itemView.findViewById(R.id.v1)
    }
}