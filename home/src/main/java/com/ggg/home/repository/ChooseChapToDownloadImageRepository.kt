package com.ggg.home.repository

import androidx.lifecycle.LiveData
import com.ggg.common.utils.AppExecutors
import com.ggg.home.data.local.HomeDB
import com.ggg.home.data.model.ChapterModel
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

    fun getListChapters(comicId: Long): LiveData<List<ChapterModel>> {
        return db.chapterDao().getListChaptersComic(comicId = comicId)
    }
}