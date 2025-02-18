package com.ssafy.keywe.domain.profile

//import com.ssafy.keywe.data.dto.profile.GetProfileRequest
import android.content.Context
import android.net.Uri
import com.ssafy.keywe.data.ResponseResult
import com.ssafy.keywe.data.dto.profile.UpdateProfileRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface ProfileRepository {
    suspend fun getProfileList(): ResponseResult<List<GetProfileListModel>>
    suspend fun getProfileDetail(profileId: Long): ResponseResult<GetProfileDetailModel>
    suspend fun postProfile(
//        postProfileRequest: PostProfileRequest,
//        context: Context,
//        imageUri: Uri?
        profileBody: RequestBody,
        image: MultipartBody.Part?
    ): ResponseResult<PostProfileModel>

    suspend fun updateProfile(
        updateProfileRequest: UpdateProfileRequest,
        context: Context,
        imageUri: Uri?
    ): ResponseResult<UpdateProfileModel>

    suspend fun deleteProfile(profileId: Long, token: String): ResponseResult<Unit>
    suspend fun sendSmsVerification(phone: String): ResponseResult<String>
    suspend fun verifySmsCode(phone: String, verificationCode: String): ResponseResult<String>
}
