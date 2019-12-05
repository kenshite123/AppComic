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
import com.ggg.home.utils.PrefsUtil
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.media.AudioManager
import android.content.Context


class FcmService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra("fragment", ComicDetailFragment.TAG)
        intent.putExtra("comicId", remoteMessage.notification!!.body.toString())
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        for (entry in remoteMessage.data.entries) {
            val key = entry.key
            val value = entry.value
            Timber.d("key: $key - value: $value")
        }

        val mBuilder = NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.icon_launcher)
                .setContentTitle(remoteMessage.notification!!.title.toString())
                .setContentText(remoteMessage.notification!!.body.toString())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        beep(100)

        val notificationManager = NotificationManagerCompat.from(this)

        notificationManager.notify(1, mBuilder.build())

    }

    private fun beep(volume: Int) {
        val manager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        manager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0)

        val notification = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val player = MediaPlayer.create(applicationContext, notification)
        player.start()
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.d("FCM_TOKEN: $token")
        PrefsUtil.instance.setUserFCMToken(token)
    }
}