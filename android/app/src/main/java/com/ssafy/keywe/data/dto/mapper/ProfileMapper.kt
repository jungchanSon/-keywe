package com.ssafy.keywe.data.dto.mapper

//import com.ssafy.keywe.data.dto.profile.PatchProfileResponse
import com.ssafy.keywe.R
import com.ssafy.keywe.data.dto.auth.SelectProfileResponse
import com.ssafy.keywe.data.dto.profile.GetProfileDetailResponse
import com.ssafy.keywe.data.dto.profile.GetProfileListResponse
import com.ssafy.keywe.data.dto.profile.PostProfileResponse
import com.ssafy.keywe.data.dto.profile.UpdateProfileResponse
import com.ssafy.keywe.domain.auth.SelectProfileModel
import com.ssafy.keywe.domain.profile.GetProfileDetailModel
import com.ssafy.keywe.domain.profile.GetProfileListModel
import com.ssafy.keywe.domain.profile.PostProfileModel
import com.ssafy.keywe.domain.profile.UpdateProfileModel

fun GetProfileListResponse.toDomain(): GetProfileListModel = GetProfileListModel(
    id = this.id,
    name = this.name,
    role = this.role,
    image = this.image ?: R.drawable.humanimage.toString()
)

fun GetProfileDetailResponse.toDomain(): GetProfileDetailModel = GetProfileDetailModel(
    id = this.id,
    name = this.name,
    role = this.role,
    phone = this.phone ?: "",
    image = this.image ?: R.drawable.humanimage.toString()
)

fun PostProfileResponse.toDomain(): PostProfileModel = PostProfileModel(
    id = this.id,
    role = this.role,
    createAt = this.createAt
)

fun UpdateProfileResponse.toDomain(): UpdateProfileModel = UpdateProfileModel(
    role = this.role,
    name = this.name,
    phone = this.phone,
    password = this.password,
)

fun SelectProfileResponse.toDomain(): SelectProfileModel = SelectProfileModel(
    accessToken = this.accessToken,
//    refreshToken = this.refreshToken,
)
