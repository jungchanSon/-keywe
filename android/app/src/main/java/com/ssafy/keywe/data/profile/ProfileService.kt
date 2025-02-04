package com.ssafy.keywe.data.profile

import com.ssafy.keywe.data.dto.profile.ProfileResponse
import com.ssafy.keywe.data.dto.profile.UpdateProfileRequest
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ProfileService {
    @GET(PROFILE_PATH)
    suspend fun getProfile(
        @Path("userId") userId: String
    ): ProfileResponse

    @PUT(PROFILE_PATH)
    suspend fun updateProfile(
        @Path("userId") userId: String,
        @Body updateProfileRequest: UpdateProfileRequest
    ): ProfileResponse

    @Multipart
    @PUT("$PROFILE_PATH/image")
    suspend fun updateProfileImage(
        @Path("userId") userId: String,
        @Part image: MultipartBody.Part
    ): ProfileResponse

    @DELETE(PROFILE_PATH)
    suspend fun deleteProfile(
        @Path("userId") userId: String
    ): ProfileResponse

    companion object {
        private const val PROFILE_PATH = "/user/{userId}"
    }


}