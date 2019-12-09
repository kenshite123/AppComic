package com.ggg.home.ui.view_comic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.ggg.home.R
import com.ggg.home.data.model.CCHadReadModel
import com.ggg.home.data.model.ChapterHadRead
import com.ggg.home.data.model.ComicWithCategoryModel
import com.ggg.home.ui.adapter.ListImageComicAdapter
import com.ggg.home.ui.main.HomeBaseFragment
import com.ggg.home.utils.Constant
import kotlinx.android.synthetic.main.fragment_view_comic.*
import org.jetbrains.anko.bundleOf
import timber.log.Timber

class ViewComicFragment : HomeBaseFragment() {
    private lateinit var viewModel: ViewComicViewModel
    var isFirstLoad = true
    lateinit var comicWithCategoryModel: ComicWithCategoryModel
    lateinit var listChapterModel: List<ChapterHadRead>
    var positionChapter: Int = 0
    lateinit var listImageComicAdapter: ListImageComicAdapter
    var isShowNavigation = true
    var isShowVertical = true
    var currentPagePosition = 0

    lateinit var pagerSnapHelper: PagerSnapHelper
    lateinit var layoutManagerForVertical: LinearLayoutManager
    lateinit var layoutManagerForHorizontal: LinearLayoutManager

    companion object {
        val TAG = "ViewComicFragment"
        @JvmStatic
        fun create(comicWithCategoryModel: ComicWithCategoryModel,
                   listChapterModel: List<ChapterHadRead>, positionChapter: Int) : ViewComicFragment {
            val fragment = ViewComicFragment()
            val bundle = bundleOf(
                    "comicWithCategoryModel" to comicWithCategoryModel,
                    "listChapterModel" to listChapterModel,
                    "positionChapter" to positionChapter
            )
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_comic, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.d("onActivityCreated")
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ViewComicViewModel::class.java)
        isFirstLoad = true
        hideActionBar()
        hideBottomNavView()

        comicWithCategoryModel = arguments!!["comicWithCategoryModel"] as ComicWithCategoryModel
        listChapterModel = arguments!!["listChapterModel"] as List<ChapterHadRead>
        positionChapter = arguments!!["positionChapter"] as Int

        initViews()
        initEvent()
        loadData()
    }

    private fun initViews() {
        pagerSnapHelper = PagerSnapHelper()
        layoutManagerForVertical = LinearLayoutManager(context!!, RecyclerView.VERTICAL, false)
        layoutManagerForHorizontal = LinearLayoutManager(context!!, RecyclerView.HORIZONTAL, false)

        listImageComicAdapter = ListImageComicAdapter(context!!, this, listOf())
        rvListImageComic.setHasFixedSize(false)
        rvListImageComic.layoutManager = layoutManagerForVertical
        rvListImageComic.adapter = listImageComicAdapter

        ctlHeader.bringToFront()
        llFooter.bringToFront()
    }

    override fun initObserver() {
    }

    override fun initEvent() {
        ivBack.setOnClickListener {
            navigationController.popToBackStack()
        }

        ivNext.setOnClickListener {
            if (positionChapter == 0) {
                showMsg(R.string.TEXT_FINAL_CHAPTERS_OF_COMIC_TOAST)
            } else {
                insertCCHadRead(positionChapter)
                positionChapter--
                currentPagePosition = 0
                loadData()
            }
        }

        ivPrevious.setOnClickListener {
            if (positionChapter == listChapterModel.size - 1) {
                showMsg(R.string.TEXT_FIRST_CHAPTERS_OF_COMIC_TOAST)
            } else {
                insertCCHadRead(positionChapter)
                positionChapter++
                currentPagePosition = 0
                loadData()
            }
        }

        ivChangeScrollDirection.setOnClickListener {
            if (isShowVertical) {
                ivChangeScrollDirection.setImageResource(R.drawable.icon_read_vertical)
                rvListImageComic.layoutManager = layoutManagerForHorizontal
                pagerSnapHelper.attachToRecyclerView(rvListImageComic)
                rvListImageComic.scrollToPosition(currentPagePosition)
                isShowVertical = false
            } else {
                ivChangeScrollDirection.setImageResource(R.drawable.icon_read_horizontal)
                rvListImageComic.layoutManager = layoutManagerForVertical
                pagerSnapHelper.attachToRecyclerView(null)
                rvListImageComic.scrollToPosition(currentPagePosition)
                isShowVertical = true
            }
        }

        ivComment.setOnClickListener {
            val chapterModel = listChapterModel[positionChapter].chapterModel!!
            navigationController.showCommentOfChap(comicWithCategoryModel, chapterModel)
        }

        rvListImageComic.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (isShowVertical) {
                    currentPagePosition = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!isShowVertical) {
                    val layoutManager = recyclerView.layoutManager
                    val view = pagerSnapHelper.findSnapView(layoutManager!!)
                    currentPagePosition = layoutManager.getPosition(view!!)
                }
            }
        })
    }

    private fun loadData() {
        val chapterSelected = listChapterModel[positionChapter]
        val listImageComic: List<String> = chapterSelected.chapterModel!!.listImageUrlString.split(", ")
        listImageComicAdapter.notifyData(listImageComic)
        rvListImageComic.scrollToPosition(currentPagePosition)
        tvChapter.text = chapterSelected.chapterModel!!.chapterName
    }

    override fun onEvent(eventAction: Int, control: View?, data: Any?) {
        when (eventAction) {
            Constant.ACTION_CLICK_ON_IMAGE_COMIC_TO_SHOW_NAVIGATION -> {
                if (isShowNavigation) {
                    ctlHeader.visibility = View.GONE
                    llFooter.visibility = View.GONE
                    isShowNavigation = false
                } else {
                    ctlHeader.visibility = View.VISIBLE
                    llFooter.visibility = View.VISIBLE
                    isShowNavigation = true
                }
            }

            else -> super.onEvent(eventAction, control, data)
        }
    }

    override fun onDetach() {
        super.onDetach()
        Timber.d("onDetach")
        // code here
        insertCCHadRead(positionChapter)
    }

    private fun insertCCHadRead(positionChapter: Int) {
        val ccHadReadModel = CCHadReadModel()
        ccHadReadModel.comicId = comicWithCategoryModel.comicModel!!.id
        ccHadReadModel.chapterId = listChapterModel[positionChapter].chapterModel!!.chapterId
        ccHadReadModel.positionOfPage = currentPagePosition
        ccHadReadModel.lastModified = System.currentTimeMillis()
        viewModel.insertCCHadRead(ccHadReadModel)
    }

    override fun onResume() {
        super.onResume()
        if (isFirstLoad) {
            initObserver()
            isFirstLoad = false
        }
    }
}