package com.ssafy.keywe.data.fcm

import com.ssafy.keywe.data.ApiResponseHandler.handleApiResponse
import com.ssafy.keywe.data.ResponseResult
import com.ssafy.keywe.data.dto.fcm.FCMRequest
import javax.inject.Inject

class FCMRemoteDataSource @Inject constructor(private val fcmService: FCMService) : FCMDataSource {
    override suspend fun registFCM(request: FCMRequest): ResponseResult<Unit> = handleApiResponse {
        fcmService.registFCM(request)
    }

    override suspend fun deleteFCM(deviceId: String): ResponseResult<Unit> = handleApiResponse {
        fcmService.deleteFCM(deviceId)
    }
}