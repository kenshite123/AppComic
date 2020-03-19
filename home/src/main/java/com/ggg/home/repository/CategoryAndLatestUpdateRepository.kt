package com.ggg.home.repository

import android.text.TextUtils
import androidx.lifecycle.LiveData
import com.ggg.common.GGGAppInterface
import com.ggg.common.utils.AppExecutors
import com.ggg.common.utils.NetworkBoundResource
import com.ggg.common.utils.NetworkOnlyResource
import com.ggg.common.vo.Resource
import com.ggg.common.ws.ApiResponse
import com.ggg.home.data.local.HomeDB
import com.ggg.home.data.model.CategoryModel
import com.ggg.home.data.model.CategoryOfComicModel
import com.ggg.home.data.model.ComicModel
import com.ggg.home.data.model.ComicWithCategoryModel
import com.ggg.home.data.remote.HomeRetrofitProvider
import com.ggg.home.data.remote.HomeService
import com.ggg.home.utils.Constant
import javax.inject.Inject

class CategoryAndLatestUpdateRepository {
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

    fun getListLatestUpdate(data: HashMap<String, Int>): LiveData<Resource<List<ComicModel>>> {
        val callApi = object : NetworkOnlyResource<List<ComicModel>>(appExecutors = executor) {
            override fun createCall(): LiveData<ApiResponse<List<ComicModel>>> {
                return api.getLatestUpdate(data["limit"]!!, data["offset"]!!)
            }

            override fun saveCallResult(item: List<ComicModel>) {
            }
        }
        return callApi.asLiveData()
    }

    fun getAllListComicByFilter(data: HashMap<String, Any>): LiveData<Resource<List<ComicWithCategoryModel>>> {
        val listCategoryId = data["listCategoryId"] as List<Long>
        val status = data["status"] as String
        val type = data["type"] as String
        val limit = data["limit"] as Int

        val callApi = object : NetworkBoundResource<List<ComicWithCategoryModel>, List<ComicModel>>(appExecutors = executor) {
            override fun loadFromDb(): LiveData<List<ComicWithCategoryModel>> {
                val offset = data["offset"] as Int * limit
                return if (listCategoryId.isNotEmpty() && listCategoryId.count() == 1 && listCategoryId[0] == -1L) {
                    if (type == Constant.FILTER_COMIC_TYPE_UPDATED) {
                        db.comicDao().getListLatestUpdateByFilter(GGGAppInterface.gggApp.siteDeploy, status, limit, offset)
                    } else {
                        db.comicDao().getAllListComic(GGGAppInterface.gggApp.siteDeploy, status, type, limit, offset)
                    }
                } else {
                    if (type == Constant.FILTER_COMIC_TYPE_UPDATED) {
                        db.comicDao().getListLatestUpdateByFilter(GGGAppInterface.gggApp.siteDeploy, listCategoryId, status, limit, offset)
                    } else {
                        db.comicDao().getAllListComic(GGGAppInterface.gggApp.siteDeploy, listCategoryId, status, type, limit, offset)
                    }
                }
            }

            override fun createCall(): LiveData<ApiResponse<List<ComicModel>>> {
                val offset = data["offset"] as Int
                return api.getAllListComicByFilter(listCategoryId, status, type, limit, offset)
            }

            override fun saveCallResult(item: List<ComicModel>) {
                if (item.isNotEmpty()) {
                    db.comicDao().updateClearListBanners()
                    item.forEach { comicModel ->
                        run {
                            comicModel.categories.forEach {
                                val categoryOfComicModel = CategoryOfComicModel()
                                categoryOfComicModel.categoryId = it.id
                                categoryOfComicModel.categoryName = it.name
                                categoryOfComicModel.comicId = comicModel.id
                                db.categoryOfComicDao().insertCategoryOfComic(categoryOfComicModel)
                            }
                            comicModel.authorsString = TextUtils.join(", ", comicModel.authors)
                            comicModel.lastModified = System.currentTimeMillis()
                        }
                    }
                    db.comicDao().insertListComic(item)
                }
            }

            override fun shouldFetch(data: List<ComicWithCategoryModel>?): Boolean {
                return true
            }
        }
        return callApi.asLiveData()
    }

    fun getAllListComicByFilterOnline(data: HashMap<String, Any>): LiveData<Resource<List<ComicModel>>> {
        val listCategoryId = data["listCategoryId"] as List<Long>
        val status = data["status"] as String
        val type = data["type"] as String
        val limit = data["limit"] as Int

        val callApi = object : NetworkOnlyResource<List<ComicModel>>(appExecutors = executor) {
            override fun createCall(): LiveData<ApiResponse<List<ComicModel>>> {
                val offset = data["offset"] as Int
                return api.getAllListComicByFilter(listCategoryId, status, type, limit, offset)
            }

            override fun saveCallResult(item: List<ComicModel>) {
                if (item.isNotEmpty()) {
                    db.comicDao().updateClearListBanners()
                    item.forEach { comicModel ->
                        run {
                            comicModel.categories.forEach {
                                val categoryOfComicModel = CategoryOfComicModel()
                                categoryOfComicModel.categoryId = it.id
                                categoryOfComicModel.categoryName = it.name
                                categoryOfComicModel.comicId = comicModel.id
                                db.categoryOfComicDao().insertCategoryOfComic(categoryOfComicModel)
                            }
                            comicModel.authorsString = TextUtils.join(", ", comicModel.authors)
                            comicModel.lastModified = System.currentTimeMillis()
                        }
                    }
                    db.comicDao().insertListComic(item)
                }
            }
        }
        return callApi.asLiveData()
    }

    fun getListLatestUpdateWithFilter(data: HashMap<String, Any>): LiveData<Resource<List<ComicModel>>> {
        val callApi = object : NetworkOnlyResource<List<ComicModel>>(appExecutors = executor) {
            override fun createCall(): LiveData<ApiResponse<List<ComicModel>>> {
                val listCategoryId = data["listCategoryId"] as List<Long>
                val status = data["status"] as String
                val type = data["type"] as String
                val limit = data["limit"] as Int
                val offset = data["offset"] as Int

                return api.getAllListComicByFilter(listCategoryId, status, type, limit, offset)
            }

            override fun saveCallResult(item: List<ComicModel>) {
            }
        }
        return callApi.asLiveData()
    }
}