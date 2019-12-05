package com.ggg.home.repository

import androidx.lifecycle.LiveData
import com.ggg.common.utils.AppExecutors
import com.ggg.common.utils.NetworkOnlyResource
import com.ggg.common.vo.Resource
import com.ggg.common.ws.ApiResponse
import com.ggg.home.data.local.HomeDB
import com.ggg.home.data.model.post_param.ChangePassWordBody
import com.ggg.home.data.model.response.ChangePassWordResponse
import com.ggg.home.data.remote.HomeRetrofitProvider
import com.ggg.home.data.remote.HomeService
import javax.inject.Inject

class ChangePassWordRepository {
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

    fun changePassWord(param: HashMap<String, String>): LiveData<Resource<ChangePassWordResponse>> {
        val callApi = object : NetworkOnlyResource<ChangePassWordResponse>(appExecutors = executor){
            override fun saveCallResult(item: ChangePassWordResponse) {

            }

            override fun createCall(): LiveData<ApiResponse<ChangePassWordResponse>> {
                var id = param["id"]!!.toInt()
                var token = param["token"].toString()
                var changePassWordBody: ChangePassWordBody = ChangePassWordBody()
                changePassWordBody.oldPassword = param["oldPassword"].toString()
                changePassWordBody.newPassword = param["newPassword"].toString()
                changePassWordBody.confirmPassword = param["confirmPassword"].toString()
                return api.changePassword(token, id, changePassWordBody)
            }
        }
        return callApi.asLiveData()
    }
}