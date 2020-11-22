package com.ggg.home.repository

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ggg.common.GGGAppInterface
import com.ggg.common.utils.AppExecutors
import com.ggg.common.utils.NetworkBoundResource
import com.ggg.common.utils.NetworkOnlyDbResource
import com.ggg.common.utils.NetworkOnlyResource
import com.ggg.common.vo.Resource
import com.ggg.common.ws.ApiResponse
import com.ggg.home.data.local.HomeDB
import com.ggg.home.data.model.*
import com.ggg.home.data.model.response.NoneResponse
import com.ggg.home.data.remote.HomeRetrofitProvider
import com.ggg.home.data.remote.HomeService
import org.jetbrains.anko.doAsync
import javax.inject.Inject

class LibraryRepository {
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

    fun getListHistory(data: HashMap<String, Int>): LiveData<Resource<List<HistoryModel>>> {
        val callApi = object : NetworkBoundResource<List<HistoryModel>, List<HistoryModel>>(appExecutors = executor) {
            override fun loadFromDb(): LiveData<List<HistoryModel>> {
                val limit = data["limit"]!!
                val offset = data["offset"]!! * limit
                return db.comicDao().getListHistory(GGGAppInterface.gggApp.siteDeploy, limit, offset)
            }

            override fun createCall(): LiveData<ApiResponse<List<HistoryModel>>> {
                return MutableLiveData<ApiResponse<List<HistoryModel>>>()
            }

            override fun saveCallResult(item: List<HistoryModel>) {

            }

            override fun shouldFetch(data: List<HistoryModel>?): Boolean {
                return false
            }

        }

        return callApi.asLiveData()
    }

//    fun getListComicFollow(data: HashMap<String, Any>): LiveData<Resource<List<ComicWithCategoryModel>>> {
//        val callApi = object : NetworkBoundResource<List<ComicWithCategoryModel>, List<ComicModel>>(appExecutors = executor) {
//            override fun loadFromDb(): LiveData<List<ComicWithCategoryModel>> {
//                val listComicId = data["listComicId"] as List<String>
//                return db.comicDao().getListComicFollow(GGGAppInterface.gggApp.siteDeploy, listComicId)
//            }
//
//            override fun createCall(): LiveData<ApiResponse<List<ComicModel>>> {
//                return api.getListComicFollow(
//                        data["token"].toString(),
//                        data["limit"]!! as Int,
//                        data["offset"]!! as Int
//                )
//            }
//
//            override fun saveCallResult(item: List<ComicModel>) {
//                if (item.isNotEmpty()) {
//                    item.forEach { comicModel ->
//                        run {
//                            comicModel.categories.forEach {
//                                val categoryOfComicModel = CategoryOfComicModel()
//                                categoryOfComicModel.categoryId = it.id
//                                categoryOfComicModel.categoryName = it.name
//                                categoryOfComicModel.comicId = comicModel.id
//                                db.categoryOfComicDao().insertCategoryOfComic(categoryOfComicModel)
//                            }
//
//                            GGGAppInterface.gggApp.addComicToFavorite(comicModel.id)
//
//                            comicModel.authorsString = TextUtils.join(", ", comicModel.authors)
//                            comicModel.lastModified = System.currentTimeMillis()
//                        }
//                    }
//                    db.comicDao().insertListComic(item)
//                }
//            }
//
//            override fun shouldFetch(data: List<ComicWithCategoryModel>?): Boolean {
//                return true
//            }
//        }
//
//        return callApi.asLiveData()
//    }

    fun getListComicFollow(data: HashMap<String, Any>): LiveData<Resource<List<ComicModel>>> {
        val callApi = object : NetworkOnlyResource<List<ComicModel>>(appExecutors = executor) {
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

                            GGGAppInterface.gggApp.addComicToFavorite(comicModel.id)

                            comicModel.authorsString = TextUtils.join(", ", comicModel.authors)
                            comicModel.lastModified = System.currentTimeMillis()
                        }
                    }
                    db.comicDao().insertListComic(item)
                }
            }

            override fun createCall(): LiveData<ApiResponse<List<ComicModel>>> {
                return api.getListComicFollow(
                        data["token"].toString(),
                        data["limit"]!! as Int,
                        data["offset"]!! as Int
                )
            }
        }

        return callApi.asLiveData()
    }

    fun getListComicDownloaded(data: HashMap<String, Int>): LiveData<Resource<List<ComicModel>>> {
        val getDataFromDb = object : NetworkOnlyDbResource<List<ComicModel>>(appExecutors = executor){
            override fun loadFromDb(): LiveData<List<ComicModel>> {
                val limit = data["limit"]!!
                val offset = data["offset"]!! * limit
                val listDownloadComic = db.downloadComicDao().getListDownloadComicAndProgress(
                        limit = limit,
                        offset = offset
                )
                if (listDownloadComic.isNullOrEmpty()) {
                    return MutableLiveData()
                } else {
                    val listComicId = mutableListOf<Long>()
                    listDownloadComic.forEach {
                        listComicId.add(it.comicId)
                    }
                    val listComic = db.comicDao().getListComicDownloaded(
                            listComicId = listComicId,
                            siteDeploy = GGGAppInterface.gggApp.siteDeploy
                    )

                    listComic.forEach {
                        for (i in 0 until listDownloadComic.count()) {
                            val downloadComicTotalProgress = listDownloadComic[i]
                            if (it.id == downloadComicTotalProgress.comicId) {
                                it.totalDownloaded = downloadComicTotalProgress.totalDownloaded
                                it.totalNeedToDownload = downloadComicTotalProgress.totalNeedToDownload
                                break
                            }
                        }
                    }

                    val liveData = MutableLiveData<List<ComicModel>>()
                    liveData.postValue(listComic)
                    return liveData
                }
            }
        }
        return getDataFromDb.asLiveData()
    }

    fun unFavoriteComic(data: HashMap<String, String>): LiveData<Resource<NoneResponse>> {
        val callApi = object : NetworkOnlyResource<NoneResponse> (appExecutors = executor) {
            override fun saveCallResult(item: NoneResponse) {

            }

            override fun createCall(): LiveData<ApiResponse<NoneResponse>> {
                return api.unFavoriteComic(
                        data["token"].toString(),
                        data["comicId"].toString()
                )
            }

        }

        return callApi.asLiveData()
    }

    fun deleteListHistory(listComicId: List<Long>) {
        doAsync {
            db.ccHadReadDao().deleteListHistory(listComicId = listComicId)
        }
    }

    fun deleteListDownloaded(listComicId: List<Long>) {
        doAsync {
            db.chapterDao().updateChapNotDownloaded(listComicId = listComicId)
            db.downloadComicDao().clearCacheImageDownload(listComicId = listComicId)
        }
    }
}