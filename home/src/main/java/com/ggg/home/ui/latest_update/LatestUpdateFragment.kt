package com.ggg.home.ui.latest_update

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.ggg.common.vo.Status
import com.ggg.home.R
import com.ggg.home.data.model.ComicWithCategoryModel
import com.ggg.home.ui.adapter.ListComicAdapter
import com.ggg.home.ui.main.HomeBaseFragment
import com.ggg.home.utils.Constant
import kotlinx.android.synthetic.main.fragment_latest_update.*
import timber.log.Timber

class LatestUpdateFragment : HomeBaseFragment() {
    private lateinit var viewModel: LatestUpdateViewModel
    var isFirstLoad = true
    lateinit var listComicAdapter: ListComicAdapter
    lateinit var listComicLatestUpdate: List<ComicWithCategoryModel>
    var items: Int = 30
    var page: Int = 0

    companion object {
        val TAG = "LatestUpdateFragment"
        @JvmStatic
        fun create() = LatestUpdateFragment()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_latest_update, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.d("onActivityCreated")
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LatestUpdateViewModel::class.java)
        showActionBar()
        hideBottomNavView()
        setTitleActionBar(R.string.TEXT_COMIC_LATEST_UPDATE)

        initViews()
        initEvent()
        loadData()
    }

    private fun initViews() {
        listComicAdapter = ListComicAdapter(context!!, this, listOf())
        rvListComic.setHasFixedSize(false)
        rvListComic.layoutManager = GridLayoutManager(context!!, 3)
        rvListComic.adapter = listComicAdapter
    }

    override fun initObserver() {
        viewModel.getListLatestUpdateResult.observe(this, androidx.lifecycle.Observer {
            loading(it)
            if (it.status == Status.SUCCESS || it.status == Status.SUCCESS_DB || it.status == Status.ERROR) {
                if (it.status == Status.SUCCESS_DB && it.data.isNullOrEmpty()) {
                    showLoading()
                }

                it.data?.let {
                    this.listComicLatestUpdate = it.distinctBy { it.comicModel?.id }
                    listComicAdapter.notifyData(this.listComicLatestUpdate)
                }
            }
        })
    }

    override fun initEvent() {

    }

    private fun loadData() {
        val dataLatestUpdate = hashMapOf(
                "limit" to items,
                "offset" to page
        )
        viewModel.getListLatestUpdate(dataLatestUpdate)
    }

    override fun onEvent(eventAction: Int, control: View?, data: Any?) {
        when (eventAction) {
            Constant.ACTION_CLICK_ON_COMIC -> {
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
    }
}