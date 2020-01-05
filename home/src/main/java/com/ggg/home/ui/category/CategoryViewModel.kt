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

    private val requestGetListComicByCategory: MutableLiveData<HashMap<String, Any>> = MutableLiveData()
    var getListComicByCategoryResult: LiveData<Resource<List<ComicWithCategoryModel>>>

    private val requestGetListComicByKeyWords: MutableLiveData<HashMap<String, Any>> = MutableLiveData()
    var getListComicByKeyWordsResult: LiveData<Resource<List<ComicModel>>>

    init {
        getAllListCategoriesResult = Transformations.switchMap(requestGetAllListCategories) {
            return@switchMap categoryRepository.getAllListCategories()
        }

        getListComicByCategoryResult = Transformations.switchMap(requestGetListComicByCategory) {
            return@switchMap categoryRepository.getListComicByCategory(it)
        }

        getListComicByKeyWordsResult = Transformations.switchMap(requestGetListComicByKeyWords) {
            return@switchMap categoryRepository.getListComicByKeyWords(it)
        }
    }

    fun getAllListCategories() {
        requestGetAllListCategories.value = true
    }

    fun getListComicByCategory(data: HashMap<String, Any>) {
        requestGetListComicByCategory.value = data
    }

    fun getListComicByKeyWords(data: HashMap<String, Any>) {
        requestGetListComicByKeyWords.value = data
    }
}