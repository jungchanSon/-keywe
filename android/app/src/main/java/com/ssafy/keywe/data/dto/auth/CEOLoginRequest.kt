package com.ssafy.keywe.data.dto.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CEOLoginRequest(
    @SerialName("id")
    val id: String,
    @SerialName("password")
    val password: String,
)