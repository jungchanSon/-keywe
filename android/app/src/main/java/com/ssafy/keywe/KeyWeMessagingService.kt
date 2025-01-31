package com.ssafy.keywe

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random


class KeyWeMessagingService : FirebaseMessagingService() {

    private val random = Random

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.let { message ->
            sendNotification(message, 1)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterialApi::class)
    private fun sendNotification(notification: RemoteMessage.Notification, count: Int) {
        val intent = Intent(
            this, MainActivity::class.java
        )
        intent.putExtra("count", count.toString())

        val requestCode = System.currentTimeMillis().toInt()
        val pendingIntent = PendingIntent.getActivity(
            this,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )


        val channelId = "FCMDemoChannel"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, channelId).setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(notification.title).setStyle(
                    NotificationCompat.BigTextStyle().bigText(notification.body)
                ).setShowWhen(true).setWhen(System.currentTimeMillis()).setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC).setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            channelId, notification.title, NotificationManager.IMPORTANCE_HIGH
        )

        channel.setShowBadge(true)
        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        manager.createNotificationChannel(channel)

        val notificationId = System.currentTimeMillis().toInt()
        manager.notify(notificationId, builder.build())
    }

//    @OptIn(ExperimentalMaterialApi::class)
//    private fun sendNotification(message: RemoteMessage.Notification) {
//        // If you want the notifications to appear when your app is in foreground
//
//        val intent = Intent(this, MainActivity::class.java).apply {
//            addFlags(FLAG_ACTIVITY_CLEAR_TOP)
//        }
//
//        val pendingIntent = PendingIntent.getActivity(
//            this, 0, intent, FLAG_IMMUTABLE
//        )
//
//        val channelId = this.getString(R.string.app_name)
//
//        val notificationBuilder =
//            NotificationCompat.Builder(this, channelId).setContentTitle(message.title)
//                .setContentText(message.body).setSmallIcon(R.mipmap.ic_launcher).setAutoCancel(true)
//                .setContentIntent(pendingIntent)
//
//        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(channelId, CHANNEL_NAME, IMPORTANCE_DEFAULT)
//            manager.createNotificationChannel(channel)
//        }
//
//        manager.notify(random.nextInt(), notificationBuilder.build())
//    }

    @SuppressLint("ResourceType")
    override fun onNewToken(token: String) {

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        Log.d("FCM", "New token: $token")
    }

    companion object {
        const val CHANNEL_NAME = "FCM notification channel"
    }
}