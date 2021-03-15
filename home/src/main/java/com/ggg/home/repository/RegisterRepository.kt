package com.ggg.home.repository

import androidx.lifecycle.LiveData
import com.ggg.common.utils.AppExecutors
import com.ggg.common.utils.NetworkOnlyResource
import com.ggg.common.vo.Resource
import com.ggg.common.ws.ApiResponse
import com.ggg.home.data.local.HomeDB
import com.ggg.home.data.model.post_param.RegisterBody
import com.ggg.home.data.model.response.LoginResponse
import com.ggg.home.data.model.response.RegisterResponse
import com.ggg.home.data.remote.HomeRetrofitProvider
import com.ggg.home.data.remote.HomeService
import javax.inject.Inject

class RegisterRepository {
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

    fun register(data: HashMap<String, String>): LiveData<Resource<RegisterResponse>> {
        val callApi = object : NetworkOnlyResource<RegisterResponse>(appExecutors = executor){
            override fun saveCallResult(item: RegisterResponse) {

            }

            override fun createCall(): LiveData<ApiResponse<RegisterResponse>> {
                val registerBody = RegisterBody()
                registerBody.fullName = data["fullName"].toString()
                registerBody.userName = data["userName"].toString()
                registerBody.email = data["email"].toString()
                registerBody.password = data["password"].toString()
                return api.register(registerBody)
            }
        }
        return callApi.asLiveData()
    }
}