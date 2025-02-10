package com.ssafy.keywe.data.profile

import com.ssafy.keywe.data.ResponseResult
import com.ssafy.keywe.data.dto.profile.GetAllProfileResponse
import com.ssafy.keywe.data.dto.profile.GetProfileDetailResponse
import com.ssafy.keywe.data.dto.profile.PatchProfileRequest
import com.ssafy.keywe.data.dto.profile.PatchProfileResponse
import com.ssafy.keywe.data.dto.profile.PostProfileRequest
import com.ssafy.keywe.data.dto.profile.PostProfileResponse

interface ProfileDataSource {
    suspend fun requestGetAllProfile(): ResponseResult<GetAllProfileResponse>
    suspend fun requestGetProfileDetail(profileId: Long): ResponseResult<GetProfileDetailResponse>
    suspend fun requestPostProfile(postProfileRequest: PostProfileRequest): ResponseResult<PostProfileResponse>
    suspend fun requestPatchProfile(profileId: Long, patchProfileRequest: PatchProfileRequest): ResponseResult<PatchProfileResponse>
    suspend fun requestDeleteProfile(profileId: Long): ResponseResult<Unit>
}