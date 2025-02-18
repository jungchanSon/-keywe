package com.ssafy.keywe.data.dto.profile

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//@Serializable
//data class ProfileResponse(
//    @SerialName("statusCode") val statusCode: Int,
//    @SerialName("message") val message: String,
//    @SerialName("data") val data:
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
data class GetProfileListResponse(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("role") val role: String,
    @SerialName("image") val image: String
)

@Serializable
data class GetProfileDetailResponse(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("role") val role: String,
    @SerialName("phone") val phone: String?,
    @SerialName("image") val image: String
)

@Serializable
data class PostProfileResponse(
    @SerialName("id") val id: String,
    @SerialName("role") val role: String,
    @SerialName("createAt") val createAt: String
)

@Serializable
data class UpdateProfileResponse(
    @SerialName("role") val role: String,
    @SerialName("name") val name: String,
    @SerialName("phone") val phone: String?,
    @SerialName("password") val password: String?
)

@Serializable
data class SmsResponse(
    val message: String
)