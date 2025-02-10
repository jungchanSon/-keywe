package com.ssafy.keywe.domain.profile

data class GetAllProfileModel(
    val id: Long,
    val name: String,
    val phone: String?,
    val type: String
)

data class GetProfileDetailModel(
    val id: Long,
    val name: String,
    val phone: String?
)

data class PostProfileModel(
    val profileId: Long
)

data class PatchProfileModel(
    val name: String,
    val phone: String?,
    val password: String?
)