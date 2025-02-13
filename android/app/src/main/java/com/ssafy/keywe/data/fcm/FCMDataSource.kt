package com.ssafy.keywe.data.fcm

import com.ssafy.keywe.data.ResponseResult
import com.ssafy.keywe.data.dto.fcm.FCMRequest

interface FCMDataSource {
    suspend fun registFCM(request: FCMRequest): ResponseResult<Unit>
}