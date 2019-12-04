package com.ggg.home.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.ggg.common.utils.OnEventControlListener
import com.ggg.common.utils.StringUtil
import com.ggg.home.R
import com.ggg.home.data.model.ComicRankWithCategoryModel
import java.lang.ref.WeakReference

class PagerRankingAdapter : PagerAdapter {
    lateinit var weakContext: WeakReference<Context>
    lateinit var listener: OnEventControlListener
    lateinit var listComic: List<ComicRankWithCategoryModel>

    constructor(context: Context, listener: OnEventControlListener, listComic: List<ComicRankWithCategoryModel>) {
        this.weakContext = WeakReference(context)
        this.listener = listener
        this.listComic = listComic
    }

    fun notifyData(listComic: List<ComicRankWithCategoryModel>) {
        this.listComic = listComic
        notifyDataSetChanged()
    }

    var listTitle: ArrayList<String> = arrayListOf(StringUtil.getString(R.string.TEXT_RANK_DAY),
            StringUtil.getString(R.string.TEXT_RANK_WEEK), StringUtil.getString(R.string.TEXT_RANK_MONTH)
            , StringUtil.getString(R.string.TEXT_RANK_ALL))

    override fun getPageTitle(position: Int): CharSequence? {
        return listTitle[position]
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    override fun getCount(): Int {
        return listTitle.count()
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(weakContext.get()).inflate(R.layout.item_page_rank, container, false)
        val listComicRankingAdapter = ListComicRankingAdapter(weakContext.get()!!, listener, this.listComic)
        val rvListComic: RecyclerView = view.findViewById(R.id.rvListComic)
        rvListComic.setHasFixedSize(false)
        rvListComic.layoutManager = LinearLayoutManager(weakContext.get()!!, RecyclerView.VERTICAL, false)
        rvListComic.adapter = listComicRankingAdapter

        container.addView(view)
        return view

    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }
}