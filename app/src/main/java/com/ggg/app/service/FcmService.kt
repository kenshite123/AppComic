package com.ggg.app.service

import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.ggg.app.R
import com.ggg.home.utils.PrefsUtil
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber

class FcmService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val mBuilder = NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.icon_launcher)
                .setContentTitle(remoteMessage.notification!!.title.toString())
                .setContentText(remoteMessage.notification!!.body.toString())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        val notificationManager = NotificationManagerCompat.from(this)

        notificationManager.notify(0, mBuilder.build())
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.d("FCM_TOKEN: $token")
        PrefsUtil.instance.setUserFCMToken(token)
    }
}