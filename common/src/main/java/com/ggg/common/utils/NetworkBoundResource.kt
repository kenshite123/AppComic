package com.ggg.common.utils

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.ggg.common.vo.Resource
import com.ggg.common.ws.ApiResponse

/**
 * Created by TuanNguyen on 12/12/17.
 */
abstract class NetworkBoundResource <ResultType, RequestType>{

    private var appExecutors: AppExecutors

    private val result = MediatorLiveData<Resource<ResultType>>()

    @MainThread
    constructor(appExecutors: AppExecutors){
        this.appExecutors = appExecutors
        result.setValue(Resource.loading(null))
        val dbSource = loadFromDb()
        result.addSource(dbSource) { data ->
            result.removeSource(dbSource)
            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource)
            } else {
                result.addSource(dbSource) { newData -> result.setValue(Resource.success(newData)) }
            }
        }
    }

    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
        var apiResponse: LiveData<ApiResponse<RequestType>>
        if (CommonUtils.isInternetAvailable()) {
            apiResponse = createCall()
        } else {
            apiResponse = MutableLiveData<ApiResponse<RequestType>>()
        }
        // we re-attach dbSource as a new source, it will dispatch its latest value quickly
        result.addSource(dbSource) { newData -> result.setValue(Resource.loading(newData)) }
        result.addSource<ApiResponse<RequestType>>(apiResponse) { response ->
            result.removeSource<ApiResponse<RequestType>>(apiResponse)
            result.removeSource(dbSource)

            if (response!!.isSuccessful()) {
                appExecutors.diskIO().execute {
                    saveCallResult(processResponse(response) as RequestType)
                    appExecutors.mainThread().execute {
                        // we specially request a new live data,
                        // otherwise we will get immediately last cached value,
                        // which may not be updated with latest results received from network.
                        result.addSource(loadFromDb()
                        ) { newData -> result.setValue(Resource.success(newData)) }
                    }
                }
            } else {
                onFetchFailed()
                result.addSource(dbSource
                ) { newData -> result.setValue(Resource.error(response.errorMessage!!, newData)) }
            }
        }
    }

    protected fun onFetchFailed() {}

    fun asLiveData(): LiveData<Resource<ResultType>> {
        return result
    }

    @WorkerThread
    protected fun processResponse(response: ApiResponse<RequestType>): RequestType? {
        return response.body
    }

    @WorkerThread
    protected abstract fun saveCallResult(item: RequestType)

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>

    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>


}