package com.ssafy.keywe.data.profile

import com.ssafy.keywe.data.ApiResponseHandler.handleApiResponse
import com.ssafy.keywe.data.ResponseResult
import com.ssafy.keywe.data.dto.profile.GetProfileDetailResponse
import com.ssafy.keywe.data.dto.profile.GetProfileListResponse
import com.ssafy.keywe.data.dto.profile.GetProfileRequest
import com.ssafy.keywe.data.dto.profile.PostProfileRequest
import com.ssafy.keywe.data.dto.profile.PostProfileResponse
import com.ssafy.keywe.data.dto.profile.UpdateProfileRequest
import com.ssafy.keywe.data.dto.profile.UpdateProfileResponse
import javax.inject.Inject

class ProfileRemoteDataSource @Inject constructor(private val profileService: ProfileService) :
    ProfileDataSource {

    override suspend fun requestGetProfileList(): ResponseResult<List<GetProfileListResponse>> =
        handleApiResponse {
            profileService.getProfileList()
        }

    override suspend fun requestGetProfileDetail(getProfileRequest: GetProfileRequest): ResponseResult<GetProfileDetailResponse> =
        handleApiResponse {
            profileService.getProfileDetail(getProfileRequest)
        }

    override suspend fun requestPostProfile(postProfileRequest: PostProfileRequest): ResponseResult<PostProfileResponse> =
        handleApiResponse {
            profileService.postProfile(postProfileRequest)
        }

    override suspend fun requestUpdateProfile(updateProfileRequest: UpdateProfileRequest): ResponseResult<UpdateProfileResponse> =
        handleApiResponse {
            profileService.updateProfile(updateProfileRequest)
        }

    override suspend fun requestDeleteProfile(profileId: Long): ResponseResult<Unit> =
        handleApiResponse {
            profileService.deleteProfile(profileId)
        }

}