package com.ssafy.keywe.data.profile

import com.ssafy.keywe.data.dto.profile.GetProfileDetailResponse
import com.ssafy.keywe.data.dto.profile.GetProfileListResponse
import com.ssafy.keywe.data.dto.profile.PostProfileRequest
import com.ssafy.keywe.data.dto.profile.PostProfileResponse
import com.ssafy.keywe.data.dto.profile.UpdateProfileRequest
import com.ssafy.keywe.data.dto.profile.UpdateProfileResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface ProfileService {
    @GET(PROFILE_LIST_PATH)
    suspend fun getProfileList(
    ): Response<List<GetProfileListResponse>>

    @GET(PROFILE_PATH)
    suspend fun getProfileDetail(
    ): Response<GetProfileDetailResponse>

    @POST(PROFILE_PATH)
    suspend fun postProfile(
        @Body postProfileRequest: PostProfileRequest
    ): Response<PostProfileResponse>

    @PATCH(PROFILE_PATH)
    suspend fun updateProfile(
        @Body updateProfileRequest: UpdateProfileRequest
    ): Response<UpdateProfileResponse>

    @DELETE(PROFILE_PATH)
    suspend fun deleteProfile(
        @Body profileId: Long,
    ): Response<Unit>

    companion object {
        private const val PROFILE_LIST_PATH = "/user/profile/list"
        private const val PROFILE_PATH = "/user/profile"
    }


}