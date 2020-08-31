package com.ggg.home.repository

import androidx.lifecycle.LiveData
import com.ggg.common.utils.AppExecutors
import com.ggg.common.utils.NetworkOnlyDbResource
import com.ggg.common.utils.NetworkOnlyResource
import com.ggg.common.vo.Resource
import com.ggg.common.ws.ApiResponse
import com.ggg.home.data.local.HomeDB
import com.ggg.home.data.model.ChapterModel
import com.ggg.home.data.model.post_param.DataGetListImageToDownloadParam
import com.ggg.home.data.model.response.ChangePassWordResponse
import com.ggg.home.data.remote.HomeRetrofitProvider
import com.ggg.home.data.remote.HomeService
import com.ggg.home.utils.Constant
import javax.inject.Inject

class ChooseChapToDownloadImageRepository {
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

    fun getListChapters(comicId: Long): LiveData<Resource<List<ChapterModel>>> {
        val getDataFromDb = object : NetworkOnlyDbResource<List<ChapterModel>>(appExecutors = executor){
            override fun loadFromDb(): LiveData<List<ChapterModel>> {
                return db.chapterDao().getListChaptersComic(comicId = comicId)
            }
        }
        return getDataFromDb.asLiveData()
    }

    fun getListImageToDownload(param: HashMap<String, Any>): LiveData<Resource<List<ChapterModel>>> {
        val callApi = object : NetworkOnlyResource<List<ChapterModel>>(appExecutors = executor){
            override fun saveCallResult(item: List<ChapterModel>) {
            }

            override fun createCall(): LiveData<ApiResponse<List<ChapterModel>>> {
                val comicId = param["comicId"]!!.toString().toLong()
                val listChapterId = param["chapterIds"]!! as List<Long>
                val dataGetListImageToDownloadParam = DataGetListImageToDownloadParam()
                dataGetListImageToDownloadParam.chapterIds = listChapterId
                return api.getListImageToDownload(comicId = comicId, dataGetListImageToDownloadParam = dataGetListImageToDownloadParam)
            }
        }
        return callApi.asLiveData()
    }
}