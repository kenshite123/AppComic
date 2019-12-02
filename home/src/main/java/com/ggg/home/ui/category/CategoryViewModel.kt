package com.ggg.home.ui.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ggg.common.vo.Resource
import com.ggg.home.data.model.CategoryModel
import com.ggg.home.data.model.ComicModel
import com.ggg.home.data.model.ComicWithCategoryModel
import com.ggg.home.repository.CategoryRepository
import javax.inject.Inject

class CategoryViewModel @Inject constructor(private val categoryRepository: CategoryRepository) : ViewModel() {
    private val requestGetAllListCategories: MutableLiveData<Boolean> = MutableLiveData()
    var getAllListCategoriesResult: LiveData<Resource<List<CategoryModel>>>

    private val requestGetListComicByCategory: MutableLiveData<HashMap<String, Long>> = MutableLiveData()
    var getListComicByCategoryResult: LiveData<Resource<List<ComicWithCategoryModel>>>

    init {
        getAllListCategoriesResult = Transformations.switchMap(requestGetAllListCategories) {
            return@switchMap categoryRepository.getAllListCategories()
        }

        getListComicByCategoryResult = Transformations.switchMap(requestGetListComicByCategory) {
            return@switchMap categoryRepository.getListComicByCategory(it)
        }
    }

    fun getAllListCategories() {
        requestGetAllListCategories.value = true
    }

    fun getListComicByCategory(data: HashMap<String, Long>) {
        requestGetListComicByCategory.value = data
    }
}