package com.ggg.home.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ggg.common.GGGAppInterface
import com.ggg.common.utils.OnEventControlListener
import com.ggg.common.utils.StringUtil
import com.ggg.home.R
import com.ggg.home.data.model.ComicDownloadedModel
import com.ggg.home.data.model.ComicModel
import com.ggg.home.data.model.ComicWithCategoryModel
import com.ggg.home.data.model.HistoryModel
import com.ggg.home.utils.Constant
import kotlinx.android.synthetic.main.fragment_category.*
import java.lang.ref.WeakReference

class PagerLibraryAdapter : PagerAdapter {

    lateinit var weakContext: WeakReference<Context>
    lateinit var listener: OnEventControlListener
    private var listHistoryModel: List<HistoryModel> = listOf()
    private var listComicFollow: List<ComicModel> = listOf()
    private var listComicDownloaded: List<ComicModel> = listOf()

    var listTitle: ArrayList<String> = arrayListOf(StringUtil.getString(R.string.TEXT_HISTORY),
            StringUtil.getString(R.string.TEXT_FOLLOW), StringUtil.getString(R.string.TEXT_DOWNLOADED))

    constructor(context: Context, listener: OnEventControlListener) {
        this.weakContext = WeakReference(context)
        this.listener = listener
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return listTitle[position]
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return listTitle.count()
    }

    fun notifyData(listHistoryModel: List<HistoryModel>) {
        this.listHistoryModel = listHistoryModel
        notifyDataSetChanged()
    }

    fun notifyData(listComicFollow: List<ComicModel>, isFollow: Boolean) {
        this.listComicFollow = listComicFollow
        notifyDataSetChanged()
    }

    fun notifyDataListComicDownloaded(listComicDownloaded: List<ComicModel>) {
        this.listComicDownloaded = listComicDownloaded
        notifyDataSetChanged()
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View
        when (position) {
            0 -> {
                view = LayoutInflater.from(weakContext.get()).inflate(R.layout.item_tab_history, container, false)
                val gridLayoutManager = GridLayoutManager(weakContext.get()!!, 3)
                val rvListComic: RecyclerView = view.findViewById(R.id.rvListComic)
                val listComicHistoryAdapter = ListComicHistoryAdapter(weakContext.get()!!, listener, listHistoryModel)
                rvListComic.setHasFixedSize(false)
                rvListComic.layoutManager = gridLayoutManager
                rvListComic.adapter = listComicHistoryAdapter

                rvListComic.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        if (dy > 0) {
                            val visibleItemCount = 3
                            val totalItemCount = gridLayoutManager.itemCount
                            val pastVisibleItems = gridLayoutManager.findLastCompletelyVisibleItemPosition()

                            if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                                listener.onEvent(Constant.ACTION_LOAD_MORE_LIST_COMIC_HISTORY, null, null)
                            }
                        }
                    }

                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                    }
                })
            }
            1 -> {
                view = LayoutInflater.from(weakContext.get()).inflate(R.layout.item_tab_follow, container, false)
                val rvListComic: RecyclerView = view.findViewById(R.id.rvListComic)
                val listComicAdapter = ListComicAdapter(weakContext.get()!!, listener, listComicFollow, true)
                val gridLayoutManager = GridLayoutManager(weakContext.get()!!, 3)

                rvListComic.setHasFixedSize(false)
                rvListComic.layoutManager = gridLayoutManager
                rvListComic.adapter = listComicAdapter

                rvListComic.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        if (dy > 0) {
                            val visibleItemCount = 3
                            val totalItemCount = gridLayoutManager.itemCount
                            val pastVisibleItems = gridLayoutManager.findLastCompletelyVisibleItemPosition()

                            if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                                listener.onEvent(Constant.ACTION_LOAD_MORE_LIST_COMIC_FOLLOW, null, null)
                            }
                        }
                    }

                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                    }
                })
            }
            else -> {
                view = LayoutInflater.from(weakContext.get()).inflate(R.layout.item_tab_downloaded, container, false)
                val gridLayoutManager = GridLayoutManager(weakContext.get()!!, 3)
                val rvListComic: RecyclerView = view.findViewById(R.id.rvListComic)
                val listComicDownloadedAdapter = ListComicDownloadedAdapter(weakContext.get()!!, listener, listComicDownloaded)
                rvListComic.setHasFixedSize(false)
                rvListComic.itemAnimator?.changeDuration = 0L
                rvListComic.layoutManager = gridLayoutManager
                rvListComic.adapter = listComicDownloadedAdapter

                rvListComic.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        if (dy > 0) {
                            val visibleItemCount = 3
                            val totalItemCount = gridLayoutManager.itemCount
                            val pastVisibleItems = gridLayoutManager.findLastCompletelyVisibleItemPosition()

                            if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                                listener.onEvent(Constant.ACTION_LOAD_MORE_LIST_COMIC_DOWNLOADED, null, null)
                            }
                        }
                    }

                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                    }
                })
            }
        }
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }
}