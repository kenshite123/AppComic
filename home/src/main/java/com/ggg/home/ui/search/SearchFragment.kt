package com.ggg.home.ui.search

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.inputmethod.EditorInfo


class SearchFragment : HomeBaseFragment() {
    private lateinit var viewModel: CategoryViewModel
    var page: Long = 0
    var items: Long = 60
    var isFirstLoad = true
    var isLoadMore = true
    lateinit var listComicAdapter: ListComicAdapter
    var listComicByKeyWords: List<ComicModel> = arrayListOf()
    var isLoadAllData = false
    lateinit var gridLayoutManager : GridLayoutManager

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
        showActionBar()
        hideBottomNavView()
        setTitleActionBar(R.string.TEXT_SEARCH)

        initViews()
        initEvent()
    }

    private fun initViews() {
        gridLayoutManager = GridLayoutManager(context!!, 3)
        listComicAdapter = ListComicAdapter(context!!, this, this.listComicByKeyWords, true)
        rvListComicSearch.setHasFixedSize(true)
        rvListComicSearch.layoutManager = gridLayoutManager
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
                if (!isLoadAllData) {
                    hideSoftKeyboard()
                    it.data?.let {
                        if (isLoadMore) {
                            isLoadMore = false
                            val list = this.listComicByKeyWords.toMutableList()
                            list.addAll(it)
                            this.listComicByKeyWords = list.toList()
                            listComicAdapter.notifyDataSearch(listComicByKeyWords)
                            isLoadAllData = it.size < items
                        } else {
                            this.listComicByKeyWords = it
                            listComicAdapter.notifyDataSearch(listComicByKeyWords)
                        }

                    }
                }
            }
        })
    }

    override fun initEvent() {
        ivSearch.setOnClickListener {
            isLoadAllData = false
            isLoadMore = false
            getListComicByKeyWords()
        }

        edtSearch.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                isLoadAllData = false
                isLoadMore = false
                getListComicByKeyWords()
                return@OnEditorActionListener true
            }
            false
        })

        rvListComicSearch.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!isLoadMore) {
                    val visibleItemCount = 3
                    val totalItemCount = gridLayoutManager.itemCount
                    val pastVisiblesItems = gridLayoutManager.findLastCompletelyVisibleItemPosition()

                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        isLoadMore = true
                        page++
                        getListComicByKeyWords()
                    }
                }
            }
        })

    }

    private fun getListComicByKeyWords() {
        Log.d("page", page.toString())
        if (!isLoadMore) {
            page = 0
        }
        val data = hashMapOf(
                "keywords" to edtSearch.text.toString(),
                "limit" to items,
                "offset" to page
        )
        viewModel.getListComicByKeyWords(data)
    }

    override fun onResume() {
        super.onResume()
        if (isFirstLoad) {
            initObserver()
            isFirstLoad = false
        }
    }
}