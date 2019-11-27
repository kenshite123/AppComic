package com.ggg.home.ui.comic_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ggg.common.GGGAppInterface
import com.ggg.common.utils.StringUtil
import com.ggg.common.vo.Status
import com.ggg.home.R
import com.ggg.home.data.model.*
import com.ggg.home.ui.adapter.ListCategoryComicDetailAdapter
import com.ggg.home.ui.adapter.PagerComicDetailAdapter
import com.ggg.home.ui.main.HomeBaseFragment
import com.ggg.home.utils.Constant
import com.ggg.home.utils.Utils
import kotlinx.android.synthetic.main.fragment_comic_detail.*
import org.jetbrains.anko.bundleOf
import timber.log.Timber

class ComicDetailFragment : HomeBaseFragment() {
    private lateinit var viewModel: ComicDetailViewModel
    var isFirstLoad = true
    lateinit var comicWithCategoryModel: ComicWithCategoryModel
    lateinit var listCategoryComicDetailAdapter: ListCategoryComicDetailAdapter
    lateinit var pagerComicDetailAdapter: PagerComicDetailAdapter
    lateinit var listChapters: List<ChapterHadRead>
    var currentPagePosition = 0

    companion object {
        val TAG = "ComicDetailFragment"
        @JvmStatic
        fun create(comicWithCategoryModel: ComicWithCategoryModel) : ComicDetailFragment {
            val bundle = bundleOf(
                    "comicWithCategoryModel" to comicWithCategoryModel
            )
            val comicDetailFragment = ComicDetailFragment()
            comicDetailFragment.arguments = bundle
            return comicDetailFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comic_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.d("onActivityCreated")
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ComicDetailViewModel::class.java)
        showActionBar()
        hideBottomNavView()

        comicWithCategoryModel = arguments?.get("comicWithCategoryModel") as ComicWithCategoryModel
        initViews()
        initEvent()
        loadData()
    }

    private fun initViews() {
        val comic = comicWithCategoryModel.comicModel!!
        setTitleActionBar(comic.title)

        Glide.with(context!!)
                .load(comic.imageUrl)
                .placeholder(GGGAppInterface.gggApp.circularProgressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(ivComic)

        if (!comic.authorsString.isNullOrEmpty()) {
            tvAuthor.text = comic.authorsString!!
        } else {
            tvAuthor.text = StringUtil.getString(R.string.TEXT_STATUS_UNCOMPLETED)
        }

        if (comic.status == Constant.STATUS_COMPLETED) {
            tvStatus.text = StringUtil.getString(R.string.TEXT_STATUS_COMPLETED)
        } else {
            tvStatus.text = StringUtil.getString(R.string.TEXT_STATUS_UNCOMPLETED)
        }

        tvViews.text = Utils.formatNumber(comic.viewed)

        listCategoryComicDetailAdapter = ListCategoryComicDetailAdapter(context!!, this, comicWithCategoryModel.categories!!)
        rvListCategory.setHasFixedSize(true)
        rvListCategory.layoutManager = GridLayoutManager(context!!, 3)
        rvListCategory.adapter = listCategoryComicDetailAdapter

        pagerComicDetailAdapter = PagerComicDetailAdapter(context!!, this)
        viewPager.adapter = pagerComicDetailAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun initEvent() {
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                currentPagePosition = position
                when (position) {
                    0 -> { // list chapters
                        pagerComicDetailAdapter.notifyData(listChapters)
                    }

                    1 -> { // description
                        pagerComicDetailAdapter.notifyData(comicWithCategoryModel)
                    }

                    2 -> { // list comments
                    }
                }
            }
        })
    }

    private fun loadData() {
        viewModel.getListChapters(comicWithCategoryModel.comicModel!!.id)
    }

    private fun initObserve() {
        viewModel.getListChaptersResult.observe(this, Observer {
            loading(it)
            if (it.status == Status.SUCCESS || it.status == Status.ERROR) {
                it.data?.let {
                    this.listChapters = it
                    if (currentPagePosition == 0) {
                        pagerComicDetailAdapter.notifyData(this.listChapters)
                    }
                }
            }
        })
    }

    override fun onEvent(eventAction: Int, control: View?, data: Any?) {
        when (eventAction) {
            Constant.ACTION_CLICK_ON_CHAPTER -> {
                val positionChapter = data as Int
                insertCCHadRead(positionChapter)
                navigationController.showViewComic(comicWithCategoryModel, listChapters, positionChapter)
            }

            Constant.ACTION_CLICK_ON_ITEM_CATEGORY_OF_COMIC_DETAIL -> {
                val categoryOfComicModel = data as CategoryOfComicModel
                navigationController.showCategoryDetail(categoryOfComicModel)
            }

            else -> super.onEvent(eventAction, control, data)
        }
    }

    private fun insertCCHadRead(positionChapter: Int) {
        val ccHadReadModel = CCHadReadModel()
        ccHadReadModel.comicId = comicWithCategoryModel.comicModel!!.id
        ccHadReadModel.chapterId = listChapters[positionChapter].chapterModel!!.chapterId
        ccHadReadModel.lastModified = System.currentTimeMillis()
        viewModel.insertCCHadRead(ccHadReadModel)
    }

    override fun onResume() {
        super.onResume()
        if (isFirstLoad) {
            initObserve()
            isFirstLoad = false
        }
    }
}