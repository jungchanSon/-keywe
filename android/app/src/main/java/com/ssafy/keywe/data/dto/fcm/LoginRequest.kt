package com.ssafy.keywe.data.dto.fcm

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class FCMRequest(
    @SerialName("token") val token: String,
    @SerialName("deviceId") val deviceId: String,
)

