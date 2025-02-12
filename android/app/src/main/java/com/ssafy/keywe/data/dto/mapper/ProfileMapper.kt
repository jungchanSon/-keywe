package com.ssafy.keywe.data.dto.mapper

//import com.ssafy.keywe.data.dto.profile.PatchProfileResponse
import com.ssafy.keywe.data.dto.auth.SelectProfileResponse
import com.ssafy.keywe.data.dto.profile.GetAllProfileResponse
import com.ssafy.keywe.data.dto.profile.GetProfileDetailResponse
import com.ssafy.keywe.data.dto.profile.PatchProfileResponse
import com.ssafy.keywe.data.dto.profile.PostProfileResponse
import com.ssafy.keywe.domain.auth.SelectProfileModel
import com.ssafy.keywe.domain.profile.GetAllProfileModel
import com.ssafy.keywe.domain.profile.GetProfileDetailModel
import com.ssafy.keywe.domain.profile.PatchProfileModel
import com.ssafy.keywe.domain.profile.PostProfileModel

fun GetAllProfileResponse.toDomain(): GetAllProfileModel = GetAllProfileModel(
    id = this.id, name = this.name,
//    role = this.role,
//    profileImage = this.profileImage,
    phone = this.phone ?: "", type = this.type
)

fun GetProfileDetailResponse.toDomain(): GetProfileDetailModel = GetProfileDetailModel(
    id = this.id, name = this.name, phone = this.phone ?: ""
)

fun PostProfileResponse.toDomain(): PostProfileModel = PostProfileModel(
    profileId = this.profileId
)

fun PatchProfileResponse.toDomain(): PatchProfileModel = PatchProfileModel(
    name = this.name,
    phone = this.phone,
    password = this.simplePassword,
//    role = this.role
)

fun SelectProfileResponse.toDomain(): SelectProfileModel = SelectProfileModel(
    accessToken = this.accessToken,
//    refreshToken = this.refreshToken,
)
