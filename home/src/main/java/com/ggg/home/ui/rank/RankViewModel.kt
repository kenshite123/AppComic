package com.ggg.home.ui.rank

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ggg.common.vo.Resource
import com.ggg.home.data.model.ComicRankWithCategoryModel
import com.ggg.home.repository.RankRepository
import javax.inject.Inject

class RankViewModel @Inject constructor(private val rankRepository: RankRepository) : ViewModel() {
    private val requestGetListRankComic: MutableLiveData<HashMap<String, Any>> = MutableLiveData()
    var getListRankComicResult: LiveData<Resource<List<ComicRankWithCategoryModel>>>

    init {
        getListRankComicResult = Transformations.switchMap(requestGetListRankComic) {
            return@switchMap rankRepository.getListRankByType(it)
        }
    }

    fun getListRankComic(data: HashMap<String, Any>) {
        requestGetListRankComic.value = data
    }
}