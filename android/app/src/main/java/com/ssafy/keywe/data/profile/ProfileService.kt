package com.ssafy.keywe.data.profile

import com.ssafy.keywe.data.dto.profile.GetAllProfileResponse
import com.ssafy.keywe.data.dto.profile.GetProfileDetailResponse
import com.ssafy.keywe.data.dto.profile.PatchProfileRequest
import com.ssafy.keywe.data.dto.profile.PatchProfileResponse
import com.ssafy.keywe.data.dto.profile.PostProfileRequest
import com.ssafy.keywe.data.dto.profile.PostProfileResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ProfileService {
    @GET(PROFILE_LIST_PATH)
    suspend fun getAllProfile(
    ): Response<GetAllProfileResponse>

    @GET(PROFILE_PATH)
    suspend fun getProfileDetail(
        @Path("profileId") profileId: Long
    ): Response<GetProfileDetailResponse>

//    @GET(PROFILE_GET_PATCH_DELETE_PATH)
//    suspend fun getProfileDetail(
//        @Path("userId") userId: String
//    ): ProfileListResponse

    @POST(PROFILE_POST_PATH)
    suspend fun postProfile(
        @Body postProfileRequest: PostProfileRequest
    ): Response<PostProfileResponse>

    @PATCH(PROFILE_PATH)
    suspend fun patchProfile(
        @Path("profileId") profileId: Long,
        @Body patchProfileRequest: PatchProfileRequest
    ): Response<PatchProfileResponse>

    @DELETE(PROFILE_PATH)
    suspend fun deleteProfile(
        @Path("profileId") profileId: Long,
    ): Response<Unit>

    companion object {
        private const val PROFILE_LIST_PATH = "/user/profile/list"
        private const val PROFILE_POST_PATH = "/user/profile"
        private const val PROFILE_PATH = "/user/profile/{profileId}"
    }


}