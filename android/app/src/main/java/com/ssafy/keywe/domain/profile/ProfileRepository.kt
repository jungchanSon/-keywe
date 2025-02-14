package com.ssafy.keywe.domain.profile

import com.ssafy.keywe.data.ResponseResult
//import com.ssafy.keywe.data.dto.profile.GetProfileRequest
import com.ssafy.keywe.data.dto.profile.PostProfileRequest
import com.ssafy.keywe.data.dto.profile.UpdateProfileRequest

interface ProfileRepository {
    suspend fun getProfileList(): ResponseResult<List<GetProfileListModel>>
    suspend fun getProfileDetail(profileId: Long): ResponseResult<GetProfileDetailModel>
    suspend fun postProfile(postProfileRequest: PostProfileRequest): ResponseResult<PostProfileModel>
    suspend fun updateProfile(updateProfileRequest: UpdateProfileRequest): ResponseResult<UpdateProfileModel>
    suspend fun deleteProfile(profileId: Long): ResponseResult<Unit>

}