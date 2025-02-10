package com.ssafy.keywe.data.dto.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    @SerialName("email")
    val email: String,
    @SerialName("password")
    val password: String,
)

@Serializable
data class CEOLoginRequest(
    @SerialName("id") val id: Long,
    @SerialName("password") val password: String,
)

@Serializable
data class SelectProfileRequest(
    @SerialName("profileId") val profileId: Long
)