package com.ssafy.keywe.domain.fcm

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

class FCMModel {

}

/*
    "title": "대리 주문 도움 요청",
    "body": "정다경님이 키오스크 주문 도움을 요청했습니다",
    "sessionId": "세션 식별자",
    "storeId": "가게 식별자",
    "kioskUserId": "요청자 회원 식별자",
    "kioskUserName": "요청자 회원 이름"
 */

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