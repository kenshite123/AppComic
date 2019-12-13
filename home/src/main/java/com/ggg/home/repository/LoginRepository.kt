package com.ggg.home.repository

import android.text.TextUtils
import androidx.lifecycle.LiveData
import com.ggg.common.GGGAppInterface
import com.ggg.common.utils.AppExecutors
import com.ggg.common.utils.NetworkOnlyResource
import com.ggg.common.vo.Resource
import com.ggg.common.ws.ApiResponse
import com.ggg.common.ws.BaseResponse
import com.ggg.home.data.local.HomeDB
import com.ggg.home.data.model.response.LoginResponse
import com.ggg.home.data.remote.HomeRetrofitProvider
import com.ggg.home.data.remote.HomeService
import javax.inject.Inject

class LoginRepository {
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

    fun login(data: HashMap<String, String>): LiveData<Resource<LoginResponse>> {
        val callApi = object : NetworkOnlyResource<LoginResponse>(appExecutors = executor) {
            override fun saveCallResult(item: LoginResponse) {
                if (item != null) {
                    if (item.user != null) {
                        item.user?.let {
                            GGGAppInterface.gggApp.clearListComicFavorite()
                            if (!it.mangaFollows.isNullOrEmpty()) {
                                it.mangaFollows.forEach {
                                    GGGAppInterface.gggApp.addComicToFavorite(it)
                                }
                            }
                        }
                    }
                }
            }

            override fun createCall(): LiveData<ApiResponse<LoginResponse>> {
                val username = data["username"].toString()
                val password = data["password"].toString()
                val token = data["token"].toString()
                return api.login(username, password, token)
            }
        }

        return callApi.asLiveData()
    }
}