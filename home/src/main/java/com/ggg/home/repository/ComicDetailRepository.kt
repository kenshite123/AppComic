package com.ggg.home.repository

import android.text.TextUtils
import androidx.lifecycle.LiveData
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
import org.w3c.dom.Text
import javax.inject.Inject

class ComicDetailRepository {
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

    fun getListChapters(comicId: Long): LiveData<Resource<List<ChapterHadRead>>> {
        val callApi = object : NetworkBoundResource<List<ChapterHadRead>, List<ChapterModel>>(appExecutors = executor) {
            override fun loadFromDb(): LiveData<List<ChapterHadRead>> {
                return db.chapterDao().getListChaptersComicHadRead(comicId)
            }

            override fun createCall(): LiveData<ApiResponse<List<ChapterModel>>> {
                return api.getListChaptersComic(comicId)
            }

            override fun saveCallResult(item: List<ChapterModel>) {
                if (item.isNotEmpty()) {
                    val listChapter = db.chapterDao().getListChaptersComicData(comicId = comicId)
                    if (!listChapter.isNullOrEmpty()) {
                        for (i in 0 until item.count()) {
                            val chapterModel = item[i]
                            val list = listChapter.filter { it.chapterId == chapterModel.chapterId }
                            if (list.isNullOrEmpty()) {
                                chapterModel.listImageUrlString = TextUtils.join(", ", chapterModel.imageUrls)
                                chapterModel.comicId = comicId
                                chapterModel.lastModified = System.currentTimeMillis()
                            } else {
                                chapterModel.listImageUrlString = list[0].listImageUrlString
                                chapterModel.hadDownloaded = list[0].hadDownloaded
                                chapterModel.comicId = comicId
                                chapterModel.lastModified = System.currentTimeMillis()
                            }
                            db.chapterDao().insertChapter(chapterModel)
                        }
                    } else {
                        item.map {
                            it.listImageUrlString = TextUtils.join(", ", it.imageUrls)
                            it.comicId = comicId
                            it.lastModified = System.currentTimeMillis()
                            db.chapterDao().insertChapter(it)
                        }
                    }
                }
            }

            override fun shouldFetch(data: List<ChapterHadRead>?): Boolean {
                return true
            }
        }
        return callApi.asLiveData()
    }

    fun getListChaptersDb(comicId: Long): LiveData<Resource<List<ChapterHadRead>>> {
        val callApi = object : NetworkOnlyDbResource<List<ChapterHadRead>>(appExecutors = executor) {
            override fun loadFromDb(): LiveData<List<ChapterHadRead>> {
                return db.chapterDao().getListChaptersComicHadRead(comicId)
            }
        }
        return callApi.asLiveData()
    }

    fun insertCCHadRead(ccHadReadModel: CCHadReadModel) {
        doAsync {
            db.ccHadReadDao().insertCCHadRead(ccHadReadModel)
        }
    }

    fun getListComments(data: HashMap<String, Long>): LiveData<Resource<List<CommentModel>>> {
        val callApi = object : NetworkOnlyResource<List<CommentModel>> (appExecutors = executor) {
            override fun saveCallResult(item: List<CommentModel>) {

            }

            override fun createCall(): LiveData<ApiResponse<List<CommentModel>>> {
                return api.getListCommentByComic(
                        data["comicId"]!!,
                        data["limit"]!!,
                        data["offset"]!!
                )
            }

        }

        return callApi.asLiveData()
    }

    fun getComicInfo(comicId: Long): LiveData<Resource<ComicWithCategoryModel>> {
        val callApi = object : NetworkBoundResource<ComicWithCategoryModel, ComicModel>(appExecutors = executor) {
            override fun loadFromDb(): LiveData<ComicWithCategoryModel> {
                return db.comicDao().getComicInfo(comicId)
            }

            override fun createCall(): LiveData<ApiResponse<ComicModel>> {
                return api.getComicInfo(comicId)
            }

            override fun saveCallResult(item: ComicModel) {
                    item.categories.forEach {
                        val categoryOfComicModel = CategoryOfComicModel()
                        categoryOfComicModel.categoryId = it.id
                        categoryOfComicModel.categoryName = it.name
                        categoryOfComicModel.comicId = item.id
                        db.categoryOfComicDao().insertCategoryOfComic(categoryOfComicModel)
                    }
                    item.authorsString = TextUtils.join(", ", item.authors)
                    item.lastModified = System.currentTimeMillis()
                    db.comicDao().insertComic(item)
            }

            override fun shouldFetch(data: ComicWithCategoryModel?): Boolean {
                return true
            }
        }
        return callApi.asLiveData()
    }

    fun favoriteComic(data: HashMap<String, Any>): LiveData<Resource<NoneResponse>> {
        val callApi = object : NetworkOnlyResource<NoneResponse> (appExecutors = executor) {
            override fun saveCallResult(item: NoneResponse) {

            }

            override fun createCall(): LiveData<ApiResponse<NoneResponse>> {
                return api.favoriteComic(
                        data["token"].toString(),
                        (data["comicId"]!! as Long).toString()
                )
            }

        }

        return callApi.asLiveData()
    }

    fun unFavoriteComic(data: HashMap<String, Any>): LiveData<Resource<NoneResponse>> {
        val callApi = object : NetworkOnlyResource<NoneResponse> (appExecutors = executor) {
            override fun saveCallResult(item: NoneResponse) {

            }

            override fun createCall(): LiveData<ApiResponse<NoneResponse>> {
                return api.unFavoriteComic(
                        data["token"].toString(),
                        (data["comicId"]!! as Long).toString()
                )
            }

        }

        return callApi.asLiveData()
    }
}