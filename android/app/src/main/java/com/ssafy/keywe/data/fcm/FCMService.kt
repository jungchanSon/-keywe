package com.ssafy.keywe.data.fcm

import com.ssafy.keywe.data.dto.fcm.FCMRequest
import com.ssafy.keywe.data.dto.fcm.FCMResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface FCMService {

    @POST(FCM_PATH)
    suspend fun registFCM(
        @Body request: FCMRequest,
    ): Response<FCMResponse>


    companion object {
        private const val FCM_PATH = "/remote/fcm"
    }
}