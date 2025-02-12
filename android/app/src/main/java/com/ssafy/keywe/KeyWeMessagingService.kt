package com.ssafy.keywe

import android.annotation.SuppressLint
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
        Log.d("FCM notification", "${remoteMessage.notification}")
        Log.d("FCM data", "${remoteMessage.data}")


        if (remoteMessage.data.isNotEmpty()) {
            val title = remoteMessage.data["title"] ?: "No Title"
            val body = remoteMessage.data["body"] ?: "No Body"
            val count = remoteMessage.data["count"]?.toIntOrNull() ?: 0
            sendNotification(title, body, count)
        }

//        remoteMessage.notification?.let { message ->
//            sendNotification(message, 1)
//        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterialApi::class)
    private fun sendNotification(title: String, body: String, count: Int) {
        val channelId = "FCM_Channel"
        val notificationId = System.currentTimeMillis().toInt()

        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            putExtra("count", count.toString())
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(this, channelId).setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title).setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body)).setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC).setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel =
            NotificationChannel(channelId, "FCM Channel", NotificationManager.IMPORTANCE_HIGH)
        manager.createNotificationChannel(channel)

        manager.notify(notificationId, builder.build())
    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    @OptIn(ExperimentalMaterialApi::class)
//    private fun sendNotification(notification: RemoteMessage.Notification, count: Int) {
//        val intent = Intent(
//            this, MainActivity::class.java
//        )
//        intent.putExtra("count", count.toString())
//
//        val requestCode = System.currentTimeMillis().toInt()
//        val pendingIntent = PendingIntent.getActivity(
//            this,
//            requestCode,
//            intent,
//            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
//        )
//        Log.d("FCM", "sendNotification: $notification")
//
//        val channelId = "FCMDemoChannel"
//        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//        val builder: NotificationCompat.Builder =
//            NotificationCompat.Builder(this, channelId).setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle(notification.title).setStyle(
//                    NotificationCompat.BigTextStyle().bigText(notification.body)
//                ).setShowWhen(true).setWhen(System.currentTimeMillis()).setAutoCancel(true)
//                .setDefaults(NotificationCompat.DEFAULT_ALL)
//                .setPriority(NotificationCompat.PRIORITY_MAX)
//                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC).setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent)
//        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//
//        val channel = NotificationChannel(
//            channelId, notification.title, NotificationManager.IMPORTANCE_HIGH
//        )
//
//        channel.setShowBadge(true)
//        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
//        manager.createNotificationChannel(channel)
//
//        val notificationId = System.currentTimeMillis().toInt()
//        manager.notify(notificationId, builder.build())
//    }


    @SuppressLint("ResourceType")
    override fun onNewToken(token: String) {

        PushNotificationManager.updateToken(token)
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        Log.d("FCM", "New token: $token")
    }

    companion object {
        const val CHANNEL_NAME = "FCM notification channel"
    }
}