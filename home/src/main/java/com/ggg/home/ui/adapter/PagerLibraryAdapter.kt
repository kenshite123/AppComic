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
import com.ggg.home.data.model.ComicWithCategoryModel
import com.ggg.home.data.model.HistoryModel
import java.lang.ref.WeakReference

class PagerLibraryAdapter : PagerAdapter {

    lateinit var weakContext: WeakReference<Context>
    lateinit var listener: OnEventControlListener
    var listHistoryModel: List<HistoryModel> = listOf()
    var listComicFollow: List<ComicWithCategoryModel> = listOf()

    var listTitle: ArrayList<String> = arrayListOf(StringUtil.getString(R.string.TEXT_HISTORY),
            StringUtil.getString(R.string.TEXT_FOLLOW))

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

    fun notifyData(listComicFollow: List<ComicWithCategoryModel>, isFollow: Boolean) {
        this.listComicFollow = listComicFollow
        notifyDataSetChanged()
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View
        if (position == 0) {
            view = LayoutInflater.from(weakContext.get()).inflate(R.layout.item_tab_history, container, false)
            val rvListComic: RecyclerView = view.findViewById(R.id.rvListComic)
            val listComicHistoryAdapter = ListComicHistoryAdapter(weakContext.get()!!, listener, listHistoryModel)
            rvListComic.setHasFixedSize(false)
            rvListComic.layoutManager = GridLayoutManager(weakContext.get()!!, 3)
            rvListComic.adapter = listComicHistoryAdapter
        } else {
            view = LayoutInflater.from(weakContext.get()).inflate(R.layout.item_tab_follow, container, false)
            val rvListComic: RecyclerView = view.findViewById(R.id.rvListComic)
            val listComicAdapter = ListComicAdapter(weakContext.get()!!, listener, listComicFollow)
            val gridLayoutManager = GridLayoutManager(weakContext.get()!!, 3)

            rvListComic.setHasFixedSize(false)
            rvListComic.layoutManager = gridLayoutManager
            rvListComic.adapter = listComicAdapter
        }
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }
}