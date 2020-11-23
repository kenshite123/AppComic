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
    init {
    }

    fun updateDownloadedComic(srcImg: String) {
        mainRepository.updateDownloadedComic(srcImg)
    }

    fun updateChapDownloaded(chapterId: Long) {
        mainRepository.updateChapDownloaded(chapterId = chapterId)
    }

    fun updateListDownloadingToNotDownload() {
        mainRepository.updateListDownloadingToNotDownload()
    }
}