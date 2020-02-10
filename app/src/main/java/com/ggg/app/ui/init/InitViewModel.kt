package com.ggg.app.ui.init

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ggg.common.vo.Resource
import com.ggg.home.data.model.*
import javax.inject.Inject

class InitViewModel @Inject constructor(private val initRepository: InitRepository) : ViewModel() {
    private val requestGetBanners: MutableLiveData<Boolean> = MutableLiveData()
    var getBannersResult: LiveData<Resource<List<ComicModel>>>

//    private val requestGetListLatestUpdate: MutableLiveData<HashMap<String, Int>> = MutableLiveData()
//    var getListLatestUpdateResult: LiveData<Resource<List<ComicModel>>>

    private val requestGetListChapters: MutableLiveData<Long> = MutableLiveData()
    var getListChaptersResult: LiveData<Resource<List<ChapterModel>>>

    private val requestGetConfigModel: MutableLiveData<Boolean> = MutableLiveData()
    var getConfigModelResult: LiveData<Resource<ConfigModel>>

    init {
        getBannersResult = Transformations.switchMap(requestGetBanners) {
            return@switchMap initRepository.getBanners()
        }

//        getListLatestUpdateResult = Transformations.switchMap(requestGetListLatestUpdate) {
//            return@switchMap initRepository.getListLatestUpdate(it)
//        }

        getListChaptersResult = Transformations.switchMap(requestGetListChapters) {
            return@switchMap initRepository.getListChapters(it)
        }

        getConfigModelResult = Transformations.switchMap(requestGetConfigModel) {
            return@switchMap initRepository.getConfig()
        }
    }

    fun getBanners() {
        requestGetBanners.value = true
    }

    fun getConfig() {
        requestGetConfigModel.value = true
    }

//    fun getListLatestUpdate(data: HashMap<String, Int>) {
//        requestGetListLatestUpdate.value = data
//    }

    fun getListChapters(comicId: Long) {
        requestGetListChapters.value = comicId
    }
}