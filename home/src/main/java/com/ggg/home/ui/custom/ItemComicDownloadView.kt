package com.ggg.home.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ggg.common.GGGAppInterface
import com.ggg.common.utils.OnEventControlListener
import com.ggg.home.R
import com.ggg.home.data.model.ComicModel
import com.ggg.home.utils.Constant
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.item_comic_library.view.*
import org.jetbrains.anko.runOnUiThread
import timber.log.Timber
import java.lang.ref.WeakReference

class ItemComicDownloadView : ConstraintLayout {
    lateinit var weakContext: WeakReference<Context>
    lateinit var listener: OnEventControlListener
    var comic: ComicModel = ComicModel()
    var compositeDisposable: CompositeDisposable = CompositeDisposable()
    private var isEdit = false
    private var position = -1

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        weakContext = WeakReference(context)
        View.inflate(context, R.layout.item_comic_library, this)
        initObserve()
        addEvents()
    }

    fun setData(comicModel: ComicModel, listener: OnEventControlListener, position: Int = -1, isEdit: Boolean = false) {
        this.comic = comicModel
        this.listener = listener
        this.position = position
        this.isEdit = isEdit
        this.reloadViews()
    }

    private fun reloadViews() {
        Glide.with(weakContext.get()!!)
                .load(comic.imageUrl)
                .placeholder(GGGAppInterface.gggApp.circularProgressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivComic)

        tvComicTitle.text = comic.title
        tvChap.text = comic.latestChapter
        if (isEdit) {
            if (comic.isSelected) {
                ivChecked.visibility = View.VISIBLE
            } else {
                ivChecked.visibility = View.GONE
            }
        } else {
            ivChecked.visibility = View.GONE
        }
        this.reloadProgress()
    }

    private fun reloadProgress() {
        if (comic.totalNeedToDownload == 0) {
            ctlProgressDownload.visibility = View.GONE
        } else {
            val percent = comic.totalDownloaded * 100 / comic.totalNeedToDownload
            if (percent == 100) {
                ctlProgressDownload.visibility = View.GONE
            } else {
                ctlProgressDownload.visibility = View.VISIBLE
                tvPercent.text = "${percent}%"
                circularProgressBar.apply {
                    progressMax = 100F
                    progress = percent.toFloat()
                }
            }
        }
    }

    private fun initObserve() {
//        val d = GGGAppInterface.gggApp.bus().toObservableDownloadImageSuccess().subscribe({
//            if (comic.id == it) {
//                comic.totalDownloaded++
//                Timber.d("totalDownloaded: ${comic.totalDownloaded} - totalNeedToDownload: ${comic.totalNeedToDownload} - percent: ${comic.totalDownloaded * 100 / comic.totalNeedToDownload}")
//                this.reloadProgress()
//            }
//        }, { Timber.e(it) }, {  })
//        compositeDisposable.add(d)

        val d1 = GGGAppInterface.gggApp.bus()
                .toObservableDownloadImageDone()
                .subscribe({
                    val comicId = it["comicId"]!!
                    if (comic.id == comicId) {
                        comic.totalDownloaded = 0
                        comic.totalNeedToDownload = 0
                        weakContext.get()?.runOnUiThread {
                            reloadProgress()
                        }
                    }
        }, { Timber.e(it) }, {  })
        compositeDisposable.add(d1)
    }

    private fun addEvents() {
        ivComic.setOnClickListener {
            if (this.isEdit) {
                comic.isSelected = !comic.isSelected
                if (comic.isSelected) {
                    ivChecked.visibility = View.VISIBLE
//                    ivChecked.setImageResource(R.drawable.icon_checked)
                } else {
                    ivChecked.visibility = View.GONE
                }

                listener.onEvent(Constant.ACTION_SELECT_OR_DESELECT_COMIC_TO_EDIT, null, position)
            } else {
                if (comic.totalNeedToDownload == comic.totalDownloaded) {
                    listener.onEvent(Constant.ACTION_CLICK_ON_COMIC_DOWNLOADED, it, comic.id)
                }
            }
        }

        ctlProgressDownload.setOnClickListener {
            if (this.isEdit) {
                comic.isSelected = !comic.isSelected
                if (comic.isSelected) {
                    if (comic.isSelected) {
                        ivChecked.visibility = View.VISIBLE
//                    ivChecked.setImageResource(R.drawable.icon_checked)
                    } else {
                        ivChecked.visibility = View.GONE
                    }
                } else {
//                    ivChecked.setImageResource(R.drawable.icon_uncheck)
                    ivChecked.visibility = View.GONE
                }
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        compositeDisposable.clear()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        initObserve()
        reloadViews()
    }
}