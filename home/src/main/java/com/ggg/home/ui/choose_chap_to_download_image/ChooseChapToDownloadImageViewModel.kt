package com.ggg.home.ui.choose_chap_to_download_image

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ggg.home.data.model.ChapterModel
import com.ggg.home.repository.ChooseChapToDownloadImageRepository
import javax.inject.Inject

class ChooseChapToDownloadImageViewModel @Inject constructor(private val chooseChapToDownloadImageRepository: ChooseChapToDownloadImageRepository): ViewModel() {
    private val requestGetListChapters: MutableLiveData<Long> = MutableLiveData()
    var getListChaptersResult: LiveData<List<ChapterModel>>

    init {
        getListChaptersResult = Transformations.switchMap(requestGetListChapters) {
            return@switchMap chooseChapToDownloadImageRepository.getListChapters(it)
        }
    }

    fun getListChapters(comicId: Long) {
        requestGetListChapters.value = comicId
    }
}