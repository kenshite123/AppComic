package com.ggg.common.vo

import com.ggg.common.utils.Validator


/**
 * Created by TuanNguyen on 12/12/17.
 */
data class Credentials(val username: String, val password: String) {
    fun isValid(): Boolean {
        return Validator.isValidPassword(password) && Validator.isValidUsername(username)
    }
}