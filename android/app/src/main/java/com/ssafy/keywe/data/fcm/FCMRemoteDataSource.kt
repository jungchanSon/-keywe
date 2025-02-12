package com.ssafy.keywe.data.fcm

import com.ssafy.keywe.data.ApiResponseHandler.handleApiResponse
import com.ssafy.keywe.data.ResponseResult
import com.ssafy.keywe.data.dto.fcm.FCMRequest
import com.ssafy.keywe.data.dto.fcm.FCMResponse
import javax.inject.Inject

class FCMRemoteDataSource @Inject constructor(private val fcmService: FCMService) : FCMDataSource {
    override suspend fun registFCM(request: FCMRequest): ResponseResult<FCMResponse> =
        handleApiResponse {
            fcmService.registFCM(request)
        }

}