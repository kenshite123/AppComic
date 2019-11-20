package com.ggg.home.data.remote

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.ggg.common.utils.LiveDataCallAdapterFactory
import com.ggg.home.utils.ServerPath
import com.ggg.home.utils.TokenInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class HomeRetrofitProvider {

    private var builder: Retrofit.Builder
    private var client: OkHttpClient
    private var retrofit: Retrofit
    private var connectService: HomeService

    @Inject
    constructor (cache: Cache) {
        builder = Retrofit.Builder()
                .baseUrl(ServerPath.baseUri)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
        val logging = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Timber.tag("OkHttp").d(message)
            }
        })
        logging.level = HttpLoggingInterceptor.Level.HEADERS
        logging.level = HttpLoggingInterceptor.Level.BODY


        client = OkHttpClient.Builder()
//                .cache(cache)
                .addNetworkInterceptor(StethoInterceptor())
                .addInterceptor(logging)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor { chain ->
                    val original: Request = chain.request()
                    val request = original.newBuilder()
                            .method(original.method(), original.body())
                            .build()
                    return@addInterceptor chain.proceed(request)
//                    val url = original.url().url().toString()
//                    val loginApi = ServerPath.baseUri + ServerPath.LOGIN
//                    val googleApi = ServerPath.googleUri
//                    val requestBuilder = original.newBuilder()
                    //handle add authorization
//                    if (!url.equals(loginApi) && !url.contains(googleApi)) {
//                        if (PrefsUtil.instance.getToken().isNotEmpty()) {
//                            val token = PrefsUtil.instance.getToken()
//                            val userEmail = PrefsUtil.instance.getUserEmail()
//                            requestBuilder.addHeader("X-UserModel-Token", token)
//                            requestBuilder.addHeader("X-UserModel-Login", userEmail)
//                            requestBuilder.header("Content-type", "application/json")
//                        }
//                    }
//                    chain.proceed(requestBuilder.build())
                }
                .build()

        retrofit = builder.client(client).build()
        connectService = retrofit.create(HomeService::class.java)
    }

    /**
     * Get the actual retrofit
     */
    fun provide(): Retrofit {
        return retrofit
    }

    fun connectAPI(): HomeService {
        return connectService
    }

    fun setToken(token: String) {
        client = client.newBuilder().addInterceptor(TokenInterceptor(token)).build()
        retrofit = builder.client(client).build()
        connectService = retrofit.create(HomeService::class.java)
    }
}