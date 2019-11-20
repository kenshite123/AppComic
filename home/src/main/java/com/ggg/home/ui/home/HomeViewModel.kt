package com.ggg.home.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ggg.common.vo.Resource
import com.ggg.home.data.model.ComicModel
import com.ggg.home.repository.HomeRepository
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val homeRepository: HomeRepository) : ViewModel() {
    private val requestGetBanners: MutableLiveData<Boolean> = MutableLiveData()
    var getBannersResult: LiveData<Resource<List<ComicModel>>>

    init {
        getBannersResult = Transformations.switchMap(requestGetBanners) {
            return@switchMap homeRepository.getBanners()
        }
    }

    fun getBanners() {
        requestGetBanners.value = true
    }
}