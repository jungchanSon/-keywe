package com.ssafy.keywe.data.profile

import com.ssafy.keywe.data.dto.profile.DeleteProfileRequest
import com.ssafy.keywe.data.dto.profile.GetProfileDetailResponse
import com.ssafy.keywe.data.dto.profile.GetProfileListResponse
import com.ssafy.keywe.data.dto.profile.PostProfileResponse
import com.ssafy.keywe.data.dto.profile.UpdateProfileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ProfileService {
    @GET(PROFILE_LIST_PATH)
    suspend fun getProfileList(
    ): Response<List<GetProfileListResponse>>

    @GET(PROFILE_PATH)
    suspend fun getProfileDetail(
        @Query("profileId") profileId: Long
    ): Response<GetProfileDetailResponse>

    @Multipart
    @POST(PROFILE_PATH)
    suspend fun postProfile(
//        @Body postProfileRequest: PostProfileRequest,
//        @Body imageBase64: String?
//        @Body postProfileRequest: RequestBody,
        @Part("profile") profile: RequestBody,
        @Part profileImage: MultipartBody.Part? = null
    ): Response<PostProfileResponse>

    @Multipart
    @PATCH(PROFILE_PATH)
    suspend fun updateProfile(
        @Part("profile") profile: RequestBody,
        @Part profileImage: MultipartBody.Part?
    ): Response<UpdateProfileResponse>

    //    @DELETE(PROFILE_PATH)
    @HTTP(method = "DELETE", path = PROFILE_PATH, hasBody = true)
    suspend fun deleteProfile(
        @Header("Authorization") token: String,
        @Body request: DeleteProfileRequest
    ): Response<Unit>

    @POST("/user/profile/sms/send")
    suspend fun sendSmsVerification(
        @Body phone: Map<String, String>
    ): Response<Unit>

    @POST("/user/profile/sms/verify")
    suspend fun verifySmsCode(
        @Body request: Map<String, String>
    ): Response<Unit>

    companion object {
        private const val PROFILE_LIST_PATH = "/user/profile/list"
        private const val PROFILE_PATH = "/user/profile"
    }


}