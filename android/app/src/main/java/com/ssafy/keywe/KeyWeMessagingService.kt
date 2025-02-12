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
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ssafy.keywe.domain.fcm.FCMNotificationModel
import kotlin.random.Random


class KeyWeMessagingService : FirebaseMessagingService() {

    private val random = Random

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("FCM notification", "${remoteMessage.notification?.title}")
        Log.d("FCM notification", "${remoteMessage.notification?.body}")
        Log.d("FCM notification", "${remoteMessage.data}")



        if (remoteMessage.data.isNotEmpty()) {
            val storeId = remoteMessage.data["storeId"]
            val kioskUserId = remoteMessage.data["kioskUserId"]
            val sessionId = remoteMessage.data["sessionId"]
            Log.d(remoteMessage.data.toString(), "Message data payload: ${remoteMessage.data}")

            val title = remoteMessage.notification?.title ?: "No Title"
            val body = remoteMessage.notification?.body ?: "No Body"
//            val count = remoteMessage.data["count"]?.toIntOrNull() ?: 0
            if (storeId != null && kioskUserId != null && sessionId != null) sendNotification(
                title, body, FCMNotificationModel(storeId, kioskUserId, sessionId)
            )
        }

//        remoteMessage.notification?.let { message ->
//            sendNotification(message, 1)
//        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterialApi::class)
    private fun sendNotification(title: String, body: String, notification: FCMNotificationModel) {
        val channelId = "FCM_Channel"
        val notificationId = System.currentTimeMillis().toInt()

        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            putExtra("notification", notification)
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder =
            NotificationCompat.Builder(this, channelId).setSmallIcon(R.mipmap.sym_def_app_icon)
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

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterialApi::class)
    override fun handleIntent(intent: Intent) {
        //add a log, and you'll see the method will be triggered all the time (both foreground and background).
        Log.d("FCM", "handleIntent")

        //if you don't know the format of your FCM message,
        //just print it out, and you'll know how to parse it
        val bundle = intent.extras
        if (bundle != null) {
            for (key in bundle.keySet()) {
                val value = bundle[key]
                Log.d("FCM", "Key: $key Value: $value")
            }
        }

        //the background notification is created by super method
        //but you can't remove the super method.
        //the super method do other things, not just creating the notification
        super.handleIntent(intent)

        //remove the Notificaitons
        removeFirebaseOrigianlNotificaitons()

        if (bundle == null) return

        //pares the message
//        val cloudMsg: CloudMsg = parseCloudMsg(bundle)

        //if you want take the data to Activity, set it

        sendNotification("title", "desc", FCMNotificationModel("1", "2", "3"))
    }


    /**
     * parse the message which is from FCM
     * @param bundle
     */
    private fun parseCloudMsg(bundle: Bundle): FCMNotificationModel {
        var title: String? = null
        var msg: String? = null

        //if the message is sent from Firebase platform, the key will be that
        msg = bundle["gcm.notification.body"] as String?

        if (bundle.containsKey("gcm.notification.title")) title =
            bundle["gcm.notification.title"] as String?

        //parse your custom message
        var testValue: String? = null
        testValue = bundle["testKey"] as String?

        //package them into a object(CloudMsg is your own structure), it is easy to send to Activity.
//        val cloudMsg: CloudMsg = FCMNotificationModel(title, msg, testValue)
        return FCMNotificationModel("1", "2", "3")
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
            //notificationManager.cancelAll();
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