package com.ggg.home.ui.rank

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.ggg.common.vo.Status
import com.ggg.home.R
import com.ggg.home.data.model.ComicRankWithCategoryModel
import com.ggg.home.ui.adapter.PagerRankingAdapter
import com.ggg.home.ui.main.HomeBaseFragment
import com.ggg.home.utils.Constant
import kotlinx.android.synthetic.main.fragment_rank.*
import timber.log.Timber

class RankFragment : HomeBaseFragment() {
    private lateinit var viewModel: RankViewModel
    var isFirstLoad = true
    lateinit var pagerRankingAdapter: PagerRankingAdapter
    lateinit var listComic: List<ComicRankWithCategoryModel>
    var currentPosition = 0

    companion object {
        val TAG = "RankFragment"
        @JvmStatic
        fun create() : RankFragment {
            val fragment = RankFragment()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rank, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.d("onActivityCreated")
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(RankViewModel::class.java)
        showActionBar()
        hideBottomNavView()
        setTitleActionBar(R.string.TEXT_RANK)

        initViews()
        initEvent()
        loadData()
    }

    private fun initViews() {
        pagerRankingAdapter = PagerRankingAdapter(context!!, this, listOf())
        viewPager.adapter = pagerRankingAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun initObserver() {
        viewModel.getListRankComicResult.observe(this, Observer {
            loading(it)
            if (it.status == Status.SUCCESS_DB && it.data.isNullOrEmpty()) {
                showLoading()
            }

            it.data?.let {
                this.listComic = it
                pagerRankingAdapter.notifyData(this.listComic)
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
                currentPosition = position
                loadData()
            }
        })
    }

    private fun loadData() {
        val type: String = when (currentPosition) {
            0 -> Constant.RANK_TYPE_DAY
            1 -> Constant.RANK_TYPE_WEEK
            2 -> Constant.RANK_TYPE_MONTH
            else -> Constant.RANK_TYPE_ALL
        }

        val data = hashMapOf(
                "type" to type,
                "limit" to 20,
                "offset" to 0
        )

        viewModel.getListRankComic(data)
    }

    override fun onResume() {
        super.onResume()
        if (isFirstLoad) {
            initObserver()
            isFirstLoad = false
        }
    }

    override fun onEvent(eventAction: Int, control: View?, data: Any?) {
        when (eventAction) {
            else -> super.onEvent(eventAction, control, data)
        }
    }
}