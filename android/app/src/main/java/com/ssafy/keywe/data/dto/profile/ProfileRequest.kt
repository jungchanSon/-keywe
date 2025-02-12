package com.ssafy.keywe.data.dto.profile

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//@Serializable
//data class UpdateProfileRequest(
//    @SerialName("name") val name: String,
//    @SerialName("phone") val phone: String,
//    @SerialName("simplePassword") val simplePassword: String
//)

@Serializable
data class PostProfileRequest(
    @SerialName("role") val role: String,
    @SerialName("name") val name: String,
    @SerialName("phone") val phone: String?,
    @SerialName("password") val password: String?
//    @SerialName("password") val password: Int?
)

@Serializable
data class UpdateProfileRequest(
    @SerialName("name") val name: String,
    @SerialName("phone") val phone: String?,
    @SerialName("password") val password: String?
)

