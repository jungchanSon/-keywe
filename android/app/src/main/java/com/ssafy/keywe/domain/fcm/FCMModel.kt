package com.ssafy.keywe.domain.fcm

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

class FCMModel {

}

@Parcelize
data class FCMNotificationModel(
    val title: String,
    val body: String,
    val data: NotificationData,
) : Parcelable

@Parcelize
data class NotificationData(
    val storeId: String,
    val kioskUserId: String,
    val sessionId: String,
) : Parcelable