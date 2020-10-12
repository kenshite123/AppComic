package com.ggg.home.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ggg.common.vo.Resource
import com.ggg.home.data.model.CategoryModel
import com.ggg.home.data.model.ComicModel
import com.ggg.home.data.model.ComicWithCategoryModel
import com.ggg.home.data.model.DownloadComicModel
import com.ggg.home.repository.HomeRepository
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val homeRepository: HomeRepository) : ViewModel() {
    private val requestGetBanners: MutableLiveData<Boolean> = MutableLiveData()
    var getBannersResult: LiveData<Resource<List<ComicWithCategoryModel>>>

    private val requestGetListLatestUpdate: MutableLiveData<HashMap<String, Int>> = MutableLiveData()
    var getListLatestUpdateResult: LiveData<Resource<List<ComicModel>>>

    private val requestGetListComicNotDownloaded: MutableLiveData<Boolean> = MutableLiveData()
    var getListComicNotDownloadedResult: LiveData<Resource<List<DownloadComicModel>>>

    init {
        getBannersResult = Transformations.switchMap(requestGetBanners) {
            return@switchMap homeRepository.getBanners()
        }

        getListLatestUpdateResult = Transformations.switchMap(requestGetListLatestUpdate) {
            return@switchMap homeRepository.getListLatestUpdate(it)
        }

        getListComicNotDownloadedResult = Transformations.switchMap(requestGetListComicNotDownloaded) {
            return@switchMap homeRepository.getListComicNotDownloaded()
        }
    }

    fun getBanners() {
        requestGetBanners.value = true
    }

    fun getListLatestUpdate(data: HashMap<String, Int>) {
        requestGetListLatestUpdate.value = data
    }

    fun updateDownloadedComic(srcImg: String) {
        homeRepository.updateDownloadedComic(srcImg)
    }

    fun checkDownloadComic(comicId: Long, chapterId: Long) {
        homeRepository.checkDownloadComic(comicId = comicId, chapterId = chapterId)
    }

    fun updateDownloadComic(downloadComicModel: DownloadComicModel) {
        homeRepository.updateDownloadComic(downloadComicModel)
    }

    fun updateChapDownloaded(chapterId: Long) {
        homeRepository.updateChapDownloaded(chapterId = chapterId)
    }

    fun getListComicNotDownloaded() {
        requestGetListComicNotDownloaded.value = true
    }

    fun updateListNotDownloadToDownloading() {
        homeRepository.updateListNotDownloadToDownloading()
    }
}