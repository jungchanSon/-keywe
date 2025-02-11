package com.ssafy.keywe.domain.profile

import com.ssafy.keywe.data.dto.profile.RoleType

// 바꿔
data class GetAllProfileModel(
    val id: Long, val name: String, val phone: String?, val type: RoleType
)
//
//"id": number,
//"name": string,
//"role": "PARENT" || "CHILD",
//"profileImage": String | null,

data class GetProfileDetailModel(
    val id: Long, val name: String, val phone: String?
)

data class PostProfileModel(
    val profileId: Long
)

data class PatchProfileModel(
    val name: String, val phone: String?, val password: String?
)

data class ProfileSelectModel(
    val accessToken: String, val refreshToken: String
)