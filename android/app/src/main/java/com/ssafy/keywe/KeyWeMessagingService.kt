package com.ssafy.keywe

import android.R
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.Constants
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

            val title = remoteMessage.notification?.title ?: "No Title"
            val body = remoteMessage.notification?.body ?: "No Body"
//            val count = remoteMessage.data["count"]?.toIntOrNull() ?: 0
            if (storeId != null && kioskUserId != null && sessionId != null) {
                Log.d("FCM notification", "send Message")
                sendNotification(
                    FCMNotificationModel(
                        title, body, NotificationData(storeId, kioskUserId, sessionId)
                    )
                )
            }


        }

//        val fcmMsg: FCMNotificationModel = parseFCMMsg(bundle)
//        remoteMessage.notification?.let { message ->
//            sendNotification(fcmMsg)
//        }
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
            NotificationCompat.Builder(this, channelId).setSmallIcon(R.mipmap.sym_def_app_icon)
                .setContentTitle(notification.title).setContentText(notification.body)
                .setStyle(NotificationCompat.BigTextStyle().bigText(notification.body))
                .setAutoCancel(true).setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC).setSound(defaultSoundUri)
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
    @OptIn(ExperimentalMaterialApi::class)
    override fun handleIntent(intent: Intent) {
        //add a log, and you'll see the method will be triggered all the time (both foreground and background).
        Log.d("FCM", "handleIntent")

        //if you don't know the format of your FCM message,
        //just print it out, and you'll know how to parse it
        val bundle = intent.extras


        val new = intent?.apply {
            val temp = extras?.apply {
                remove(Constants.MessageNotificationKeys.ENABLE_NOTIFICATION)
                remove(keyWithOldPrefix(Constants.MessageNotificationKeys.ENABLE_NOTIFICATION))
            }
            replaceExtras(temp)
        }
        super.handleIntent(new)

        //the background notification is created by super method
        //but you can't remove the super method.
        //the super method do other things, not just creating the notification
//        super.handleIntent(intent)

//        //remove the Notificaitons
//        removeFirebaseOrigianlNotificaitons()
//
//        if (bundle == null) return
//
//        //pares the message
//        val fcmMsg: FCMNotificationModel = parseFCMMsg(bundle)
//
//        //if you want take the data to Activity, set it
//
//        sendNotification(fcmMsg)
    }


    /**
     * parse the message which is from FCM
     * @param bundle
     */
    private fun parseFCMMsg(bundle: Bundle): FCMNotificationModel {
        var title: String = "키위 요청"
        var body: String? = null

        //if the message is sent from Firebase platform, the key will be that
        body = bundle["gcm.notification.body"] as String? ?: "대리주문 요청이 왔습니다."

        if (bundle.containsKey("gcm.notification.title")) title =
            bundle["gcm.notification.title"] as String ?: "키위 요청"

        // 알림 내용 확인용
//        if (bundle != null) {
//            for (key in bundle.keySet()) {
//                val value = bundle[key]
//                Log.d("FCM", "Key: $key Value: $value")
//            }
//        }
        val storeId = bundle["storeId"] as String
        val kioskUserId = bundle["kioskUserId"] as String
        val sessionId = bundle["sessionId"] as String


        //package them into a object(CloudMsg is your own structure), it is easy to send to Activity.
        return FCMNotificationModel(title, body, NotificationData(storeId, kioskUserId, sessionId))
    }


    /**
     * remove the notification created by "super.handleIntent(intent)"
     */
    private fun removeFirebaseOrigianlNotificaitons() {
        //check notificationManager is available

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager ?: return

        //check api level for getActiveNotifications()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            //if your Build version is less than android 6.0
            //we can remove all notifications instead.
            notificationManager.cancelAll();
            return
        }

        //check there are notifications
        val activeNotifications = notificationManager.activeNotifications ?: return

        //remove all notification created by library(super.handleIntent(intent))
        for (tmp in activeNotifications) {
            Log.d(
                "FCM StatusBarNotification", "tag/id: " + tmp.tag + " / " + tmp.id
            )
            val tag = tmp.tag
            val id = tmp.id

            //trace the library source code, follow the rule to remove it.
            if (tag != null && tag.contains("FCM-Notification")) notificationManager.cancel(tag, id)
        }
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