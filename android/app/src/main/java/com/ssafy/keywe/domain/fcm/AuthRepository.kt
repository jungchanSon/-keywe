package com.ssafy.keywe.domain.fcm

import com.ssafy.keywe.data.ResponseResult
import com.ssafy.keywe.data.dto.fcm.FCMRequest

interface FCMRepository {
    suspend fun registFCM(request: FCMRequest): ResponseResult<Unit>
    suspend fun deleteFCM(deviceId: String): ResponseResult<Unit>
}