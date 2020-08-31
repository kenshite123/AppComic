package com.ggg.home.ui.library

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ggg.common.vo.Resource
import com.ggg.home.data.model.*
import com.ggg.home.repository.LibraryRepository
import javax.inject.Inject

class LibraryViewModel @Inject constructor(private val libraryRepository: LibraryRepository) : ViewModel() {
    private val requestGetListHistory: MutableLiveData<HashMap<String, Int>> = MutableLiveData()
    var getListHistoryResult: LiveData<Resource<List<HistoryModel>>>

    private val requestGetListComicFollow: MutableLiveData<HashMap<String, Any>> = MutableLiveData()
    var getListComicFollowResult: LiveData<Resource<List<ComicWithCategoryModel>>>

    private val requestGetListComicDownloaded: MutableLiveData<HashMap<String, Any>> = MutableLiveData()
    var getListComicDownloadedResult: LiveData<Resource<List<ComicModel>>>

    init {
        getListHistoryResult = Transformations.switchMap(requestGetListHistory) {
            return@switchMap libraryRepository.getListHistory(it)
        }

        getListComicFollowResult = Transformations.switchMap(requestGetListComicFollow) {
            return@switchMap libraryRepository.getListComicFollow(it)
        }

        getListComicDownloadedResult = Transformations.switchMap(requestGetListComicDownloaded) {
            return@switchMap libraryRepository.getListComicDownloaded(it)
        }
    }

    fun getListHistory(data : HashMap<String, Int>) {
        requestGetListHistory.value = data
    }

    fun getListComicFollow(data: HashMap<String, Any>) {
        requestGetListComicFollow.value = data
    }

    fun getListComicDownloaded(data: HashMap<String, Any>) {
        requestGetListComicDownloaded.value = data
    }
}