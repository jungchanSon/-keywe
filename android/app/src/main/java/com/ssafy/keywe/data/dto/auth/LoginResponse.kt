package com.ssafy.keywe.data.dto.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/*
    "status_code": 200,
    "message": "OK",
    "data": {
        "id": 3,
        "email": "testuser2@makeittakeit.kr",
        "nickname": "testuser2",
        "signup_method": "email",
        "token": {
            "type": "Bearer",
            "access": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0b2tlbl90eXBlIjoiYWNjZXNzIiwiZXhwIjoxNzM3NTY0MDgwLCJpYXQiOjE3Mzc1NDk2ODAsImp0aSI6ImFhN2I2ZjMzNjYyZjRhNzJiZWM1MDczODdhODY1YWQxIiwidXNlcl9pZCI6M30.rGpIATTCuAW1XH_qS67uMDxWb5SDUDaGTDOtyxjRcEM",
            "refresh": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0b2tlbl90eXBlIjoicmVmcmVzaCIsImV4cCI6MTczODc1OTI4MCwiaWF0IjoxNzM3NTQ5NjgwLCJqdGkiOiJjZGQ3ZTM1MGFlZmM0NjljYjlmOGEzYThiOTgzNTc0YyIsInVzZXJfaWQiOjN9.fDs1RGVA-YrlI9r7HkCgz03RyDa0Q7bs3fTd6hV0GGQ"
        }
    }
 */
@Serializable
data class KeyWeResponse<T>(
    @SerialName("status_code") val statusCode: Int,
    @SerialName("message") val message: String,
    @SerialName("data") val data: T?,
)

@Serializable
data class LoginResponse(
    @SerialName("accessToken") val accessToken: String,
    @SerialName("emailVerified") val emailVerified: Boolean,
    @SerialName("role") val role: String
)

@Serializable
data class CEOLoginResponse(
    @SerialName("accessToken") val accessToken: String,
    @SerialName("refreshToken") val refreshToken: String,
)

@Serializable
data class SelectProfileResponse(
    @SerialName("accessToken") val accessToken: String,
    // todo familyId 추가
//    @SerialName("refreshToken") val refreshToken: String
)

@Serializable
data class TokenResponse(
    @SerialName("type") val type: String,
    @SerialName("access") val access: String,
    @SerialName("refresh") val refresh: String,
)