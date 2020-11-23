package com.ggg.home.repository

import android.text.TextUtils
import androidx.lifecycle.LiveData
import com.ggg.common.utils.AppExecutors
import com.ggg.common.utils.NetworkBoundResource
import com.ggg.common.utils.NetworkOnlyResource
import com.ggg.common.vo.Resource
import com.ggg.common.ws.ApiResponse
import com.ggg.home.data.local.HomeDB
import com.ggg.home.data.model.CCHadReadModel
import com.ggg.home.data.model.ChapterHadRead
import com.ggg.home.data.model.post_param.DataSendReportParam
import com.ggg.home.data.remote.HomeRetrofitProvider
import com.ggg.home.data.remote.HomeService
import org.jetbrains.anko.doAsync
import javax.inject.Inject

class ViewComicRepository {
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

    fun insertCCHadRead(ccHadReadModel: CCHadReadModel) {
        doAsync {
            db.ccHadReadDao().insertCCHadRead(ccHadReadModel)
        }
    }

    fun getListImage(data: ChapterHadRead): LiveData<Resource<ChapterHadRead>> {
        val callApi = object : NetworkBoundResource<ChapterHadRead, List<String>>(appExecutors = executor) {
            override fun loadFromDb(): LiveData<ChapterHadRead> {
                val chapterId = data.chapterModel!!.chapterId
                return db.chapterDao().getChapterHadRead(chapterId)
            }

            override fun saveCallResult(item: List<String>) {
                if (item.isNotEmpty()) {
                    val chapterModel = data.chapterModel!!
                    chapterModel.listImageUrlString = TextUtils.join(", ", item)
                    chapterModel.imageUrls = item
                    chapterModel.lastModified = System.currentTimeMillis()
                    db.chapterDao().insertChapter(chapterModel)
                }
            }

            override fun createCall(): LiveData<ApiResponse<List<String>>> {
                val chapterModel = data.chapterModel!!
                return api.getListImageOfChap(
                        chapterModel.comicId,
                        chapterModel.chapterId
                )
            }

            override fun shouldFetch(data: ChapterHadRead?): Boolean {
                return true
            }

        }

        return callApi.asLiveData()
    }

    fun sendReport(data: HashMap<String, Any?>): LiveData<Resource<Any>> {
        val callApi = object : NetworkOnlyResource<Any>(appExecutors = executor) {
            override fun createCall(): LiveData<ApiResponse<Any>> {
                val token = data["token"] as? String
                val dataSendReportParam = data["dataSendReportParam"] as DataSendReportParam
                return api.sendReport(authorization = token, dataSendReportParam = dataSendReportParam)
            }

            override fun saveCallResult(item: Any) {
            }
        }
        return callApi.asLiveData()
    }
}