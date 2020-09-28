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
import com.ggg.home.data.model.ComicModel
import com.ggg.home.ui.custom.ItemComicDownloadView
import com.ggg.home.utils.Constant
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
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
        val view = LayoutInflater.from(weakContext.get()).inflate(R.layout.item_bound_downloaded_comic, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return this.listComic.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comic = listComic[position]
        holder.itemComicDownloadView.setData(comicModel = comic, listener = listener)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemComicDownloadView: ItemComicDownloadView = itemView.findViewById(R.id.itemComicDownloadView)
    }
}