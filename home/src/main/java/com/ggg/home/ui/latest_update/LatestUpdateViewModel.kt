package com.ggg.home.ui.latest_update

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ggg.common.vo.Resource
import com.ggg.home.data.model.ComicWithCategoryModel
import com.ggg.home.repository.LatestUpdateRepository
import javax.inject.Inject

class LatestUpdateViewModel @Inject constructor(private val latestUpdateRepository: LatestUpdateRepository) : ViewModel() {
    private val requestGetListLatestUpdate: MutableLiveData<HashMap<String, Int>> = MutableLiveData()
    var getListLatestUpdateResult: LiveData<Resource<List<ComicWithCategoryModel>>>

    init {
        getListLatestUpdateResult = Transformations.switchMap(requestGetListLatestUpdate) {
            return@switchMap latestUpdateRepository.getListLatestUpdate(it)
        }
    }

    fun getListLatestUpdate(data: HashMap<String, Int>) {
        requestGetListLatestUpdate.value = data
    }
}