package com.ggg.app.service

import androidx.core.app.NotificationCompat
import com.ggg.app.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber
import android.app.PendingIntent
import android.content.Intent
import com.ggg.home.ui.main.MainActivity
import androidx.core.app.NotificationManagerCompat
import com.ggg.home.ui.comic_detail.ComicDetailFragment


class FcmService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
//        Timber.d("From: " + remoteMessage!!.from!!)
//
//        if (remoteMessage.data.size > 0) {
//            Timber.d("Message data payload: " + remoteMessage.data)
//        }
//
//        if (remoteMessage.notification != null) {
//            Timber.d("Message Notification Title: " + remoteMessage.notification!!.title!!)
//            Timber.d("Message Notification Body: " + remoteMessage.notification!!.body!!)
//        }

        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra("fragment", ComicDetailFragment.TAG)
        intent.putExtra("comicId", remoteMessage.notification!!.body.toString())
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val mBuilder = NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.icon_launcher)
                .setContentTitle(remoteMessage.notification!!.title.toString())
                .setContentText(remoteMessage.notification!!.body.toString())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        val notificationManager = NotificationManagerCompat.from(this)

        notificationManager.notify(1, mBuilder.build())

    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

    }
}