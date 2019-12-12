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
import com.ggg.home.utils.PrefsUtil
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.media.AudioManager
import android.content.Context
import androidx.core.app.TaskStackBuilder
import com.ggg.app.ui.init.InitialActivity
import com.ggg.home.utils.Constant
import android.net.Uri


class FcmService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

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

//        val intent = Intent(this, Class.forName("com.ggg.home.ui.main.MainActivity"))
//        val intent = Intent(applicationContext, MainActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        }
//        intent.putExtra("isShowComicDetail", isShowComicDetail)
//        intent.putExtra("comicId", comicId)

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.journaldev.com/"))
        val pendingIntent = PendingIntent.getActivity(this, 15, intent, PendingIntent.FLAG_UPDATE_CURRENT)
//        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
//            // Add the intent, which inflates the back stack
//            addParentStack(InitialActivity::class.java)
//            addNextIntent(intent)
//            // Get the PendingIntent containing the entire back stack
//            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
//        }

        val mBuilder = NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.icon_launcher)
                .setContentTitle(remoteMessage.notification!!.title.toString())
                .setContentText(remoteMessage.notification!!.body.toString())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentIntent(pendingIntent)

//        beep(100)

        val notificationManager = NotificationManagerCompat.from(this)

        notificationManager.notify(0, mBuilder.build())
    }

    private fun beep(volume: Int) {
        val manager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        manager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0)

        val notification = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val player = MediaPlayer.create(applicationContext, notification)
        player.start()
    }

//    private fun setNotification(notificationMessage: String) {
//
//        //**add this line**
//        val requestID = System.currentTimeMillis().toInt()
//
//        val alarmSound = getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//        mNotificationManager = application.getSystemService(Context.NOTIFICATION_SERVICE)
//
//        val notificationIntent = Intent(applicationContext, NotificationActivity2::class.java)
//
//        //**add this line**
//        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//
//        //**edit this line to put requestID as requestCode**
//        val contentIntent = PendingIntent.getActivity(this, requestID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
//
//        val mBuilder = NotificationCompat.Builder(applicationContext)
//                .setSmallIcon(R.drawable.logo)
//                .setContentTitle("My Notification")
//                .setStyle(NotificationCompat.BigTextStyle()
//                        .bigText(notificationMessage))
//                .setContentText(notificationMessage).setAutoCancel(true)
//        mBuilder.setSound(alarmSound)
//        mBuilder.setContentIntent(contentIntent)
//        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build())
//
//    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.d("FCM_TOKEN: $token")
        PrefsUtil.instance.setUserFCMToken(token)
    }
}