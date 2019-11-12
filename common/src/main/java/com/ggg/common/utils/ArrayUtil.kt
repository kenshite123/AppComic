package com.ggg.common.utils

/**
 * Created by nguyenminhtuan on 01/02/2018.
 */
object ArrayUtil {
    fun isEmpty(array: List<*>?): Boolean {
        return !(array != null && array.size > 0)
    }
}