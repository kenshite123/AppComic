package com.ggg.common.utils

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.ggg.common.vo.Resource
import com.ggg.common.ws.ApiResponse

/**
 * Created by TuanNguyen on 12/12/17.
 */
abstract class NetworkOnlyResource<RequestType>{
    private var appExecutors: AppExecutors

    private val result = MediatorLiveData<Resource<RequestType>>()


    @MainThread
    constructor(appExecutors: AppExecutors) {
        this.appExecutors = appExecutors
        result.setValue(Resource.loading(null))
        fetchFromNetwork()
    }

    @Suppress("UNCHECKED_CAST")
    private fun fetchFromNetwork() {
        val apiResponse = createCall()
        result.addSource<ApiResponse<RequestType>>(apiResponse) { response ->
            if (response!!.isSuccessful()) {
                appExecutors.diskIO().execute {
                    saveCallResult(processResponse(response) as RequestType)
                    appExecutors.mainThread().execute {
                        // we specially request a new live data,
                        // otherwise we will get immediately last cached value,
                        // which may not be updated with latest results received from network.
                        result.setValue(Resource.success(response.body))
                    }
                }
            } else {
                onFetchFailed()
//                if (response.code == 401){
//                    result.setValue(Resource.error(response.code.toString(), null))
//                }else {
                    result.setValue(Resource.error(response.errorMessage ?: "", null))
//                }
            }
        }

    }

    protected fun onFetchFailed() {}

    fun asLiveData(): LiveData<Resource<RequestType>> {
        return result
    }

    @WorkerThread
    protected fun processResponse(response: ApiResponse<RequestType>): RequestType? {
        return response.body
    }

    @WorkerThread
    protected abstract fun saveCallResult(item: RequestType)


    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>
}