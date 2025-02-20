package com.ssafy.keywe.data.fcm

import com.ssafy.keywe.data.dto.fcm.FCMRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path


interface FCMService {

    @POST(FCM_PATH)
    suspend fun registFCM(
        @Body request: FCMRequest,
    ): Response<Unit>

    @DELETE(FCM_DELETE_PATH)
    suspend fun deleteFCM(
        @Path("deviceId") deviceId: String,
    ): Response<Unit>


    companion object {

        private const val FCM_PATH = "/notification/tokens"
        private const val FCM_DELETE_PATH = "/notification/tokens/{deviceId}"
    }
}