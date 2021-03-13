package com.ggg.home.ui.library

import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.androidnetworking.AndroidNetworking
import com.ggg.common.GGGAppInterface
import com.ggg.common.vo.Status
import com.ggg.home.R
import com.ggg.home.data.model.ComicModel
import com.ggg.home.data.model.ComicWithCategoryModel
import com.ggg.home.data.model.HistoryModel
import com.ggg.home.data.model.response.LoginResponse
import com.ggg.home.ui.adapter.PagerLibraryAdapter
import com.ggg.home.ui.main.HomeBaseFragment
import com.ggg.home.utils.Constant
import com.google.android.gms.ads.*
import kotlinx.android.synthetic.main.fragment_library.*
import org.jetbrains.anko.doAsync
import timber.log.Timber
import java.io.File

class LibraryFragment : HomeBaseFragment() {
    private lateinit var viewModel: LibraryViewModel
    lateinit var pagerLibraryAdapter: PagerLibraryAdapter
    private var isFirstLoad = true
    private var listHistoryModel: List<HistoryModel> = listOf()
    private var listComicFollow: List<ComicModel> = listOf()
    private var listComicDownloadedModel: List<ComicModel> = listOf()
    private var currentPagePosition = 0
    private var items = 90
    private var page = 0
    private var pageHistory = 0
    private var pageFollow = 0
    private var pageDownloaded = 0
    private var isLoadAllDataHistory = false
    private var isLoadMoreHistory = false
    private var isLoadAllDataFollow = false
    private var isLoadMoreFollow = false
    private var isLoadAllDataDownloaded = false
    private var isLoadMoreDownloaded = false

    companion object {
        val TAG = "LibraryFragment"
        @JvmStatic
        fun create() = LibraryFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_library, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.d("onActivityCreated")
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LibraryViewModel::class.java)
        hideActionBar()
        showBottomNavView()

        initViews()
        initEvent()
    }

    private fun initViews() {
        pagerLibraryAdapter = PagerLibraryAdapter(context!!, this)
        viewPager.adapter = pagerLibraryAdapter
        tabLayout.setupWithViewPager(viewPager)

        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        adView.adListener = object : AdListener() {
            override fun onAdImpression() {
                super.onAdImpression()
            }

            override fun onAdClicked() {
                super.onAdClicked()
            }

            override fun onAdFailedToLoad(p0: LoadAdError?) {
                super.onAdFailedToLoad(p0)
            }

            override fun onAdClosed() {
                super.onAdClosed()
            }

            override fun onAdOpened() {
                super.onAdOpened()
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                adView.visibility = View.VISIBLE
            }
        }
    }

    override fun initObserver() {
        viewModel.getListHistoryResult.observe(this, Observer {
            if (currentPagePosition == 0) {
                loading(it)
            }
            it.data?.let {
                if (isLoadMoreHistory) {
                    val list = this.listHistoryModel.toMutableList()
                    list.addAll(it)
                    this.listHistoryModel = list.toList()

                    isLoadMoreHistory = false
                } else {
                    this.listHistoryModel = it
                }
                isLoadAllDataHistory = it.count() < items

                if (currentPagePosition == 0) {
                    pagerLibraryAdapter.notifyDataListHistory(this.listHistoryModel)
                }
            }
        })

        viewModel.getListComicFollowResult.observe(this, Observer {
            if (currentPagePosition == 1) {
                loading(it)
            }
            it.data?.let {
                if (isLoadMoreFollow) {
                    val list = this.listComicFollow.toMutableList()
                    list.addAll(it)
                    this.listComicFollow = list.toList()

                    isLoadMoreFollow = false
                } else {
                    this.listComicFollow = it
                }
                isLoadAllDataFollow = it.count() < items

                if (currentPagePosition == 1) {
                    pagerLibraryAdapter.notifyDataListFollow(listComicFollow, false)
                }
            }
        })

        viewModel.getListComicDownloadedResult.observe(this, Observer {
            if (currentPagePosition == 2) {
                loading(it)
            }

            it.data?.let {
                if (isLoadMoreDownloaded) {
                    val list = this.listComicDownloadedModel.toMutableList()
                    list.addAll(it)
                    this.listComicDownloadedModel = list.toList()

                    isLoadMoreDownloaded = false
                } else {
                    this.listComicDownloadedModel = it
                }
                isLoadAllDataDownloaded = it.count() < items

                if (currentPagePosition == 2) {
                    pagerLibraryAdapter.notifyDataListComicDownloaded(this.listComicDownloadedModel)
                }
            }
        })

        viewModel.unFavoriteComicResult.observe(this, Observer {
            loading(it)
            if (it.status == Status.SUCCESS) {
                val listSelected = this.listComicFollow.filter { it.isSelected }
                listSelected.forEach {
                    GGGAppInterface.gggApp.removeComicToFavorite(it.id)
                }
                val list = this.listComicFollow.filter { !it.isSelected }
                this.listComicFollow = list.toList()
                pagerLibraryAdapter.notifyDataListFollow(listComicFollow = listComicFollow, isEdit = false)
                resetAction()
            } else {
                it.message?.let {
                    showDialog(it)
                }
            }
        })
    }

    override fun initEvent() {
        tvEditAndCancel.setOnClickListener {
            if (tvEditAndCancel.text == getString(R.string.TEXT_EDIT)) {
                tvEditAndCancel.text = getString(R.string.TEXT_CANCEL)
                v1.visibility = View.VISIBLE
                llAction.visibility = View.VISIBLE
                tvSelectAndUnSelectAll.text = getString(R.string.TEXT_SELECT_ALL)
                when (currentPagePosition) {
                    0 -> {
                        // history
                        pagerLibraryAdapter.notifyDataListHistory(listHistoryModel = listHistoryModel, isEdit = true)
                    }

                    1 -> {
                        // follow
                        pagerLibraryAdapter.notifyDataListFollow(listComicFollow = listComicFollow, isEdit = true)
                    }

                    2 -> {
                        // downloaded
                        pagerLibraryAdapter.notifyDataListComicDownloaded(listComicDownloaded = listComicDownloadedModel, isEdit = true)
                    }
                }
            } else {
                tvEditAndCancel.text = getString(R.string.TEXT_EDIT)
                v1.visibility = View.GONE
                llAction.visibility = View.GONE
                tvSelectAndUnSelectAll.text = getString(R.string.TEXT_SELECT_ALL)
                when (currentPagePosition) {
                    0 -> {
                        // history
                        pagerLibraryAdapter.notifyDataListHistory(listHistoryModel = listHistoryModel, isEdit = false)
                    }

                    1 -> {
                        // follow
                        pagerLibraryAdapter.notifyDataListFollow(listComicFollow = listComicFollow, isEdit = false)
                    }

                    2 -> {
                        // downloaded
                        pagerLibraryAdapter.notifyDataListComicDownloaded(listComicDownloaded = listComicDownloadedModel, isEdit = false)
                    }
                }
            }
        }

        tvSelectAndUnSelectAll.setOnClickListener {
            if (tvSelectAndUnSelectAll.text == getString(R.string.TEXT_SELECT_ALL)) {
                tvSelectAndUnSelectAll.text = getString(R.string.TEXT_DESELECT_ALL)
                when (currentPagePosition) {
                    0 -> {
                        // history
                        this.listHistoryModel.forEach {
                            it.comicModel?.isSelected = true
                        }
                        pagerLibraryAdapter.notifyDataListHistory(listHistoryModel = this.listHistoryModel, isEdit = true)
                    }

                    1 -> {
                        // follow
                        this.listComicFollow.forEach {
                            it.isSelected = true
                        }
                        pagerLibraryAdapter.notifyDataListFollow(listComicFollow = this.listComicFollow, isEdit = true)
                    }

                     2 -> {
                         // downloaded
                         this.listComicDownloadedModel.forEach {
                             it.isSelected = true
                         }
                         pagerLibraryAdapter.notifyDataListComicDownloaded(listComicDownloaded = this.listComicDownloadedModel, isEdit = true)
                     }
                }
            } else {
                tvSelectAndUnSelectAll.text = getString(R.string.TEXT_SELECT_ALL)
                when (currentPagePosition) {
                    0 -> {
                        // history
                        this.listHistoryModel.forEach {
                            it.comicModel?.isSelected = false
                        }
                        pagerLibraryAdapter.notifyDataListHistory(listHistoryModel = this.listHistoryModel, isEdit = true)
                    }

                    1 -> {
                        // follow
                        this.listComicFollow.forEach {
                            it.isSelected = false
                        }
                        pagerLibraryAdapter.notifyDataListFollow(listComicFollow = this.listComicFollow, isEdit = true)
                    }

                    2 -> {
                        // downloaded
                        this.listComicDownloadedModel.forEach {
                            it.isSelected = false
                        }
                        pagerLibraryAdapter.notifyDataListComicDownloaded(listComicDownloaded = this.listComicDownloadedModel, isEdit = true)
                    }
                }
            }
        }

        llDelete.setOnClickListener {
            when (currentPagePosition) {
                0 -> {
                    // history
                    val listSelected = listHistoryModel.filter { it.comicModel?.isSelected == true }
                    if (!listSelected.isNullOrEmpty()) {
                        val listComicId = mutableListOf<Long>()
                        listSelected.forEach {
                            listComicId.add(it.comicModel!!.id)
                        }
                        viewModel.deleteListHistory(listComicId)
                        val list = listHistoryModel.filter { it.comicModel?.isSelected == false }
                        this.listHistoryModel = list.toList()
                        pagerLibraryAdapter.notifyDataListHistory(listHistoryModel = listHistoryModel, isEdit = false)
                        resetAction()
                    }
                }

                1 -> {
                    // follow
                    if (GGGAppInterface.gggApp.checkIsLogin()) {
                        val listSelected = this.listComicFollow.filter { it.isSelected }
                        if (!listSelected.isNullOrEmpty()) {
                            val listComicId = mutableListOf<String>()
                            listSelected.forEach {
                                listComicId.add(it.id.toString())
                            }
                            val loginResponse = GGGAppInterface.gggApp.loginResponse as LoginResponse
                            val token = "${loginResponse.tokenType}${loginResponse.accessToken}"
                            val data = hashMapOf(
                                    "token" to token,
                                    "listComicId" to TextUtils.join(",", listComicId)
                            )

                            viewModel.unFavoriteComic(data)
                        }
                    } else {
                        showConfirmDialog(R.string.TEXT_ERROR_NO_LOGIN_TO_UNFOLLOW_COMIC,
                                R.string.TEXT_CANCEL, DialogInterface.OnClickListener { dialogInterface, _ -> dialogInterface.dismiss() },
                                R.string.TEXT_REGISTER, DialogInterface.OnClickListener { dialogInterface, _ -> run {
                            dialogInterface.dismiss()
                            navigationController.showRegister()
                        }},
                                R.string.TEXT_LOGIN, DialogInterface.OnClickListener { dialogInterface, _ -> run {
                            dialogInterface.dismiss()
                            navigationController.showLogin()
                        }})
                    }
//                        pagerLibraryAdapter.notifyDataListFollow(listComicFollow = listComicFollow, isEdit = false)
                }

                2 -> {
                    // downloaded
                    val listSelected = listComicDownloadedModel.filter { it.isSelected }
                    if (!listSelected.isNullOrEmpty()) {
                        val listComicId = mutableListOf<Long>()
                        listSelected.forEach {
                            doAsync {
                                AndroidNetworking.forceCancel(it.id.toString())
                                listComicId.add(it.id)
                                val file = File("${context!!.filesDir.absolutePath}/DownloadComic/${it.id}")
                                if (file.exists()) {
                                    file.deleteRecursively()
                                }
                            }
                        }
                        viewModel.deleteListDownloaded(listComicId)
                    }
                    val list = this.listComicDownloadedModel.filter { !it.isSelected }
                    this.listComicDownloadedModel = list.toList()
                    pagerLibraryAdapter.notifyDataListComicDownloaded(listComicDownloaded = listComicDownloadedModel, isEdit = false)
                    resetAction()
                }
            }
        }

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                currentPagePosition = position
                resetAction()
                when (position) {
                    0 -> {
                        pagerLibraryAdapter.notifyDataListHistory(listHistoryModel, false)
                    }

                    1 -> {
                        pagerLibraryAdapter.notifyDataListFollow(listComicFollow, false)
                    }

                    else -> {
                        pagerLibraryAdapter.notifyDataListComicDownloaded(listComicDownloadedModel, false)
                    }
                }
            }
        })
    }

    private fun resetAction() {
        tvEditAndCancel.text = getString(R.string.TEXT_EDIT)
        v1.visibility = View.GONE
        llAction.visibility = View.GONE
        tvSelectAndUnSelectAll.text = getString(R.string.TEXT_SELECT_ALL)
        this.listHistoryModel.forEach {
            it.comicModel?.isSelected = false
        }

        this.listComicFollow.forEach {
            it.isSelected = false
        }

        this.listComicDownloadedModel.forEach {
            it.isSelected = false
        }
    }

    private fun loadDataHistory() {
        val data = hashMapOf(
                "limit" to items,
                "offset" to pageHistory
        )
        viewModel.getListHistory(data)
    }

    private fun loadDataListComicFollow() {
        var loginResponse: LoginResponse? = null
        if (GGGAppInterface.gggApp.checkIsLogin()) {
            loginResponse = GGGAppInterface.gggApp.loginResponse as LoginResponse
        }
        val token = loginResponse?.tokenType + loginResponse?.accessToken
        val data = hashMapOf<String, Any>(
                "token" to token,
                "limit" to items,
                "offset" to pageFollow
        )
        viewModel.getListComicFollow(data)
    }

    private fun loadDataListComicDownloaded() {
        val data = hashMapOf(
                "limit" to items,
                "offset" to pageDownloaded
        )
        viewModel.getListComicDownloaded(data)
    }

    override fun onEvent(eventAction: Int, control: View?, data: Any?) {
        when(eventAction) {
            Constant.ACTION_CLICK_ON_COMIC_HISTORY -> {
                val historyModel = data as HistoryModel
                navigationController.showComicDetail(historyModel.comicModel!!.id.toString())
            }

            Constant.ACTION_CLICK_ON_COMIC_WITH_CATEGORY_MODEL -> {
                val comicWithCategoryModel = data as ComicWithCategoryModel
                navigationController.showComicDetail(comicWithCategoryModel)
            }

            Constant.ACTION_CLICK_ON_COMIC_DOWNLOADED -> {
                val comicId = data as Long
                navigationController.showComicDetail(comicId = comicId.toString())
            }

            Constant.ACTION_LOAD_MORE_LIST_COMIC_HISTORY -> {
                if (!isLoadAllDataHistory && !isLoadMoreHistory) {
                    isLoadMoreHistory = true
                    pageHistory++
                    loadDataHistory()
                }
            }

            Constant.ACTION_LOAD_MORE_LIST_COMIC_DOWNLOADED -> {
                if (!isLoadAllDataDownloaded && !isLoadMoreDownloaded) {
                    isLoadMoreDownloaded = true
                    pageDownloaded++
                    loadDataListComicDownloaded()
                }
            }

            Constant.ACTION_LOAD_MORE_LIST_COMIC_FOLLOW -> {
                if (!isLoadAllDataFollow && !isLoadMoreFollow) {
                    isLoadMoreFollow = true
                    pageFollow++
                    loadDataListComicFollow()
                }
            }

            Constant.ACTION_CLICK_ON_COMIC -> {
                val comicId = data as Long
                navigationController.showComicDetail(comicId.toString())
            }

            Constant.ACTION_SELECT_OR_DESELECT_COMIC_TO_EDIT -> {
//                val position = data as Int
                when (currentPagePosition) {
                    0 -> {
                        // history
                        val count = this.listHistoryModel.count { it.comicModel?.isSelected == true }
                        if (count > 0) {
                            tvSelectAndUnSelectAll.text = getString(R.string.TEXT_DESELECT_ALL)
                        } else {
                            tvSelectAndUnSelectAll.text = getString(R.string.TEXT_SELECT_ALL)
                        }
                    }

                    1 -> {
                        // follow
                        val count = this.listComicFollow.count { it.isSelected }
                        if (count > 0) {
                            tvSelectAndUnSelectAll.text = getString(R.string.TEXT_DESELECT_ALL)
                        } else {
                            tvSelectAndUnSelectAll.text = getString(R.string.TEXT_SELECT_ALL)
                        }
                    }

                    2 -> {
                        // downloaded
                        val count = this.listComicDownloadedModel.count { it.isSelected }
                        if (count > 0) {
                            tvSelectAndUnSelectAll.text = getString(R.string.TEXT_DESELECT_ALL)
                        } else {
                            tvSelectAndUnSelectAll.text = getString(R.string.TEXT_SELECT_ALL)
                        }
                    }
                }
            }

            else -> super.onEvent(eventAction, control, data)
        }
    }

    override fun onResume() {
        super.onResume()
        if (isFirstLoad) {
            initObserver()
            isFirstLoad = false
        }

        loadDataHistory()
        loadDataListComicFollow()
        loadDataListComicDownloaded()
    }
}