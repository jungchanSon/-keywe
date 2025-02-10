package com.ssafy.keywe.data.dto.profile

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//@Serializable
//data class ProfileResponse(
//    @SerialName("statusCode") val statusCode: Int,
//    @SerialName("message") val message: String,
//    @SerialName("data") val data: ProfileData
//)
//
//@Serializable
//data class ProfileData(
//    @SerialName("userId") val userId: String,
//    @SerialName("name") val name: String,
//    @SerialName("phone") val phone: String,
//    @SerialName("profileImage") val profile: String?,
//    @SerialName("role") val role: String,
//    @SerialName("simplePassword") val simplePassword: String
//)

@Serializable
data class GetAllProfileResponse(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("phone") val phone: String?,
    @SerialName("type") val type: String
)

@Serializable
data class GetProfileDetailResponse(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("phone") val phone: String?
)

@Serializable
data class PostProfileResponse(
    @SerialName("profileId") val profileId: Long
)

@Serializable
data class PatchProfileResponse(
    @SerialName("name") val name: String,
    @SerialName("phone") val phone: String?,
    @SerialName("password") val password: String?
)
