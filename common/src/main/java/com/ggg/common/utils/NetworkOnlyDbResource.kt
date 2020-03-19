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
abstract class NetworkOnlyDbResource<ResultType>{
    private var appExecutors: AppExecutors

    private val result = MediatorLiveData<Resource<ResultType>>()


    @MainThread
    constructor(appExecutors: AppExecutors) {
        this.appExecutors = appExecutors
        result.setValue(Resource.loading(null))
        val dbSource = loadFromDb()
        result.addSource(dbSource) { data ->
            result.setValue(Resource.success(data))
        }
    }

    protected fun onFetchFailed() {}

    fun asLiveData(): LiveData<Resource<ResultType>> {
        return result
    }

    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>
}