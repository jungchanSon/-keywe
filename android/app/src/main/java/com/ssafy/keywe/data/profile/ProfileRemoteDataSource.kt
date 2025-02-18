package com.ssafy.keywe.data.profile

//import com.ssafy.keywe.data.dto.profile.GetProfileRequest
import com.ssafy.keywe.data.ApiResponseHandler.handleApiResponse
import com.ssafy.keywe.data.ResponseResult
import com.ssafy.keywe.data.dto.profile.DeleteProfileRequest
import com.ssafy.keywe.data.dto.profile.GetProfileDetailResponse
import com.ssafy.keywe.data.dto.profile.GetProfileListResponse
import com.ssafy.keywe.data.dto.profile.PostProfileResponse
import com.ssafy.keywe.data.dto.profile.UpdateProfileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class ProfileRemoteDataSource @Inject constructor(private val profileService: ProfileService) :
    ProfileDataSource {

    override suspend fun requestGetProfileList(): ResponseResult<List<GetProfileListResponse>> =
        handleApiResponse {
            profileService.getProfileList()
        }

    override suspend fun requestGetProfileDetail(profileId: Long): ResponseResult<GetProfileDetailResponse> =
        handleApiResponse {
            profileService.getProfileDetail(profileId)
        }

    override suspend fun requestPostProfile(
        profileBody: RequestBody,
        profileImage: MultipartBody.Part?
    ): ResponseResult<PostProfileResponse> =
        handleApiResponse {
            profileService.postProfile(profileBody, profileImage)
        }

    override suspend fun requestUpdateProfile(
        profileBody: RequestBody,
        profileImage: MultipartBody.Part?
    ): ResponseResult<UpdateProfileResponse> =
        handleApiResponse {
            profileService.updateProfile(profileBody, profileImage)
        }

    override suspend fun requestDeleteProfile(
        profileId: Long,
        token: String
    ): ResponseResult<Unit> =
        handleApiResponse {
            val request = DeleteProfileRequest(profileId)
            profileService.deleteProfile(token, request)
        }

    override suspend fun requestSendSmsVerification(phone: String): ResponseResult<Unit> =
        handleApiResponse {
            profileService.sendSmsVerification(mapOf("phone" to phone))
        }

    override suspend fun requestVerifySmsCode(
        phone: String,
        verificationCode: String
    ): ResponseResult<Unit> =
        handleApiResponse {
            profileService.verifySmsCode(
                mapOf(
                    "phone" to phone,
                    "verificationCode" to verificationCode
                )
            )
        }


}