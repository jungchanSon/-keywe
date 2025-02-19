package com.ssafy.keywe

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.Constants.MessageNotificationKeys
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ssafy.keywe.domain.fcm.FCMNotificationModel
import com.ssafy.keywe.domain.fcm.NotificationData
import kotlin.random.Random


class KeyWeMessagingService : FirebaseMessagingService() {

    private val random = Random

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("FCM notification", "onMessageReceived: ${remoteMessage.notification?.title}")
        Log.d("FCM notification", "onMessageReceived: ${remoteMessage.notification?.body}")
        Log.d("FCM notification", "onMessageReceived: ${remoteMessage.data}")


        if (remoteMessage.data.isNotEmpty()) {
            val storeId = remoteMessage.data["storeId"]
            val kioskUserId = remoteMessage.data["kioskUserId"]
            val sessionId = remoteMessage.data["sessionId"]
            Log.d(remoteMessage.data.toString(), "Message data payload: ${remoteMessage.data}")
            val title = remoteMessage.data["title"] ?: "대리 주문 도움 요청"
            val body = remoteMessage.data["body"] ?: "키오스크 주문 도움이 필요합니다."

//            val title = remoteMessage.notification?.title ?: "대리 주문 도움 요청"
//            val body = remoteMessage.notification?.body ?: "키오스크 주문 도움이 필요합니다."
//            val count = remoteMessage.data["count"]?.toIntOrNull() ?: 0
            if (storeId != null && kioskUserId != null && sessionId != null) {
                sendNotification(
                    FCMNotificationModel(
                        title, body, NotificationData(storeId, kioskUserId, sessionId)
                    )
                )
            }


        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterialApi::class)
    private fun sendNotification(notification: FCMNotificationModel) {
        val channelId = "FCM_Channel"
        val notificationId = System.currentTimeMillis().toInt()

        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            putExtra("notification", notification.data)
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder =
            NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.keywe_logo_white_24x24)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.keywe_app_logo))
                .setContentTitle(notification.title).setContentText(notification.body)
                .setStyle(NotificationCompat.BigTextStyle().bigText(notification.body))
                .setAutoCancel(true).setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC).setSound(defaultSoundUri)
                .setColor(0xFFEE5B22.toInt())
                .setContentIntent(pendingIntent)

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel =
            NotificationChannel(channelId, "FCM Channel", NotificationManager.IMPORTANCE_HIGH)
        manager.createNotificationChannel(channel)

        manager.notify(notificationId, builder.build())
    }

    fun isNotification(data: Bundle): Boolean {
        return "1" == data.getString(MessageNotificationKeys.ENABLE_NOTIFICATION) || ("1" == data.getString(
            keyWithOldPrefix(MessageNotificationKeys.ENABLE_NOTIFICATION)
        ))
    }

    private fun keyWithOldPrefix(key: String): String {
        if (!key.startsWith(MessageNotificationKeys.NOTIFICATION_PREFIX)) {
            return key
        }

        return key.replace(
            MessageNotificationKeys.NOTIFICATION_PREFIX,
            MessageNotificationKeys.NOTIFICATION_PREFIX_OLD
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun handleIntent(intent: Intent) {
        //add a log, and you'll see the method will be triggered all the time (both foreground and background).
        Log.d("FCM", "handleIntent")

        //if you don't know the format of your FCM message,
        //just print it out, and you'll know how to parse it

        val new = intent?.apply {
            val temp = extras?.apply {
                remove(MessageNotificationKeys.ENABLE_NOTIFICATION)
                remove(keyWithOldPrefix(MessageNotificationKeys.ENABLE_NOTIFICATION))
            }
            replaceExtras(temp)
        }
        super.handleIntent(new)

    }


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