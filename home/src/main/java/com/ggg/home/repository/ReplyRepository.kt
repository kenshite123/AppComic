package com.ggg.home.repository

import androidx.lifecycle.LiveData
import com.ggg.common.utils.AppExecutors
import com.ggg.common.utils.NetworkOnlyResource
import com.ggg.common.vo.Resource
import com.ggg.common.ws.ApiResponse
import com.ggg.home.data.local.HomeDB
import com.ggg.home.data.model.CommentModel
import com.ggg.home.data.model.post_param.WriteCommentBody
import com.ggg.home.data.model.response.CommentResponse
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

    fun writeComment(data: HashMap<String, Any>): LiveData<Resource<CommentResponse>> {
        val callApi = object : NetworkOnlyResource<CommentResponse>(appExecutors = executor) {
            override fun saveCallResult(item: CommentResponse) {

            }

            override fun createCall(): LiveData<ApiResponse<CommentResponse>> {
                val token = data["token"].toString()
                val writeCommentBody = data["writeCommentBody"] as WriteCommentBody

                return api.writeComment(token, writeCommentBody)
            }
        }

        return callApi.asLiveData()
    }

    fun getCommentDetail(commentId: Long): LiveData<Resource<CommentModel>> {
        val callApi = object : NetworkOnlyResource<CommentModel>(appExecutors = executor) {
            override fun saveCallResult(item: CommentModel) {

            }

            override fun createCall(): LiveData<ApiResponse<CommentModel>> {
                return api.getCommentDetail(commentId)
            }
        }

        return callApi.asLiveData()
    }
}