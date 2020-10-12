package com.ggg.home.repository

import android.text.TextUtils
import androidx.lifecycle.LiveData
import com.ggg.common.GGGAppInterface
import com.ggg.common.utils.*
import com.ggg.common.vo.Resource
import com.ggg.common.ws.ApiResponse
import com.ggg.home.data.local.HomeDB
import com.ggg.home.data.model.*
import com.ggg.home.data.remote.HomeRetrofitProvider
import com.ggg.home.data.remote.HomeService
import com.ggg.home.utils.Constant
import org.jetbrains.anko.doAsync
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

    fun getBanners(): LiveData<Resource<List<ComicWithCategoryModel>>> {
        val callApi = object : NetworkBoundResource<List<ComicWithCategoryModel>, List<ComicModel>>(appExecutors = executor) {
            override fun loadFromDb(): LiveData<List<ComicWithCategoryModel>> {
                return db.comicDao().getListBanners(GGGAppInterface.gggApp.siteDeploy)
            }

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

            override fun shouldFetch(data: List<ComicWithCategoryModel>?): Boolean {
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

    fun updateDownloadedComic(srcImg: String) {
        doAsync {
            db.downloadComicDao().updateDownloadedComic(id = Utils.md5(srcImg))
        }
    }

    fun checkDownloadComic(comicId: Long, chapterId: Long) {
        doAsync {
            val listDownloadComic = db.downloadComicDao().getListDownloadComic(chapterId = chapterId)
            if (!listDownloadComic.isNullOrEmpty()) {
                val totalDownload = listDownloadComic.count()
                var totalDownloaded = 0
                listDownloadComic.forEach {
                    if (it.hadDownloaded == Constant.IS_DOWNLOADED) {
                        totalDownloaded++
                    }
                }

                if (totalDownloaded == totalDownload) {
                    db.chapterDao().updateChapDownloaded(chapterId = chapterId)
                    val hm = hashMapOf(
                            "comicId" to comicId,
                            "chapterId" to chapterId
                    )
                    GGGAppInterface.gggApp.bus().sendDownloadImageDone(hm = hm)
                }
            }
        }
    }

    fun updateChapDownloaded(chapterId: Long) {
        doAsync {
            db.chapterDao().updateChapDownloaded(chapterId = chapterId)
        }
    }

    fun updateDownloadComic(downloadComicModel: DownloadComicModel) {
        doAsync {
            db.downloadComicDao().insertDownloadComic(downloadComicModel = downloadComicModel)
        }
    }

    fun getListComicNotDownloaded(): LiveData<Resource<List<DownloadComicModel>>> {
        val getDataFromDb = object : NetworkOnlyDbResource<List<DownloadComicModel>>(appExecutors = executor) {
            override fun loadFromDb(): LiveData<List<DownloadComicModel>> {
                return db.downloadComicDao().getAllListNotDownloaded()
            }
        }
        return getDataFromDb.asLiveData()
    }

    fun updateListNotDownloadToDownloading() {
        doAsync {
            db.downloadComicDao().updateListNotDownloadToDownloading()
        }
    }
}