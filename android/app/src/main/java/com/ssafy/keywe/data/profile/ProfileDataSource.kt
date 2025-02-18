package com.ssafy.keywe.data.profile

//import com.ssafy.keywe.data.dto.profile.GetProfileRequest
import com.ssafy.keywe.data.ResponseResult
import com.ssafy.keywe.data.dto.profile.GetProfileDetailResponse
import com.ssafy.keywe.data.dto.profile.GetProfileListResponse
import com.ssafy.keywe.data.dto.profile.PostProfileResponse
import com.ssafy.keywe.data.dto.profile.UpdateProfileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface ProfileDataSource {
    suspend fun requestGetProfileList(): ResponseResult<List<GetProfileListResponse>>
    suspend fun requestGetProfileDetail(profileId: Long): ResponseResult<GetProfileDetailResponse>
    suspend fun requestPostProfile(
        profileBody: RequestBody,
        profileImage: MultipartBody.Part?
    ): ResponseResult<PostProfileResponse>

    suspend fun requestUpdateProfile(
        profileBody: RequestBody,
//        updateProfileRequest: UpdateProfileRequest,
        profileImage: MultipartBody.Part?
    ): ResponseResult<UpdateProfileResponse>

    suspend fun requestDeleteProfile(profileId: Long, token: String): ResponseResult<Unit>

    suspend fun requestSendSmsVerification(phone: String): ResponseResult<Unit>
    suspend fun requestVerifySmsCode(phone: String, verificationCode: String): ResponseResult<Unit>
}