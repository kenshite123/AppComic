package com.ggg.home.repository

import androidx.lifecycle.LiveData
import com.ggg.common.utils.AppExecutors
import com.ggg.common.utils.NetworkBoundResource
import com.ggg.common.utils.NetworkOnlyResource
import com.ggg.common.vo.Resource
import com.ggg.common.ws.ApiResponse
import com.ggg.home.data.local.HomeDB
import com.ggg.home.data.model.ComicModel
import com.ggg.home.data.model.response.LoginResponse
import com.ggg.home.data.remote.HomeRetrofitProvider
import com.ggg.home.data.remote.HomeService
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

    fun getBanners(): LiveData<Resource<List<ComicModel>>> {
        val callApi = object : NetworkBoundResource<List<ComicModel>, List<ComicModel>>(appExecutors = executor) {
            override fun loadFromDb(): LiveData<List<ComicModel>> {
                return db.comicDao().getListBanners()
            }

            override fun createCall(): LiveData<ApiResponse<List<ComicModel>>> {
                return api.getBanners()
            }

            override fun saveCallResult(item: List<ComicModel>) {
                if (item.isNotEmpty()) {
                    db.comicDao().deleteListBanners()
                    db.comicDao().insertListBanners(item)
                }
            }

            override fun shouldFetch(data: List<ComicModel>?): Boolean {
                return true
            }
        }
        return callApi.asLiveData()
    }
}