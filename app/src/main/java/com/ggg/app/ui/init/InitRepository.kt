package com.ggg.app.ui.init

import androidx.lifecycle.LiveData
import com.ggg.common.utils.AppExecutors
import com.ggg.common.vo.Resource
import com.ggg.home.data.local.HomeDB
import com.ggg.home.data.remote.HomeRetrofitProvider
import com.ggg.home.data.remote.HomeService
import javax.inject.Inject

class InitRepository {
    private val executor: AppExecutors
    private var homeApi: HomeService
    private var retrofit: HomeRetrofitProvider
    private var db: HomeDB

    @Inject
    constructor(appExec: AppExecutors, retrofit: HomeRetrofitProvider, db: HomeDB) {
        this.executor = appExec
        homeApi = retrofit.connectAPI()
        this.retrofit = retrofit
        this.db = db
    }

    fun initData(): LiveData<Resource<Boolean>> {
        val task = FetchResouceTask(homeApi)
        executor.networkIO().execute(task)
        return task.liveData
    }
}