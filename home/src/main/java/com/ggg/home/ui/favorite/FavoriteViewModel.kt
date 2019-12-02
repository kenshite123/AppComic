package com.ggg.home.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ggg.common.vo.Resource
import com.ggg.home.data.model.ComicWithCategoryModel
import com.ggg.home.repository.FavoriteRepository
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(private val favoriteRepository: FavoriteRepository) : ViewModel() {
    private val requestGetListFavoriteComic: MutableLiveData<HashMap<String, Int>> = MutableLiveData()
    var getListFavoriteComicResult: LiveData<Resource<MutableList<ComicWithCategoryModel>>>

    init {
        getListFavoriteComicResult = Transformations.switchMap(requestGetListFavoriteComic) {
            return@switchMap favoriteRepository.getListFavoriteComic(it)
        }
    }

    fun getListFavoriteComic(data: HashMap<String, Int>) {
        requestGetListFavoriteComic.value = data
    }
}