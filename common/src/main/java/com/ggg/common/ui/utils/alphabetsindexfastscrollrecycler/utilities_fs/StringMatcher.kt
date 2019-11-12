package com.ggg.common.ui.utils.alphabetsindexfastscrollrecycler.utilities_fs

/**
 * Created by tuannguyen on 12/18/17.
 */
object StringMatcher {
    fun match(value: String?, keyword: String?): Boolean {
        if (value == null || keyword == null)
            return false
        if (keyword.length > value.length)
            return false

        var i = 0
        var j = 0
        do {
            if (keyword[j] == value[i]) {
                i++
                j++
            } else if (j > 0)
                break
            else
                i++
        } while (i < value.length && j < keyword.length)

        return j == keyword.length
    }
}