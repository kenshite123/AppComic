package com.ggg.home.ui.comic_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ggg.common.vo.Resource
import com.ggg.home.data.model.CCHadReadModel
import com.ggg.home.data.model.ChapterHadRead
import com.ggg.home.data.model.ChapterModel
import com.ggg.home.repository.ComicDetailRepository
import javax.inject.Inject

class ComicDetailViewModel @Inject constructor(private val comicDetailRepository: ComicDetailRepository) : ViewModel() {
    private val requestGetListChapters: MutableLiveData<Long> = MutableLiveData()
    var getListChaptersResult: LiveData<Resource<List<ChapterHadRead>>>

    init {
        getListChaptersResult = Transformations.switchMap(requestGetListChapters) {
            return@switchMap comicDetailRepository.getListChapters(it)
        }
    }

    fun getListChapters(comicId: Long) {
        requestGetListChapters.value = comicId
    }

    fun insertCCHadRead(ccHadReadModel: CCHadReadModel) {
        comicDetailRepository.insertCCHadRead(ccHadReadModel)
    }
}