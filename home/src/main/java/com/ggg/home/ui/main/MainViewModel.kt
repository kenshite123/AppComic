package com.ggg.home.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ggg.common.vo.Resource
import com.ggg.home.data.model.ComicModel
import com.ggg.home.data.model.DownloadComicModel
import com.ggg.home.repository.MainRepository
import javax.inject.Inject

class MainViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {
    private val requestGetListComicNotDownloaded: MutableLiveData<Boolean> = MutableLiveData()
    var getListComicNotDownloadedResult: LiveData<Resource<List<DownloadComicModel>>>

    init {
        getListComicNotDownloadedResult = Transformations.switchMap(requestGetListComicNotDownloaded) {
            return@switchMap mainRepository.getListComicNotDownloaded()
        }
    }

    fun updateDownloadedComic(srcImg: String, chapterId: Long) {
        mainRepository.updateDownloadedComic(srcImg, chapterId)
    }

    fun updateDownloadComic(downloadComicModel: DownloadComicModel) {
        mainRepository.updateDownloadComic(downloadComicModel)
    }

    fun getListComicNotDownloaded() {
        requestGetListComicNotDownloaded.value = true
    }
}