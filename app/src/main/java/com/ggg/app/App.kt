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
import com.ggg.common.utils.RxBus
import com.ggg.home.data.model.ConfigModel
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
    private var siteDeploy = false
    private var listFavoriteId: List<String> = listOf()
    private var listDownloadedId: List<String> = listOf()
    private var isFromNotification = false
    private lateinit var bus: RxBus

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

        bus = RxBus()
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

        val downloaded = PrefsUtil.instance.getStringValue("downloaded", "")
        if (!downloaded.isNullOrEmpty()) {
            listDownloadedId = downloaded.split(",")
        }
    }

    override fun getLoginResponse(): Any? {
        return this.loginResponse
    }

    override fun setLoginResponse(loginResponse: Any?) {
        if (loginResponse == null) {
            this.loginResponse = null
            PrefsUtil.instance.setStringValue("LoginResponse", "")
        } else {
            this.loginResponse = loginResponse as LoginResponse
            PrefsUtil.instance.setStringValue("LoginResponse", this.loginResponse!!.convertToGson())
        }
    }

    override fun checkIsLogin(): Boolean {
        return this.loginResponse != null && this.loginResponse!!.accessToken.isNotEmpty()
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
        if (s.isNotEmpty()) {
            s.remove(comicId)

            listFavoriteId = s.toList()
            PrefsUtil.instance.setStringValue("favorites", TextUtils.join(",", listFavoriteId))
        }
    }

    override fun clearListComicFavorite() {
        listFavoriteId = listOf()
    }

    override fun getListFavoriteId(): List<String> {
        return this.listFavoriteId
    }

    override fun addComicToDownloaded(comicId: Long) {
        addComicToDownloaded(comicId.toString())
    }

    override fun addComicToDownloaded(comicId: String) {
        if (!listDownloadedId.contains(comicId)) {
            val s = listDownloadedId.toMutableList()
            s.add(comicId)

            listDownloadedId = s.toList()
            PrefsUtil.instance.setStringValue("downloaded", TextUtils.join(",", listDownloadedId))
        }
    }

    override fun removeComicToDownloaded(comicId: Long) {
        removeComicToDownloaded(comicId.toString())
    }

    override fun removeComicToDownloaded(comicId: String) {
        val s = listDownloadedId.toMutableList()
        if (s.isNotEmpty()) {
            s.remove(comicId)

            listDownloadedId = s.toList()
            PrefsUtil.instance.setStringValue("downloaded", TextUtils.join(",", listDownloadedId))
        }
    }

    override fun clearListComicDownloaded() {
        listDownloadedId = listOf()
    }

    override fun getListDownloadedId(): List<String> {
        return this.listDownloadedId
    }

    override fun setFromNotification(isFromNotification: Boolean) {
        this.isFromNotification = isFromNotification
    }

    override fun isFromNotification(): Boolean {
        return this.isFromNotification
    }

    override fun setSiteDeploy(siteDeploy: Boolean) {
        this.siteDeploy = siteDeploy
    }

    override fun getSiteDeploy(): Boolean {
        return this.siteDeploy
    }

    override fun bus(): RxBus {
        return bus
    }
}