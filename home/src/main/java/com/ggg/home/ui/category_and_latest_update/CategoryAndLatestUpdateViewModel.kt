package com.ggg.home.ui.category_and_latest_update

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ggg.common.vo.Resource
import com.ggg.home.data.model.CategoryModel
import com.ggg.home.data.model.ComicModel
import com.ggg.home.data.model.ComicWithCategoryModel
import com.ggg.home.repository.CategoryAndLatestUpdateRepository
import javax.inject.Inject

class CategoryAndLatestUpdateViewModel @Inject constructor(private val categoryAndLatestUpdateRepository: CategoryAndLatestUpdateRepository) : ViewModel() {
    private val requestGetAllListCategories: MutableLiveData<Boolean> = MutableLiveData()
    var getAllListCategoriesResult: LiveData<Resource<List<CategoryModel>>>

    private val requestGetListLatestUpdate: MutableLiveData<HashMap<String, Int>> = MutableLiveData()
    var getListLatestUpdateResult: LiveData<Resource<List<ComicModel>>>

    private val requestGetListComicByFilter: MutableLiveData<HashMap<String, Any>> = MutableLiveData()
    var getListComicByFilterResult: LiveData<Resource<List<ComicWithCategoryModel>>>

    private val requestGetListLatestUpdateWithFilter: MutableLiveData<HashMap<String, Any>> = MutableLiveData()
    var getListLatestUpdateWithFilterResult: LiveData<Resource<List<ComicModel>>>

    init {
        getAllListCategoriesResult = Transformations.switchMap(requestGetAllListCategories) {
            return@switchMap categoryAndLatestUpdateRepository.getAllListCategories()
        }

        getListLatestUpdateResult = Transformations.switchMap(requestGetListLatestUpdate) {
            return@switchMap categoryAndLatestUpdateRepository.getListLatestUpdate(it)
        }

        getListComicByFilterResult = Transformations.switchMap(requestGetListComicByFilter) {
            return@switchMap categoryAndLatestUpdateRepository.getAllListComicByFilter(it)
        }

        getListLatestUpdateWithFilterResult = Transformations.switchMap(requestGetListLatestUpdateWithFilter) {
            return@switchMap categoryAndLatestUpdateRepository.getListLatestUpdateWithFilter(it)
        }
    }

    fun getAllListCategories() {
        requestGetAllListCategories.value = true
    }

    fun getListLatestUpdate(data: HashMap<String, Int>) {
        requestGetListLatestUpdate.value = data
    }

    fun getAllListComicByFilter(data: HashMap<String, Any>) {
        requestGetListComicByFilter.value = data
    }

    fun getListLatestUpdateWithFilter(data: HashMap<String, Any>) {
        requestGetListLatestUpdateWithFilter.value = data
    }
}