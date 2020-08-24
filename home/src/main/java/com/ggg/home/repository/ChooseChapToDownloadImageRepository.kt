package com.ggg.home.repository

import androidx.lifecycle.LiveData
import com.ggg.common.utils.AppExecutors
import com.ggg.common.utils.NetworkOnlyDbResource
import com.ggg.common.utils.NetworkOnlyResource
import com.ggg.common.vo.Resource
import com.ggg.home.data.local.HomeDB
import com.ggg.home.data.model.ChapterModel
import com.ggg.home.data.model.response.ChangePassWordResponse
import com.ggg.home.data.remote.HomeRetrofitProvider
import com.ggg.home.data.remote.HomeService
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
}