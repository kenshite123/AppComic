package com.ggg.common.vo

/**
 * Created by TuanNguyen on 12/12/17.
 */
class Resource <T> constructor(val status: Status?, val data:T?, val message:String?){


    companion object {
        fun <T> successDB(data: T?): Resource<T> {
            return Resource(Status.SUCCESS_DB, data, null)
        }

        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }
    }

}