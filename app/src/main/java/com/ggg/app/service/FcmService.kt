package com.ggg.app.service

import android.app.NotificationManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.ggg.app.R
import com.ggg.home.utils.PrefsUtil
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber
import android.app.NotificationChannel
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import android.app.PendingIntent
import android.content.Intent
import com.ggg.home.ui.main.MainActivity
import com.ggg.home.utils.Constant


class FcmService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(remoteMessage)
        } else {

            var isShowComicDetail = false
            var comicId = 0L
            for (entry in remoteMessage.data.entries) {
                if ("type" == entry.key && Constant.TYPE_SHOW_COMIC_DETAIL == entry.value) {
                    isShowComicDetail = true
                }

                if ("comicId" == entry.key) {
                    comicId = entry.value.toLong()
                }
            }

            val notificationIntent = Intent(applicationContext, MainActivity::class.java)
            notificationIntent.putExtra("isShowComicDetail", isShowComicDetail)
            notificationIntent.putExtra("comicId", comicId.toString())

            notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            val contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

            val mBuilder = NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.icon_launcher)
                    .setContentTitle(remoteMessage.notification!!.title.toString())
                    .setContentText(remoteMessage.notification!!.body.toString())
                    .setPriority(NotificationManager.IMPORTANCE_HIGH)
                    .setAutoCancel(true)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setContentIntent(contentIntent)

            val notificationManager = NotificationManagerCompat.from(this)

            notificationManager.notify(0, mBuilder.build())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(remoteMessage: RemoteMessage) {
        var isShowComicDetail = false
        var comicId = 0L
        for (entry in remoteMessage.data.entries) {
            if ("type" == entry.key && Constant.TYPE_SHOW_COMIC_DETAIL == entry.value) {
                isShowComicDetail = true
            }

            if ("comicId" == entry.key) {
                comicId = entry.value.toLong()
            }
        }

        val notificationIntent = Intent(applicationContext, MainActivity::class.java)
        notificationIntent.putExtra("isShowComicDetail", isShowComicDetail)
        notificationIntent.putExtra("comicId", comicId.toString())

        notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT)
//        val contentIntent = PendingIntent.getBroadcast(applicationContext, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT)

        val CHANNEL_ID = getString(R.string.default_notification_channel_id)// The id of the channel.
        val name = getString(R.string.default_notification_channel_id)// The user-visible name of the channel.
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
        val notification = NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.icon_launcher)
                .setContentTitle(remoteMessage.notification!!.title.toString())
                .setContentText(remoteMessage.notification!!.body.toString())
                .setChannelId(getString(R.string.default_notification_channel_id))
                .setContentIntent(contentIntent)
                .build()


        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.createNotificationChannel(mChannel)

        mNotificationManager.notify(0, notification)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.d("FCM_TOKEN: $token")
        PrefsUtil.instance.setUserFCMToken(token)
    }
}