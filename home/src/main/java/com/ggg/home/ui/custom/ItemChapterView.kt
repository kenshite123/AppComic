package com.ggg.home.ui.custom

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.ggg.common.GGGAppInterface
import com.ggg.common.utils.OnEventControlListener
import com.ggg.home.R
import com.ggg.home.data.model.ChapterHadRead
import com.ggg.home.data.model.ComicModel
import com.ggg.home.utils.Constant
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.item_list_chapter.view.*
import org.jetbrains.anko.runOnUiThread
import timber.log.Timber
import java.lang.ref.WeakReference

class ItemChapterView : ConstraintLayout {
    lateinit var weakContext: WeakReference<Context>
    lateinit var listener: OnEventControlListener
    var chapterHadRead = ChapterHadRead()
    var comicId = -1L
    var position = 0
    var compositeDisposable: CompositeDisposable = CompositeDisposable()

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        weakContext = WeakReference(context)
        View.inflate(context, R.layout.item_list_chapter, this)
        addEvents()
    }

    fun setData(chapterHadRead: ChapterHadRead, comicId: Long, position: Int, listener: OnEventControlListener) {
        this.chapterHadRead = chapterHadRead
        this.comicId = comicId
        this.position = position
        this.listener = listener
        this.reloadViews()
    }

    private fun initObserve() {
        val d1 = GGGAppInterface.gggApp.bus()
                .toObservableDownloadImageDone()
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val comicId = it["comicId"]!!
                    val chapterId = it["chapterId"]!!
                    if (this.comicId == comicId) {
                        if (chapterHadRead.chapterModel != null) {
                            if (chapterHadRead.chapterModel!!.chapterId == chapterId) {
                                chapterHadRead.chapterModel!!.hadDownloaded = Constant.IS_DOWNLOADED
//                                weakContext.get()?.runOnUiThread {
                                    reloadViews()
//                                }
                            }
                        }
                    }
                }, { Timber.e(it) }, {  })
        compositeDisposable.add(d1)
    }

    private fun reloadViews() {
        if (chapterHadRead.chapterModel != null) {
            val chapterModel = chapterHadRead.chapterModel!!
            tvChapterName.text = chapterModel.chapterName
            tvUpdateDate.text = chapterModel.dateUpdate

            if (chapterHadRead.ccHadReadModel.isNullOrEmpty()) {
                tvChapterName.setTextColor(Color.parseColor("#161616"))
            } else {
                tvChapterName.setTextColor(Color.parseColor("#949494"))
            }

            if (chapterModel.hadDownloaded == Constant.IS_DOWNLOADED) {
                ivDownloaded.visibility = View.VISIBLE
            } else {
                ivDownloaded.visibility = View.GONE
            }
        }
    }

    private fun addEvents() {
        llChapters.setOnClickListener {
            listener.onEvent(Constant.ACTION_CLICK_ON_CHAPTER, it, position)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        compositeDisposable.clear()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        initObserve()
    }
}