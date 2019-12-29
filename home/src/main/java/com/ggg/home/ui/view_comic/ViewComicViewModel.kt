package com.ggg.home.ui.view_comic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ggg.common.vo.Resource
import com.ggg.home.data.model.CCHadReadModel
import com.ggg.home.data.model.ChapterHadRead
import com.ggg.home.repository.UserRepository
import com.ggg.home.repository.ViewComicRepository
import javax.inject.Inject

class ViewComicViewModel @Inject constructor(private val viewComicRepository: ViewComicRepository) : ViewModel() {
    private val requestGetListImage: MutableLiveData<ChapterHadRead> = MutableLiveData()
    var getListImageResult: LiveData<Resource<ChapterHadRead>>

    init {
        getListImageResult = Transformations.switchMap(requestGetListImage) {
            return@switchMap viewComicRepository.getListImage(it)
        }
    }

    fun insertCCHadRead(ccHadReadModel: CCHadReadModel) {
        viewComicRepository.insertCCHadRead(ccHadReadModel)
    }

    fun getListImageOfChapter(chapterHadRead: ChapterHadRead) {
        requestGetListImage.value = chapterHadRead
    }
}