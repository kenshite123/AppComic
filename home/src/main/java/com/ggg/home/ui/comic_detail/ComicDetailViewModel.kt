package com.ggg.home.ui.comic_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ggg.common.vo.Resource
import com.ggg.home.data.model.*
import com.ggg.home.data.model.response.NoneResponse
import com.ggg.home.repository.ComicDetailRepository
import javax.inject.Inject

class ComicDetailViewModel @Inject constructor(private val comicDetailRepository: ComicDetailRepository) : ViewModel() {
    private val requestGetListChapters: MutableLiveData<Long> = MutableLiveData()
    var getListChaptersResult: LiveData<Resource<List<ChapterHadRead>>>

    private val requestGetListChaptersDb: MutableLiveData<Long> = MutableLiveData()
    var getListChaptersDbResult: LiveData<Resource<List<ChapterHadRead>>>

    private val requestGetListComments: MutableLiveData<HashMap<String, Long>> = MutableLiveData()
    var getListCommentsResult: LiveData<Resource<List<CommentModel>>>

    private val requestGetComicInfo: MutableLiveData<Long> = MutableLiveData()
    var getGetComicInfoResult: LiveData<Resource<ComicWithCategoryModel>>

    private val requestFavoriteComic: MutableLiveData<HashMap<String, Any>> = MutableLiveData()
    var favoriteComicResult: LiveData<Resource<NoneResponse>>

    private val requestUnFavoriteComic: MutableLiveData<HashMap<String, Any>> = MutableLiveData()
    var unFavoriteComicResult: LiveData<Resource<NoneResponse>>

    init {
        getListChaptersResult = Transformations.switchMap(requestGetListChapters) {
            return@switchMap comicDetailRepository.getListChapters(it)
        }

        getListChaptersDbResult = Transformations.switchMap(requestGetListChaptersDb) {
            return@switchMap comicDetailRepository.getListChaptersDb(it)
        }

        getListCommentsResult = Transformations.switchMap(requestGetListComments) {
            return@switchMap comicDetailRepository.getListComments(it)
        }

        getGetComicInfoResult = Transformations.switchMap(requestGetComicInfo) {
            return@switchMap comicDetailRepository.getComicInfo(it)
        }

        favoriteComicResult = Transformations.switchMap(requestFavoriteComic) {
            return@switchMap comicDetailRepository.favoriteComic(it)
        }

        unFavoriteComicResult = Transformations.switchMap(requestUnFavoriteComic) {
            return@switchMap comicDetailRepository.unFavoriteComic(it)
        }
    }

    fun getListChapters(comicId: Long) {
        requestGetListChapters.value = comicId
    }

    fun getListChaptersDb(comicId: Long) {
        requestGetListChaptersDb.value = comicId
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

    fun favoriteComic(data: HashMap<String, Any>) {
        requestFavoriteComic.value = data
    }

    fun unFavoriteComic(data: HashMap<String, Any>) {
        requestUnFavoriteComic.value = data
    }
}