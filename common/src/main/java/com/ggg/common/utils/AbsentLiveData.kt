package com.ggg.common.utils

import androidx.lifecycle.LiveData


/**
 * Created by TuanNguyen on 12/12/17.
 */

class AbsentLiveData<T>: LiveData<T> {
    constructor(){
        postValue(null)
    }

    companion object {
        fun <T> create():LiveData<T>{
            return AbsentLiveData()
        }
    }
}
