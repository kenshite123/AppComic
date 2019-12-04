package com.ggg.home.repository

import android.text.TextUtils
import androidx.lifecycle.LiveData
import com.ggg.common.utils.AppExecutors
import com.ggg.common.utils.NetworkBoundResource
import com.ggg.common.vo.Resource
import com.ggg.common.ws.ApiResponse
import com.ggg.home.data.local.HomeDB
import com.ggg.home.data.model.*
import com.ggg.home.data.remote.HomeRetrofitProvider
import com.ggg.home.data.remote.HomeService
import javax.inject.Inject

class RankRepository {
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

    fun getListRankByType(data: HashMap<String, Any>): LiveData<Resource<List<ComicRankWithCategoryModel>>> {
        val callApi = object : NetworkBoundResource<List<ComicRankWithCategoryModel>, List<ComicRankModel>>(appExecutors = executor) {
            override fun loadFromDb(): LiveData<List<ComicRankWithCategoryModel>> {
                return db.comicRankDao().getListRankByType(
                        data["type"]!! as String,
                        data["limit"]!! as Int,
                        data["offset"]!! as Int
                )
            }

            override fun createCall(): LiveData<ApiResponse<List<ComicRankModel>>> {
                return api.getListComicRanking(
                        data["type"]!! as String,
                        data["limit"]!! as Int,
                        data["offset"]!! as Int
                )
            }

            override fun saveCallResult(item: List<ComicRankModel>) {
                if (item.isNotEmpty()) {
                    item.forEach { comicRankModel ->
                        run {
                            comicRankModel.categories.forEach {
                                val categoryOfComicModel = CategoryOfComicModel()
                                categoryOfComicModel.categoryId = it.id
                                categoryOfComicModel.categoryName = it.name
                                categoryOfComicModel.comicId = comicRankModel.id
                                db.categoryOfComicDao().insertCategoryOfComic(categoryOfComicModel)
                            }
                            comicRankModel.authorsString = TextUtils.join(", ", comicRankModel.authors)
                            comicRankModel.lastModified = System.currentTimeMillis()
                            comicRankModel.type = data["type"]!! as String
                        }
                    }
                    db.comicRankDao().deleteListComicRankByType(data["type"]!! as String)
                    db.comicRankDao().insertListComic(item)
                }
            }

            override fun shouldFetch(data: List<ComicRankWithCategoryModel>?): Boolean {
                return true
            }
        }
        return callApi.asLiveData()
    }
}