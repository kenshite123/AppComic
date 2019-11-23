package com.ggg.home.repository

import android.text.TextUtils
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.ggg.common.utils.AppExecutors
import com.ggg.common.utils.NetworkBoundResource
import com.ggg.common.utils.NetworkOnlyResource
import com.ggg.common.vo.Resource
import com.ggg.common.ws.ApiResponse
import com.ggg.home.data.local.HomeDB
import com.ggg.home.data.model.CategoryModel
import com.ggg.home.data.model.CategoryOfComicModel
import com.ggg.home.data.model.ComicModel
import com.ggg.home.data.model.response.LoginResponse
import com.ggg.home.data.remote.HomeRetrofitProvider
import com.ggg.home.data.remote.HomeService
import org.jetbrains.anko.doAsync
import timber.log.Timber
import javax.inject.Inject

class HomeRepository {
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

    fun getBanners(): LiveData<Resource<List<ComicModel>>> {
        val callApi = object : NetworkBoundResource<List<ComicModel>, List<ComicModel>>(appExecutors = executor) {
            override fun loadFromDb(): LiveData<List<ComicModel>> {
//                val mSectionLive = MediatorLiveData<List<ComicModel>>()
//                val listBanners = db.comicDao().getListBanners()
////                listBanners.value?.forEach {
////                    val listCategoryModel = db.categoryDao().getListCategoriesByComicId(it.id)
////                    it.categories = listCategoryModel.value!!
////                }
//                mSectionLive.addSource(listBanners) {
//                    it.forEach {
//                        val listCategoryByComicId = db.categoryDao().getListCategoriesByComicId(it.id)
//                        val mSectionListCategory = MediatorLiveData<List<CategoryModel>>()
//                        mSectionListCategory.addSource(listCategoryByComicId) { listCategory ->
//                            it.categories = listCategory
//                        }
//                    }
//                    mSectionLive.removeSource(listBanners)
//                    mSectionLive.value = it
//                }
//                return mSectionLive

                return db.comicDao().getListBanners()
            }

            override fun createCall(): LiveData<ApiResponse<List<ComicModel>>> {
                return api.getBanners()
            }

            override fun saveCallResult(item: List<ComicModel>) {
                if (item.isNotEmpty()) {
                    db.comicDao().updateClearListBanners()
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

    fun getListLatestUpdate(data: HashMap<String, Int>): LiveData<Resource<List<ComicModel>>> {
        val callApi = object : NetworkBoundResource<List<ComicModel>, List<ComicModel>>(appExecutors = executor) {
            override fun loadFromDb(): LiveData<List<ComicModel>> {
                return db.comicDao().getListLatestUpdate(data["limit"]!!, data["offset"]!!)
            }

            override fun createCall(): LiveData<ApiResponse<List<ComicModel>>> {
                return api.getLatestUpdate(data["limit"]!!, data["offset"]!!)
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