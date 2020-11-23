package com.ggg.home.repository

import androidx.lifecycle.LiveData
import com.ggg.common.utils.AppExecutors
import com.ggg.common.utils.NetworkOnlyResource
import com.ggg.common.vo.Resource
import com.ggg.common.ws.ApiResponse
import com.ggg.home.data.local.HomeDB
import com.ggg.home.data.model.CommentModel
import com.ggg.home.data.model.response.NoneResponse
import com.ggg.home.data.remote.HomeRetrofitProvider
import com.ggg.home.data.remote.HomeService
import javax.inject.Inject

class MyCommentRepository {
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

    fun getListMyComment(data: HashMap<String, Any>): LiveData<Resource<List<CommentModel>>> {
        val callApi = object : NetworkOnlyResource<List<CommentModel>>(appExecutors = executor) {
            override fun saveCallResult(item: List<CommentModel>) {

            }

            override fun createCall(): LiveData<ApiResponse<List<CommentModel>>> {
                return api.getListMyComment(
                        data["token"].toString(),
                        data["limit"]!! as Int,
                        data["offset"]!! as Int
                )
            }

        }

        return callApi.asLiveData()
    }

    fun deleteComment(data: HashMap<String, Any>): LiveData<Resource<List<NoneResponse>>> {
        val callApi = object : NetworkOnlyResource<List<NoneResponse>>(appExecutors = executor) {
            override fun saveCallResult(item: List<NoneResponse>) {

            }

            override fun createCall(): LiveData<ApiResponse<List<NoneResponse>>> {
                return api.deleteComment(
                        data["token"].toString(),
                        data["commentId"]!! as Long
                )
            }

        }

        return callApi.asLiveData()
    }
}