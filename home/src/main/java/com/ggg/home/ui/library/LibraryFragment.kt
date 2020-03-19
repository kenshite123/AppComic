package com.ggg.home.ui.library

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.ggg.common.GGGAppInterface
import com.ggg.home.R
import com.ggg.home.data.model.ComicWithCategoryModel
import com.ggg.home.data.model.HistoryModel
import com.ggg.home.data.model.response.LoginResponse
import com.ggg.home.ui.adapter.PagerLibraryAdapter
import com.ggg.home.ui.main.HomeBaseFragment
import com.ggg.home.utils.Constant
import kotlinx.android.synthetic.main.fragment_library.*
import timber.log.Timber

class LibraryFragment : HomeBaseFragment() {
    private lateinit var viewModel: LibraryViewModel
    lateinit var pagerLibraryAdapter: PagerLibraryAdapter
    var isFirstLoad = true
    var listHistoryModel: List<HistoryModel> = listOf()
    var listComicFollow: List<ComicWithCategoryModel> = listOf()
    var currentPagePosition = 0
    var items = 60
    var page = 0

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
    }

    override fun initObserver() {
        viewModel.getListHistoryResult.observe(this, Observer {
            if (currentPagePosition == 0) {
                loading(it)
            }
            it.data?.let {
                this.listHistoryModel = it
                if (currentPagePosition == 0) {
                    pagerLibraryAdapter.notifyData(this.listHistoryModel)
                }
            }
        })

        viewModel.getListComicFollowResult.observe(this, Observer {
            if (currentPagePosition == 1) {
                loading(it)
            }
            it.data?.let {
                this.listComicFollow = it
                if (currentPagePosition == 1) {
                    pagerLibraryAdapter.notifyData(listComicFollow, true)
                }
            }
        })
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
                    0 -> {
                        pagerLibraryAdapter.notifyData(listHistoryModel)
                    }

                    else -> {
                        pagerLibraryAdapter.notifyData(listComicFollow, true)
                    }
                }
            }
        })
    }

    private fun loadDataHistory() {
        val data = hashMapOf(
                "limit" to items,
                "offset" to page
        )
        viewModel.getListHistory(data)
    }

    private fun loadDataListComicFollow() {
        var loginResponse: LoginResponse? = null
        if (GGGAppInterface.gggApp.checkIsLogin()) {
            loginResponse = GGGAppInterface.gggApp.loginResponse as LoginResponse
        }
        val token = loginResponse?.tokenType + loginResponse?.accessToken
        val data = hashMapOf(
                "token" to token,
                "listComicId" to GGGAppInterface.gggApp.listFavoriteId,
                "limit" to 900,
                "offset" to 0
        )
        viewModel.getListComicFollow(data)
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
    }
}