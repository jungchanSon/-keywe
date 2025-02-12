package com.ssafy.keywe.domain.fcm

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

class FCMModel {

}

@Parcelize
data class FCMNotificationModel(
    val storeId: String,
    val kioskUserId: String,
    val sessionId: String,
) : Parcelable