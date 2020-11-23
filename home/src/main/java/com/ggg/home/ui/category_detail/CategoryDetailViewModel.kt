package com.ggg.home.ui.category_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ggg.common.vo.Resource
import com.ggg.home.data.model.ComicWithCategoryModel
import com.ggg.home.repository.CategoryDetailRepository
import javax.inject.Inject

class CategoryDetailViewModel @Inject constructor(private val categoryDetailRepository: CategoryDetailRepository) : ViewModel() {
    private val requestGetListComicByCategory: MutableLiveData<HashMap<String, Any>> = MutableLiveData()
    var getListComicByCategoryResult: LiveData<Resource<List<ComicWithCategoryModel>>>

    init {
        getListComicByCategoryResult = Transformations.switchMap(requestGetListComicByCategory) {
            return@switchMap categoryDetailRepository.getListComicByCategory(it)
        }
    }

    fun getListComicByCategory(data: HashMap<String, Any>) {
        requestGetListComicByCategory.value = data
    }
}