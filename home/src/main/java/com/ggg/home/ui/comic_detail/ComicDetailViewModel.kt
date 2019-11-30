package com.ggg.home.ui.comic_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ggg.common.vo.Resource
import com.ggg.home.data.model.*
import com.ggg.home.repository.ComicDetailRepository
import javax.inject.Inject

class ComicDetailViewModel @Inject constructor(private val comicDetailRepository: ComicDetailRepository) : ViewModel() {
    private val requestGetListChapters: MutableLiveData<Long> = MutableLiveData()
    var getListChaptersResult: LiveData<Resource<List<ChapterHadRead>>>

    private val requestGetListComments: MutableLiveData<HashMap<String, Long>> = MutableLiveData()
    var getListCommentsResult: LiveData<Resource<List<CommentModel>>>

    private val requestGetComicInfo: MutableLiveData<Long> = MutableLiveData()
    var getGetComicInfoResult: LiveData<Resource<ComicWithCategoryModel>>

    init {
        getListChaptersResult = Transformations.switchMap(requestGetListChapters) {
            return@switchMap comicDetailRepository.getListChapters(it)
        }

        getListCommentsResult = Transformations.switchMap(requestGetListComments) {
            return@switchMap comicDetailRepository.getListComments(it)
        }

        getGetComicInfoResult = Transformations.switchMap(requestGetComicInfo) {
            return@switchMap comicDetailRepository.getComicInfo(it)
        }
    }

    fun getListChapters(comicId: Long) {
        requestGetListChapters.value = comicId
    }

    fun insertCCHadRead(ccHadReadModel: CCHadReadModel) {
        comicDetailRepository.insertCCHadRead(ccHadReadModel)
    }

    fun getListComments(data: HashMap<String, Long>) {
        requestGetListComments.value = data
    }

    fun getComicInfo(comicId: Long) {
        requestGetComicInfo.value = comicId
    }
}