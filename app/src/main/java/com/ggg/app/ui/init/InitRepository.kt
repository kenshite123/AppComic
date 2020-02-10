package com.ggg.app.ui.init

import android.text.TextUtils
import androidx.lifecycle.LiveData
import com.ggg.common.utils.AppExecutors
import com.ggg.common.utils.NetworkBoundResource
import com.ggg.common.utils.NetworkOnlyResource
import com.ggg.common.vo.Resource
import com.ggg.common.ws.ApiResponse
import com.ggg.home.data.local.HomeDB
import com.ggg.home.data.model.*
import com.ggg.home.data.remote.HomeRetrofitProvider
import com.ggg.home.data.remote.HomeService
import javax.inject.Inject

class InitRepository {
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
        val callApi = object : NetworkOnlyResource<List<ComicModel>>(appExecutors = executor) {
            override fun createCall(): LiveData<ApiResponse<List<ComicModel>>> {
                return api.getBanners()
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

    fun getListLatestUpdate(data: HashMap<String, Int>): LiveData<Resource<List<ComicModel>>> {
        val callApi = object : NetworkOnlyResource<List<ComicModel>>(appExecutors = executor) {
            override fun createCall(): LiveData<ApiResponse<List<ComicModel>>> {
                return api.getLatestUpdate(data["limit"]!!, data["offset"]!!)
            }

            override fun saveCallResult(item: List<ComicModel>) {
                if (item.isNotEmpty()) {
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

    fun getListChapters(comicId: Long): LiveData<Resource<List<ChapterModel>>> {
        val callApi = object : NetworkOnlyResource<List<ChapterModel>>(appExecutors = executor) {
            override fun createCall(): LiveData<ApiResponse<List<ChapterModel>>> {
                return api.getListChaptersComic(comicId)
            }

            override fun saveCallResult(item: List<ChapterModel>) {
                if (item.isNotEmpty()) {
                    item.map {
                        it.listImageUrlString = TextUtils.join(", ", it.imageUrls)
                        it.comicId = comicId
                        it.lastModified = System.currentTimeMillis()
                    }
                    db.chapterDao().insertListChapter(item)
                }
            }
        }
        return callApi.asLiveData()
    }

    fun getConfig(): LiveData<Resource<ConfigModel>> {
        val callApi = object : NetworkOnlyResource<ConfigModel>(appExecutors = executor) {
            override fun createCall(): LiveData<ApiResponse<ConfigModel>> {
                return api.getConfig()
            }

            override fun saveCallResult(item: ConfigModel) {

            }
        }
        return callApi.asLiveData()
    }
}