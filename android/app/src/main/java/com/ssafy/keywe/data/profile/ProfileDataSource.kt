package com.ssafy.keywe.data.profile

import com.ssafy.keywe.data.ResponseResult
import com.ssafy.keywe.data.dto.profile.GetProfileDetailResponse
import com.ssafy.keywe.data.dto.profile.GetProfileListResponse
import com.ssafy.keywe.data.dto.profile.PostProfileRequest
import com.ssafy.keywe.data.dto.profile.PostProfileResponse
import com.ssafy.keywe.data.dto.profile.UpdateProfileRequest
import com.ssafy.keywe.data.dto.profile.UpdateProfileResponse

interface ProfileDataSource {
    suspend fun requestGetProfileList(): ResponseResult<List<GetProfileListResponse>>
    suspend fun requestGetProfileDetail(): ResponseResult<GetProfileDetailResponse>
    suspend fun requestPostProfile(postProfileRequest: PostProfileRequest): ResponseResult<PostProfileResponse>
    suspend fun requestUpdateProfile(updateProfileRequest: UpdateProfileRequest): ResponseResult<UpdateProfileResponse>
    suspend fun requestDeleteProfile(profileId: Long): ResponseResult<Unit>
}