package com.ggg.home.utils

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class TokenInterceptor : Interceptor {

    private var mToken: String

    constructor(token: String) {
        mToken = token
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val original: Request = chain.request()
//        val url = original.url().url().toString()
//        val loginApi = ServerPath.baseUri + ServerPath.LOGIN
//        val googleApi = ServerPath.googleUri
        val requestBuilder = original.newBuilder()
        //handle add authorization
//        if (!url.equals(loginApi) && !url.contains(googleApi)) {
//            val userEmail = PrefsUtil.instance.getUserEmail()
//            requestBuilder.addHeader("X-UserModel-Token", mToken)
//            requestBuilder.addHeader("X-UserModel-Email", userEmail)
//            requestBuilder.header("Content-type", "application/json")
//        }
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}