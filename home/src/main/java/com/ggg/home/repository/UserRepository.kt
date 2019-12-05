package com.ggg.home.repository

import androidx.lifecycle.LiveData
import com.ggg.common.utils.AppExecutors
import com.ggg.common.utils.NetworkOnlyResource
import com.ggg.common.vo.Resource
import com.ggg.common.ws.ApiResponse
import com.ggg.home.data.local.HomeDB
import com.ggg.home.data.model.response.NoneResponse
import com.ggg.home.data.remote.HomeRetrofitProvider
import com.ggg.home.data.remote.HomeService
import javax.inject.Inject

class UserRepository {
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

    fun logOut(param: HashMap<String, String>): LiveData<Resource<NoneResponse>> {
        val callApi = object : NetworkOnlyResource<NoneResponse>(appExecutors = executor) {
            override fun saveCallResult(item: NoneResponse) {

            }

            override fun createCall(): LiveData<ApiResponse<NoneResponse>> {
                var token = param["token"].toString()
                return api.logOut(token)
            }
        }
        return callApi.asLiveData()
    }
}