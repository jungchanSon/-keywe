package com.ssafy.keywe.domain.profile

import com.ssafy.keywe.data.ResponseResult
import com.ssafy.keywe.data.dto.profile.PatchProfileRequest
import com.ssafy.keywe.data.dto.profile.PostProfileRequest

interface ProfileRepository {
    suspend fun getAllProfile(): ResponseResult<List<GetAllProfileModel>>
    suspend fun getProfileDetail(profileId: Long): ResponseResult<GetProfileDetailModel>
    suspend fun postProfile(postProfileRequest: PostProfileRequest): ResponseResult<PostProfileModel>
    suspend fun patchProfile(
        profileId: Long, patchProfileRequest: PatchProfileRequest
    ): ResponseResult<PatchProfileModel>

    suspend fun deleteProfile(profileId: Long): ResponseResult<Unit>

}