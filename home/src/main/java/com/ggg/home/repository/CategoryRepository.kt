package com.ggg.home.repository

import android.text.TextUtils
import androidx.lifecycle.LiveData
import com.ggg.common.utils.AppExecutors
import com.ggg.common.utils.NetworkBoundResource
import com.ggg.common.vo.Resource
import com.ggg.common.ws.ApiResponse
import com.ggg.home.data.local.HomeDB
import com.ggg.home.data.model.CategoryModel
import com.ggg.home.data.model.ComicModel
import com.ggg.home.data.remote.HomeRetrofitProvider
import com.ggg.home.data.remote.HomeService
import javax.inject.Inject

class CategoryRepository {

    private val executor: AppExecutors
    private var api: HomeService
    private var retrofit: HomeRetrofitProvider
    private var db: HomeDB

    @Inject
    constructor(appExec: AppExecutors, retrofit: HomeRetrofitProvider, db: HomeDB) {
        this.executor = appExec
        api = retrofit.connectAPI()
        this.retrofit = retrofit
        this.db = db
    }

    fun getAllListCategories(): LiveData<Resource<List<CategoryModel>>> {
        val callApi = object : NetworkBoundResource<List<CategoryModel>, List<CategoryModel>>(appExecutors = executor) {
            override fun loadFromDb(): LiveData<List<CategoryModel>> {
                return db.categoryDao().getAllListCategories()
            }

            override fun createCall(): LiveData<ApiResponse<List<CategoryModel>>> {
                return api.getAllListCategories()
            }

            override fun saveCallResult(item: List<CategoryModel>) {
                if (item.isNotEmpty()) {
                    db.categoryDao().insertListCategories(item)
                }
            }

            override fun shouldFetch(data: List<CategoryModel>?): Boolean {
                return true
            }
        }
        return callApi.asLiveData()
    }

    fun getListComicByCategory(data: HashMap<String, Long>): LiveData<Resource<List<ComicModel>>> {
        val callApi = object : NetworkBoundResource<List<ComicModel>, List<ComicModel>>(appExecutors = executor) {
            override fun loadFromDb(): LiveData<List<ComicModel>> {
                return db.comicDao().getListComicByCategory(
                        data["categoryId"] as Long,
                        data["limit"] as Int,
                        data["offset"] as Int
                )
            }

            override fun createCall(): LiveData<ApiResponse<List<ComicModel>>> {
                return api.getListComicByCategory(
                        data["categoryId"] as Long,
                        data["limit"] as Int,
                        data["offset"] as Int
                )
            }

            override fun saveCallResult(item: List<ComicModel>) {
                if (item.isNotEmpty()) {
                    item.forEach { comicModel ->
                        run {
                            val listCategories = arrayListOf<Long>()
                            comicModel.categories.forEach {
                                listCategories.add(it.id)
//                                    val categoryOfComicModel = CategoryOfComicModel()
//                                    categoryOfComicModel.categoryId = it.id
//                                    categoryOfComicModel.categoryName = it.name
//                                    categoryOfComicModel.comicId = comicModel.id
//                                    db.categoryOfComicDao().insertCategoryOfComic(categoryOfComicModel)
                            }
                            comicModel.authorsString = TextUtils.join(", ", comicModel.authors)
                            comicModel.categoriesString = TextUtils.join(", ", listCategories)
                        }
                    }
                    db.comicDao().insertListComic(item)
                }
            }

            override fun shouldFetch(data: List<ComicModel>?): Boolean {
                return true
            }
        }
        return callApi.asLiveData()
    }
}