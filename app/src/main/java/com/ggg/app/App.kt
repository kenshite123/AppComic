package com.ggg.app

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.widget.Toast
import androidx.multidex.MultiDexApplication
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.facebook.stetho.Stetho
import com.ggg.app.di.AppInjector
import com.ggg.common.GGGAppInterface
import com.ggg.home.data.model.response.LoginResponse
import com.ggg.home.utils.PrefsUtil
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import io.vrinda.kotlinpermissions.DeviceInfo
import timber.log.Timber
import javax.inject.Inject


/**
 * Created by TuanNguyen on 12/12/17.
 */
class App : MultiDexApplication(), HasActivityInjector, GGGAppInterface.AppInterface {
    private lateinit var circularProgressDrawable: CircularProgressDrawable
    @Inject lateinit var dispatching: DispatchingAndroidInjector<Activity>
    var loginResponse: LoginResponse? = null
    private var listFavoriteId: List<String> = listOf()

    override fun getCtx(): Context {
        return this
    }
    override fun getCircularProgressDrawable(): CircularProgressDrawable {
        return this.circularProgressDrawable
    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return dispatching
    }

    override fun onCreate() {
        super.onCreate()

        /**
         * only log with debug and staging
         */
        if (BuildConfig.BUILD_TYPE.equals("debug") || BuildConfig.BUILD_TYPE.equals("staging")) {
            Timber.plant(Timber.DebugTree())
        }
        Stetho.initializeWithDefaults(this);
        GGGAppInterface.initInstance(this)
        AppInjector.init(this)
//        GlobalInfo.instance.mUserInfo = PrefsUtil.instance.getUserInfoObject()
        Stetho.initializeWithDefaults(this)
        DeviceInfo.getBuildBrand()
        DeviceInfo.getAppName(applicationContext)

        circularProgressDrawable = CircularProgressDrawable(this)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        var token = FirebaseInstanceId.getInstance().token
        if (token != null && token.isNotEmpty()){
            Timber.d("FCM_TOKEN: $token")
            PrefsUtil.instance.setUserFCMToken(token)
        }

        FirebaseMessaging.getInstance().subscribeToTopic("news")
                .addOnCompleteListener { task ->
                    var msg = "subscribed"
                    if (!task.isSuccessful) {
                        msg = "subscribe_failed"
                    }
                    Timber.d(msg)
                }

        val jsonLoginResponse = PrefsUtil.instance.getStringValue("LoginResponse", "")
        if (!jsonLoginResponse.isNullOrEmpty()) {
            this.loginResponse = Gson().fromJson<LoginResponse>(
                    jsonLoginResponse, object : TypeToken<LoginResponse>() {}.type)
        }

        val favorites = PrefsUtil.instance.getStringValue("favorites", "")
        if (!favorites.isNullOrEmpty()) {
            listFavoriteId = favorites.split(",")
        }
    }

    override fun getLoginResponse(): Any? {
        return this.loginResponse
    }

    override fun setLoginResponse(loginResponse: Any?) {
        this.loginResponse = loginResponse as LoginResponse
    }

    override fun checkIsLogin(): Boolean {
        return this.loginResponse != null
    }

    override fun addComicToFavorite(comicId: Long) {
        addComicToFavorite(comicId.toString())
    }

    override fun addComicToFavorite(comicId: String) {
        if (!listFavoriteId.contains(comicId)) {
            val s = listFavoriteId.toMutableList()
            s.add(comicId)

            listFavoriteId = s.toList()
            PrefsUtil.instance.setStringValue("favorites", TextUtils.join(",", listFavoriteId))
        }
    }

    override fun removeComicToFavorite(comicId: Long) {
        removeComicToFavorite(comicId.toString())
    }

    override fun removeComicToFavorite(comicId: String) {
        val s = listFavoriteId.toMutableList()
        s.remove(comicId)

        listFavoriteId = s.toList()
        PrefsUtil.instance.setStringValue("favorites", TextUtils.join(",", listFavoriteId))
    }

    override fun clearListComicFavorite() {
        val s = listFavoriteId.toMutableList()
        s.clear()
        listFavoriteId = s.toList()
    }

    override fun getListFavoriteId(): List<String> {
        return this.listFavoriteId
    }
}