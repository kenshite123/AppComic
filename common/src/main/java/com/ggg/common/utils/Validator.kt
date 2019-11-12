package com.ggg.common.utils

import java.util.regex.Pattern

/**
 * Created by TuanNguyen on 12/12/17.
 */
object Validator {

    private val USERNAME_PATTERN = "^[a-z0-9_-]{3,15}$"
    private val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$"
    private var usernamePattern: Pattern? = null
    private var passwordPattern: Pattern? = null

    init {
        usernamePattern = Pattern.compile(USERNAME_PATTERN)
        passwordPattern = Pattern.compile(PASSWORD_PATTERN)
    }

    fun isValidPassword(password: String): Boolean {
        val matcher = passwordPattern!!.matcher(password)
        return matcher.matches()
    }

    fun isValidUsername(username: String): Boolean {
        val matcher = usernamePattern!!.matcher(username)
        return matcher.matches()
    }
    fun isEmpty(str:String?): Boolean{
        if (str != null && str.length > 0) {
            return false
        }
        return true
    }

    fun isEmpty(arr:List<Any>?): Boolean{
        if (arr != null && arr.size > 0){
            return false
        }
        return true
    }
}