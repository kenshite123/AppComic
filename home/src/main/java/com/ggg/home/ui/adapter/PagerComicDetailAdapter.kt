package com.ggg.home.ui.adapter

import android.content.Context
import android.graphics.Color
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
import com.ggg.home.utils.Constant
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.lang.ref.WeakReference

class PagerComicDetailAdapter : PagerAdapter {
    lateinit var weakContext: WeakReference<Context>
    lateinit var listener: OnEventControlListener
    var comicWithCategoryModel: ComicWithCategoryModel? = null
    var listChapters: List<ChapterHadRead> = listOf()
    var listComments: List<CommentModel> = listOf()
    var isLoadMoreComment: Boolean = true
    var isLoadLatest: Boolean = true
    var positionNotifyListChap = 0
    var isNotifyItemChangeListChap = false

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

    fun notifyData(listComments: List<CommentModel>, isLoadMoreComment: Boolean) {
        this.listComments = listComments
        this.isLoadMoreComment = isLoadMoreComment
        notifyDataSetChanged()
    }

    fun notifyData(listChapters: List<ChapterHadRead>, isLoadLatest: Boolean, isLoadOldest: Boolean, comicWithCategoryModel: ComicWithCategoryModel?) {
        this.listChapters = listChapters
        this.isLoadLatest = isLoadLatest
        this.comicWithCategoryModel = comicWithCategoryModel
        notifyDataSetChanged()
    }

    fun notifyData(comicWithCategoryModel: ComicWithCategoryModel) {
        this.comicWithCategoryModel = comicWithCategoryModel
        notifyDataSetChanged()
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View
        when (position) {
            0 -> {
                view = LayoutInflater.from(weakContext.get()).inflate(R.layout.item_tab_list_chapters, container, false)
                val rvListChapter: RecyclerView = view.findViewById(R.id.rvListChapter)
                val tvLatest: TextView = view.findViewById(R.id.tvLatest)
                val tvOldest: TextView = view.findViewById(R.id.tvOldest)

                if (isLoadLatest) {
                    tvLatest.setTextColor(Color.parseColor("#d75c3b"))
                    tvOldest.setTextColor(Color.parseColor("#969696"))
                } else {
                    tvLatest.setTextColor(Color.parseColor("#969696"))
                    tvOldest.setTextColor(Color.parseColor("#d75c3b"))
                }

                val listChapterAdapter = ListChapterAdapter(weakContext.get()!!, listener, listChapters, comicWithCategoryModel)
                rvListChapter.setHasFixedSize(false)
                rvListChapter.layoutManager = LinearLayoutManager(weakContext.get()!!, RecyclerView.VERTICAL, false)
                rvListChapter.adapter = listChapterAdapter

                tvLatest.setOnClickListener {
                    listener.onEvent(Constant.ACTION_CLICK_ON_BUTTON_LOAD_LATEST_CHAPTER, null, null)
                }

                tvOldest.setOnClickListener {
                    listener.onEvent(Constant.ACTION_CLICK_ON_BUTTON_LOAD_OLDEST_CHAPTER, null, null)
                }

            }
            1 -> {
                view = LayoutInflater.from(weakContext.get()).inflate(R.layout.item_tab_description, container, false)
                val tvDescription: TextView = view.findViewById(R.id.tvDescription)
                tvDescription.text = comicWithCategoryModel?.comicModel?.content
            }
            else -> {
                view = LayoutInflater.from(weakContext.get()).inflate(R.layout.item_tab_list_comments, container, false)
                val rvListComments: RecyclerView = view.findViewById(R.id.rvListComments)
                val fabComment: FloatingActionButton = view.findViewById(R.id.fabComment)
                fabComment.bringToFront()
                fabComment.setOnClickListener {
                    listener.onEvent(Constant.ACTION_CLICK_ON_BUTTON_COMMENT_IN_COMIC_DETAIL, it, null)
                }

                val llManager = LinearLayoutManager(weakContext.get()!!, RecyclerView.VERTICAL, false)
                val listCommentAdapter = ListCommentAdapter(weakContext.get()!!, listener, listComments, true)
                rvListComments.setHasFixedSize(false)
                rvListComments.layoutManager = llManager
                rvListComments.adapter = listCommentAdapter

                rvListComments.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        if (isLoadMoreComment) {
                            if (dy > 0) {
                                val visibleItemCount = 1
                                val totalItemCount = llManager.itemCount
                                val pastVisibleItems = llManager.findLastCompletelyVisibleItemPosition()

                                if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                                    listener.onEvent(Constant.ACTION_LOAD_MORE_COMMENT_OF_COMIC_DETAIL, null, null)
                                }
                            }
                        }
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