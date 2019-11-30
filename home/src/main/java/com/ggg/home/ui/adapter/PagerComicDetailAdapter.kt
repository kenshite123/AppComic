package com.ggg.home.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.ggg.common.utils.OnEventControlListener
import com.ggg.common.utils.StringUtil
import com.ggg.home.R
import com.ggg.home.data.model.ChapterHadRead
import com.ggg.home.data.model.ComicWithCategoryModel
import com.ggg.home.data.model.CommentModel
import java.lang.ref.WeakReference

class PagerComicDetailAdapter : PagerAdapter {
    lateinit var weakContext: WeakReference<Context>
    lateinit var listener: OnEventControlListener
    var comicWithCategoryModel: ComicWithCategoryModel? = null
    var listChapters: List<ChapterHadRead> = listOf()
    var listComments: List<CommentModel> = listOf()

    var listTitle: ArrayList<String> = arrayListOf(StringUtil.getString(R.string.TEXT_LIST_CHAPTERS),
            StringUtil.getString(R.string.TEXT_DESCRIPTION), StringUtil.getString(R.string.TEXT_LIST_COMMENTS))

    constructor(context: Context, listener: OnEventControlListener) {
        this.weakContext = WeakReference(context)
        this.listener = listener
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return listTitle[position]
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return listTitle.count()
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    fun notifyData(listComments: List<CommentModel>, isComment: Boolean) {
        this.listComments = listComments
        notifyDataSetChanged()
    }

    fun notifyData(listChapters: List<ChapterHadRead>) {
        this.listChapters = listChapters
        notifyDataSetChanged()
    }

    fun notifyData(comicWithCategoryModel: ComicWithCategoryModel) {
        this.comicWithCategoryModel = comicWithCategoryModel
        notifyDataSetChanged()
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View
        if (position == 0) {
            view = LayoutInflater.from(weakContext.get()).inflate(R.layout.item_tab_list_chapters, container, false)
            val rvListChapter: RecyclerView = view.findViewById(R.id.rvListChapter)
            val listChapterAdapter = ListChapterAdapter(weakContext.get()!!, listener, listChapters)
            rvListChapter.setHasFixedSize(false)
            rvListChapter.layoutManager = LinearLayoutManager(weakContext.get()!!, RecyclerView.VERTICAL, false)
            rvListChapter.adapter = listChapterAdapter
        } else if (position == 1) {
            view = LayoutInflater.from(weakContext.get()).inflate(R.layout.item_tab_description, container, false)
            val tvDescription: TextView = view.findViewById(R.id.tvDescription)
            tvDescription.text = comicWithCategoryModel?.comicModel?.content
        } else {
            view = LayoutInflater.from(weakContext.get()).inflate(R.layout.item_tab_list_comments, container, false)
            val rvListComments: RecyclerView = view.findViewById(R.id.rvListComments)
            val listCommentAdapter = ListCommentAdapter(weakContext.get()!!, listener, listComments, true)
            rvListComments.setHasFixedSize(false)
            rvListComments.layoutManager = LinearLayoutManager(weakContext.get()!!, RecyclerView.VERTICAL, false)
            rvListComments.adapter = listCommentAdapter
        }
        container.addView(view)
        return view

    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }
}