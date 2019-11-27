package com.ggg.home.repository

import com.ggg.common.utils.AppExecutors
import com.ggg.home.data.local.HomeDB
import com.ggg.home.data.model.CCHadReadModel
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
}