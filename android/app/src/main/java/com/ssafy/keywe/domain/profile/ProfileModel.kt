package com.ssafy.keywe.domain.profile

data class GetProfileListModel(
    val id: String,
    val name: String,
    val role: String,
    val image: String?
)

data class GetProfileDetailModel(
    val id: String,
    val name: String,
    val role: String?,
    val phone: String?,
    val image: String?
)

data class PostProfileModel(
    val id: String,
    val role: String,
    val createAt: String
)

data class UpdateProfileModel(
    val role: String,
    val name: String,
    val phone: String?,
    val password: String?
)

// 안씀
//data class ProfileSelectModel(
//    val accessToken: String, val refreshToken: String
//)