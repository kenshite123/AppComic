package com.ggg.home.ui.choose_chap_to_download_image

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.ggg.common.vo.Status
import com.ggg.home.R
import com.ggg.home.data.model.ChapterModel
import com.ggg.home.ui.adapter.ListChapterDownloadImageAdapter
import com.ggg.home.ui.main.HomeBaseFragment
import kotlinx.android.synthetic.main.fragment_choose_chap_to_download_image.*
import org.jetbrains.anko.bundleOf
import timber.log.Timber

class ChooseChapToDownloadImageFragment : HomeBaseFragment() {
    private lateinit var viewModel: ChooseChapToDownloadImageViewModel
    var isFirstLoad = true
    var comicId: Long = 0
    var isLoadLatest = true
    var countChapterSelected = 0
    var totalChapter = 0
    var listChapters = listOf<ChapterModel>()

    private lateinit var listChapterDownloadImageAdapter: ListChapterDownloadImageAdapter

    companion object{
        val TAG = "ChooseChapToDownloadImageFragment"
        @JvmStatic
        fun create(comicId: Long) : ChooseChapToDownloadImageFragment {
            val chooseChapToDownloadImageFragment = ChooseChapToDownloadImageFragment()
            val bundle = bundleOf(
                    "comicId" to comicId
            )
            chooseChapToDownloadImageFragment.arguments = bundle
            return chooseChapToDownloadImageFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_choose_chap_to_download_image, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.d("onActivityCreated")
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ChooseChapToDownloadImageViewModel::class.java)

        comicId = arguments!!["comicId"] as Long

        initViews()
        initEvent()
        loadData()
    }

    private fun initViews() {
        showActionBar()
        hideBottomNavView()
        setTitleActionBar(R.string.TEXT_CHOOSE_CHAP_TO_DOWNLOAD)

        tvTotalChap.text = getString(R.string.TEXT_TOTAL_CHAP, totalChapter.toString())
        tvQuantitySelected.text = getString(R.string.TEXT_QUANTITY_CHAP_CHOSEN, countChapterSelected.toString())

        listChapterDownloadImageAdapter = ListChapterDownloadImageAdapter(context!!, this, listChapters)
        rvListChapter.setHasFixedSize(false)
        rvListChapter.layoutManager = GridLayoutManager(context!!, 3)
        rvListChapter.adapter = listChapterDownloadImageAdapter
    }

    override fun initEvent() {
        tvLatest.setOnClickListener {
            if (!isLoadLatest) {
                isLoadLatest = true
                tvLatest.setTextColor(Color.parseColor("#d75c3b"))
                tvOldest.setTextColor(Color.parseColor("#969696"))
                this.listChapters = this.listChapters.reversed()
                listChapterDownloadImageAdapter.notifyData(listChapters)
            }
        }

        tvOldest.setOnClickListener {
            if (isLoadLatest) {
                isLoadLatest = false
                tvLatest.setTextColor(Color.parseColor("#969696"))
                tvOldest.setTextColor(Color.parseColor("#d75c3b"))
                this.listChapters = this.listChapters.reversed()
                listChapterDownloadImageAdapter.notifyData(listChapters)
            }
        }

        llSelectAll.setOnClickListener {

        }

        llDownload.setOnClickListener {

        }
    }

    override fun initObserver() {
        viewModel.getListChaptersResult.observe(this, Observer {
            loading(it)
            if (it.status == Status.SUCCESS) {
                it.data?.let {
                    listChapters = if (!it.isNullOrEmpty()) {
                        it
                    } else {
                        mutableListOf()
                    }
                    listChapterDownloadImageAdapter.notifyData(listChapters)
                }
            } else if (it.status == Status.ERROR) {
                it.message?.let {
                    showDialog(it)
                }
            }
        })
    }

    private fun loadData() {
        viewModel.getListChapters(comicId)
    }

    override fun onResume() {
        super.onResume()
        if (isFirstLoad) {
            initObserver()
            isFirstLoad = false
        }
    }
}