package com.ggg.home.repository

import androidx.lifecycle.LiveData
import com.ggg.common.utils.AppExecutors
import com.ggg.common.utils.NetworkOnlyResource
import com.ggg.common.vo.Resource
import com.ggg.common.ws.ApiResponse
import com.ggg.home.data.local.HomeDB
import com.ggg.home.data.model.post_param.WriteCommentBody
import com.ggg.home.data.model.response.NoneResponse
import com.ggg.home.data.remote.HomeRetrofitProvider
import com.ggg.home.data.remote.HomeService
import javax.inject.Inject

class ReplyRepository {
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

    fun writeComment(data: HashMap<String, Any>): LiveData<Resource<NoneResponse>> {
        val callApi = object : NetworkOnlyResource<NoneResponse>(appExecutors = executor) {
            override fun saveCallResult(item: NoneResponse) {

            }

            override fun createCall(): LiveData<ApiResponse<NoneResponse>> {
                val token = data["token"].toString()
                val writeCommentBody = data["writeCommentBody"] as WriteCommentBody

                return api.writeComment(token, writeCommentBody)
            }
        }

        return callApi.asLiveData()
    }
}