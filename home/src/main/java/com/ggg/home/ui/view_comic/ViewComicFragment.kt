package com.ggg.home.ui.view_comic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.ggg.common.GGGAppInterface
import com.ggg.common.vo.Status
import com.ggg.home.R
import com.ggg.home.data.model.CCHadReadModel
import com.ggg.home.data.model.ChapterHadRead
import com.ggg.home.data.model.ComicWithCategoryModel
import com.ggg.home.data.model.post_param.DataSendReportParam
import com.ggg.home.data.model.response.LoginResponse
import com.ggg.home.data.view.DataImageAndAdsView
import com.ggg.home.ui.adapter.ListImageComicAdapter
import com.ggg.home.ui.custom.ReportComicView
import com.ggg.home.ui.main.HomeBaseFragment
import com.ggg.home.utils.Constant
import com.google.android.gms.ads.AdRequest
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_view_comic.*
import org.jetbrains.anko.bundleOf
import timber.log.Timber
import java.util.concurrent.TimeUnit

class ViewComicFragment : HomeBaseFragment() {
    private lateinit var viewModel: ViewComicViewModel
    var isFirstLoad = true
    lateinit var comicWithCategoryModel: ComicWithCategoryModel
    lateinit var listChapterModel: List<ChapterHadRead>
    var positionChapter: Int = 0
    lateinit var listImageComicAdapter: ListImageComicAdapter
    var isShowNavigation = true
    var isShowVertical = true
    var isLoadLatest = true
    var currentPagePosition = 0

    lateinit var pagerSnapHelper: PagerSnapHelper
    lateinit var layoutManagerForVertical: LinearLayoutManager
    lateinit var layoutManagerForHorizontal: LinearLayoutManager
    lateinit var chapterSelected: ChapterHadRead
    lateinit var reportComicView: ReportComicView
    var loginResponse: LoginResponse? = null
    var token: String? = null
    private var listImageUrl = mutableListOf<String>()

    companion object {
        val TAG = "ViewComicFragment"
        @JvmStatic
        fun create(comicWithCategoryModel: ComicWithCategoryModel,
                   listChapterModel: List<ChapterHadRead>, positionChapter: Int, isLoadLatest: Boolean) : ViewComicFragment {
            val fragment = ViewComicFragment()
            val bundle = bundleOf(
                    "comicWithCategoryModel" to comicWithCategoryModel,
                    "listChapterModel" to listChapterModel,
                    "positionChapter" to positionChapter,
                    "isLoadLatest" to isLoadLatest
            )
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_comic, container, false)
        reportComicView = view.findViewById(R.id.reportComicView)
        reportComicView.listener = this
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.d("onActivityCreated")
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ViewComicViewModel::class.java)
        hideActionBar()
        hideBottomNavView()

        loginResponse = GGGAppInterface.gggApp.loginResponse as LoginResponse?
        token = if (loginResponse?.tokenType.isNullOrEmpty() || loginResponse?.accessToken.isNullOrEmpty()) {
            null
        } else {
            loginResponse?.tokenType + loginResponse?.accessToken
        }

        comicWithCategoryModel = arguments!!["comicWithCategoryModel"] as ComicWithCategoryModel
        listChapterModel = arguments!!["listChapterModel"] as List<ChapterHadRead>
        positionChapter = arguments!!["positionChapter"] as Int
        isLoadLatest = arguments!!["isLoadLatest"] as Boolean

        initViews()
        initEvent()
        loadData()
    }

    private fun initViews() {
        pagerSnapHelper = PagerSnapHelper()
        layoutManagerForVertical = LinearLayoutManager(context!!, RecyclerView.VERTICAL, false)
        layoutManagerForHorizontal = LinearLayoutManager(context!!, RecyclerView.HORIZONTAL, false)

        val adRequest = AdRequest.Builder().build()
        listImageComicAdapter = ListImageComicAdapter(context!!, this, listOf(),
                false, 0, 0, adRequest)
        rvListImageComic.setHasFixedSize(false)
        rvListImageComic.layoutManager = layoutManagerForVertical
        rvListImageComic.adapter = listImageComicAdapter

        ctlHeader.bringToFront()
        llFooter.bringToFront()
    }

    override fun initObserver() {
        val disposable = Observable.interval(10, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    isShowNavigation = true
                    showHideNavigation()
                }, {

                }, {

                })
        messageEvent.add(disposable)

        viewModel.getListImageResult.observe(this, Observer {
            loading(it)
            if (it.status == Status.SUCCESS || it.status == Status.SUCCESS_DB || it.status == Status.ERROR) {
                val listImageComic: List<String> = chapterSelected.chapterModel!!.listImageUrlString.split(", ")
                listImageUrl = listImageComic.toMutableList()
                notifyDataListImageComic()
                rvListImageComic.scrollToPosition(currentPagePosition)

                if (it.status == Status.ERROR) {
                    it.message?.let {
                        showDialog(it)
                    }
                }
            }
        })

        viewModel.sendReportResult.observe(this, Observer {  })
    }

    override fun initEvent() {
        ivBack.setOnClickListener {
            navigationController.popToBackStack()
        }

        ivDownload.setOnClickListener {
            navigationController.showChooseChapToDownloadImage(comicId = comicWithCategoryModel.comicModel!!.id)
        }

        ivReport.setOnClickListener {
            showReportComicView()
        }

        ivNext.setOnClickListener {
            if (isLoadLatest) {
                if (positionChapter == 0) {
                    showMsg(R.string.TEXT_FINAL_CHAPTERS_OF_COMIC_TOAST)
                } else {
                    insertCCHadRead(positionChapter)
                    positionChapter--
                    currentPagePosition = 0
                    loadData()
                }
            } else {
                if (positionChapter == listChapterModel.size - 1) {
                    showMsg(R.string.TEXT_FINAL_CHAPTERS_OF_COMIC_TOAST)
                } else {
                    insertCCHadRead(positionChapter)
                    positionChapter++
                    currentPagePosition = 0
                    loadData()
                }
            }

        }

        ivPrevious.setOnClickListener {
            if (isLoadLatest) {
                if (positionChapter == listChapterModel.size - 1) {
                    showMsg(R.string.TEXT_FIRST_CHAPTERS_OF_COMIC_TOAST)
                } else {
                    insertCCHadRead(positionChapter)
                    positionChapter++
                    currentPagePosition = 0
                    loadData()
                }
            } else {
                if (positionChapter == 0) {
                    showMsg(R.string.TEXT_FIRST_CHAPTERS_OF_COMIC_TOAST)
                } else {
                    insertCCHadRead(positionChapter)
                    positionChapter--
                    currentPagePosition = 0
                    loadData()
                }
            }
        }

        ivChangeScrollDirection.setOnClickListener {
            if (isShowVertical) {
                ivChangeScrollDirection.setImageResource(R.drawable.icon_read_vertical)
                rvListImageComic.layoutManager = layoutManagerForHorizontal
                pagerSnapHelper.attachToRecyclerView(rvListImageComic)
//                rvListImageComic.scrollToPosition(currentPagePosition)
                isShowVertical = false
            } else {
                ivChangeScrollDirection.setImageResource(R.drawable.icon_read_horizontal)
                rvListImageComic.layoutManager = layoutManagerForVertical
                pagerSnapHelper.attachToRecyclerView(null)
//                rvListImageComic.scrollToPosition(currentPagePosition)
                isShowVertical = true
            }

            notifyDataListImageComic()
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

        rvListImageComic.setOnClickListener {
            showHideNavigation()
        }

        ctlImageComic.setOnClickListener {
            showHideNavigation()
        }
    }

    private fun loadData() {
        chapterSelected = listChapterModel[positionChapter]
        viewModel.getListImageOfChapter(chapterSelected)
        tvChapter.text = chapterSelected.chapterModel!!.chapterName
    }

    override fun onEvent(eventAction: Int, control: View?, data: Any?) {
        when (eventAction) {
            Constant.ACTION_CLICK_ON_IMAGE_COMIC_TO_SHOW_NAVIGATION -> {
                showHideNavigation()
            }

            Constant.ACTION_HIDE_REPORT_COMIC_VIEW -> {
                hideReportComicView()
            }

            Constant.ACTION_SEND_REPORT -> {
                val content = data.toString()
                hideReportComicView()
                val dataSendReportParam = DataSendReportParam()
                dataSendReportParam.comicId = comicWithCategoryModel.comicModel!!.id
                dataSendReportParam.chapterId = chapterSelected.chapterModel!!.chapterId
                dataSendReportParam.content = content
                val data = hashMapOf<String, Any?>(
                        "token" to token,
                        "dataSendReportParam" to dataSendReportParam
                )
                viewModel.sendReport(data)
                showToastRelease(getString(R.string.TEXT_THANK_FOR_YOUR_REPORT))
            }

            else -> super.onEvent(eventAction, control, data)
        }
    }

    private fun showHideNavigation() {
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

    private fun showReportComicView() {
        isShowNavigation = true
        showHideNavigation()
        reportComicView.resetView()
        reportComicView.visibility = View.VISIBLE
    }

    private fun hideReportComicView() {
        reportComicView.visibility = View.GONE
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

    private fun notifyDataListImageComic() {
        val listImageComic = listImageUrl.toList()
        val listDataImageAndAds = mutableListOf<DataImageAndAdsView>()
        for (i in 0 until listImageComic.size) {
            if (isShowVertical) {
                val dataImageView = DataImageAndAdsView()
                dataImageView.image = listImageComic[i]
                dataImageView.isAds = false
                dataImageView.isShowVertical = isShowVertical
                listDataImageAndAds.add(dataImageView)

                if (i == 2 ||
                        (i == listImageComic.size / 2 && listImageComic.size >= 20) ||
                        (i == listImageComic.size - 3 && listImageComic.size >= 8)) {
                    val dataAdsView = DataImageAndAdsView()
                    dataAdsView.isAds = true
                    dataAdsView.isShowVertical = isShowVertical
                    listDataImageAndAds.add(dataAdsView)
                }
            } else {
                val dataImageAndAdsView = DataImageAndAdsView()
                dataImageAndAdsView.isShowVertical = isShowVertical
                dataImageAndAdsView.isAds = i == 2 ||
                        (i == listImageComic.size / 2 && listImageComic.size >= 20) ||
                        (i == listImageComic.size - 3 && listImageComic.size >= 8)
                dataImageAndAdsView.image = listImageComic[i]
                listDataImageAndAds.add(dataImageAndAdsView)
            }
        }

        if (chapterSelected.chapterModel!!.hadDownloaded == Constant.IS_DOWNLOADED) {
            listImageComicAdapter.notifyData(listDataImageAndAds = listDataImageAndAds, isDownloaded = true,
                    comicId = comicWithCategoryModel.comicModel!!.id, chapterId = chapterSelected.chapterModel!!.chapterId)
        } else {
            listImageComicAdapter.notifyData(listDataImageAndAds = listDataImageAndAds,
                    isDownloaded = false, comicId = 0, chapterId = 0)
        }
    }
}