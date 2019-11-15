package com.ggg.home.ui.home

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ggg.home.R
import com.ggg.home.ui.adapter.PagerSlideAdapter
import com.ggg.home.ui.main.HomeBaseFragment
import kotlinx.android.synthetic.main.fragment_home.*
import timber.log.Timber
import java.util.*



class HomeFragment : HomeBaseFragment() {

    private lateinit var viewModel: HomeViewModel
    lateinit var pagerSlideAdapter: PagerSlideAdapter
    lateinit var listImage: ArrayList<String>
    lateinit var timer: Timer
    var currentPage = 0

    companion object {
        val TAG = "HomeFragment"
        private val DELAY_MS: Long = 500
        private val PERIOD_MS: Long = 3000
        @JvmStatic
        fun create() = HomeFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.d("onActivityCreated")
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)
        navigationController.setTitle("Home")

        initViews()
        initObserver()
        initEvent()
    }

    private fun initViews() {
        listImage = ArrayList()
        listImage.add("http://ww5.heavenmanga.org/content/upload/images/images/one-piece-banner.png")
        listImage.add("http://ww5.heavenmanga.org/content/upload/images/images/brawling-go-banner.jpg")
        listImage.add("http://ww5.heavenmanga.org/content/upload/images/images/ruler-of-the-land-banner.jpg")
        listImage.add("http://ww5.heavenmanga.org/content/upload/images/images/magi-the-labyrinth-of-magic-banner.jpg")

        pagerSlideAdapter = PagerSlideAdapter(context!!, listImage)
        rvSlide.setHasFixedSize(false)
        val layoutManager = LinearLayoutManager(context!!, RecyclerView.HORIZONTAL, false)
        layoutManager.stackFromEnd = true
        rvSlide.layoutManager = layoutManager
        rvSlide.adapter = pagerSlideAdapter
        val pagerSnapHelper = PagerSnapHelper()
        pagerSnapHelper.attachToRecyclerView(rvSlide)
        indicator.attachToRecyclerView(rvSlide, pagerSnapHelper)

        initTimerToSlide()
    }

    private fun initTimerToSlide() {
        val handler = Handler()
        val runnable = Runnable {
            if (currentPage == listImage.count()) {
                currentPage = 0
            }

            rvSlide.scrollToPosition(currentPage++)
        }
        timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(runnable)
            }
        }, DELAY_MS, PERIOD_MS)
    }

    override fun initObserver() {

    }

    override fun initEvent() {
    }
}
