package com.ggg.home.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.ggg.common.vo.Status
import com.ggg.home.R
import com.ggg.home.data.model.ComicModel
import com.ggg.home.data.model.ComicWithCategoryModel
import com.ggg.home.ui.adapter.ListComicAdapter
import com.ggg.home.ui.category.CategoryViewModel
import com.ggg.home.ui.main.HomeBaseFragment
import com.ggg.home.utils.Constant
import kotlinx.android.synthetic.main.fragment_search.*
import timber.log.Timber

class SearchFragment : HomeBaseFragment() {
    private lateinit var viewModel: CategoryViewModel
    var page: Long = 0
    var items: Long = 60
    var isFirstLoad = true
    var isLoadMore = true
    lateinit var listComicAdapter: ListComicAdapter
    var listComicByKeyWords: List<ComicModel> = arrayListOf()

    companion object {
        val TAG = "SearchFragment"
        @JvmStatic
        fun create() = SearchFragment()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.d("onActivityCreated")
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CategoryViewModel::class.java)
        isFirstLoad = true
        showActionBar()
        hideBottomNavView()
        setTitleActionBar(R.string.TEXT_SEARCH)

        initViews()
        initEvent()
    }

    private fun initViews() {
        listComicAdapter = ListComicAdapter(context!!, this, this.listComicByKeyWords, true)
        rvListComicSearch.setHasFixedSize(true)
        rvListComicSearch.layoutManager = GridLayoutManager(context!!, 3)
        rvListComicSearch.adapter = listComicAdapter

    }

    override fun onEvent(eventAction: Int, control: View?, data: Any?) {
        when (eventAction) {
            Constant.ACTION_CLICK_ON_COMIC -> {
                val comicId = data as Long
                navigationController.showComicDetail(comicId)
            }
            else -> {

            }
        }
    }

    override fun initObserver() {
        viewModel.getListComicByKeyWordsResult.observe(this, Observer {
            loading(it)
            if (it.status == Status.SUCCESS ) {
                it.data?.let {
                    isLoadMore = false
                    this.listComicByKeyWords = it
                    listComicAdapter.notifyDataSearch(listComicByKeyWords)
                    if (this.listComicByKeyWords.count() >= items) {
                        isLoadMore = true
                    }
                }
            }
        })

    }

    override fun initEvent() {
        ivSearch.setOnClickListener {
            val data = hashMapOf(
                    "keywords" to edtSearch.text.toString(),
                    "limit" to items,
                    "offset" to page
            )
            viewModel.getListComicByKeyWords(data)
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