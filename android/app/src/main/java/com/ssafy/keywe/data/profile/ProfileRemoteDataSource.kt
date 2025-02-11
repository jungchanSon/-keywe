package com.ssafy.keywe.data.profile

import com.ssafy.keywe.data.ApiResponseHandler.handleApiResponse
import com.ssafy.keywe.data.ResponseResult
import com.ssafy.keywe.data.dto.profile.GetAllProfileResponse
import com.ssafy.keywe.data.dto.profile.GetProfileDetailResponse
import com.ssafy.keywe.data.dto.profile.PatchProfileRequest
import com.ssafy.keywe.data.dto.profile.PatchProfileResponse
import com.ssafy.keywe.data.dto.profile.PostProfileRequest
import com.ssafy.keywe.data.dto.profile.PostProfileResponse
import javax.inject.Inject

class ProfileRemoteDataSource @Inject constructor(private val profileService: ProfileService) :
    ProfileDataSource {

    override suspend fun requestGetAllProfile(): ResponseResult<GetAllProfileResponse> =
        handleApiResponse {
            profileService.getAllProfile()
        }

    override suspend fun requestGetProfileDetail(profileId: Long): ResponseResult<GetProfileDetailResponse> =
        handleApiResponse {
            profileService.getProfileDetail(profileId)
        }

    override suspend fun requestPostProfile(postProfileRequest: PostProfileRequest): ResponseResult<PostProfileResponse> =
        handleApiResponse {
            profileService.postProfile(postProfileRequest)
        }

    override suspend fun requestPatchProfile(profileId: Long, patchProfileRequest: PatchProfileRequest): ResponseResult<PatchProfileResponse> =
        handleApiResponse {
            profileService.patchProfile(profileId, patchProfileRequest)
        }

    override suspend fun requestDeleteProfile(profileId: Long): ResponseResult<Unit> =
        handleApiResponse {
            profileService.deleteProfile((profileId))
        }

}