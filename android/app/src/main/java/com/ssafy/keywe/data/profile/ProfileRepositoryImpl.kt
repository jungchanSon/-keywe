package com.ssafy.keywe.data.profile

import com.ssafy.keywe.data.ResponseResult
import com.ssafy.keywe.data.dto.mapper.toDomain
import com.ssafy.keywe.data.dto.profile.PatchProfileRequest
import com.ssafy.keywe.data.dto.profile.PatchProfileResponse
import com.ssafy.keywe.data.dto.profile.PostProfileRequest
import com.ssafy.keywe.domain.profile.GetAllProfileModel
import com.ssafy.keywe.domain.profile.GetProfileDetailModel
import com.ssafy.keywe.domain.profile.PatchProfileModel
import com.ssafy.keywe.domain.profile.PostProfileModel
import com.ssafy.keywe.domain.profile.ProfileRepository
import javax.inject.Inject

interface ProfileRepository {

}

class ProfileRepositoryImpl @Inject constructor(
    private val profileDataSource: ProfileDataSource,
) : ProfileRepository {

    //    override suspend fun login(loginRequest: LoginRequest): ResponseResult<LoginModel> {
//        return when (val result = authDataSource.requestLogin(loginRequest)) {
//            is ResponseResult.Exception -> ResponseResult.Exception(
//                result.e,
//                EXCEPTION_NETWORK_ERROR_MESSAGE
//            )
//
//            is ResponseResult.ServerError -> ResponseResult.ServerError(result.status)
//            is ResponseResult.Success -> ResponseResult.Success(result.data!!.toDomain())
//        }
//    }
    override suspend fun getAllProfile(): ResponseResult<GetAllProfileModel> {
        return when (val result = profileDataSource.requestGetAllProfile()) {
            is ResponseResult.Exception -> ResponseResult.Exception(
                result.e,
                EXCEPTION_NETWORK_ERROR_MESSAGE
            )

            is ResponseResult.ServerError -> ResponseResult.ServerError(result.status)
            is ResponseResult.Success -> ResponseResult.Success(result.data!!.toDomain())
        }
    }

    override suspend fun getProfileDetail(profileId: Long): ResponseResult<GetProfileDetailModel> {
        return when (val result = profileDataSource.requestGetProfileDetail(profileId)) {
            is ResponseResult.Exception -> ResponseResult.Exception(
                result.e,
                EXCEPTION_NETWORK_ERROR_MESSAGE
            )

            is ResponseResult.ServerError -> ResponseResult.ServerError(result.status)
            is ResponseResult.Success -> ResponseResult.Success(result.data!!.toDomain())
        }
    }

    override suspend fun postProfile(postProfileRequest: PostProfileRequest): ResponseResult<PostProfileModel> {
        return when (val result = profileDataSource.requestPostProfile(postProfileRequest)) {
            is ResponseResult.Exception -> ResponseResult.Exception(
                result.e,
                EXCEPTION_NETWORK_ERROR_MESSAGE
            )

            is ResponseResult.ServerError -> ResponseResult.ServerError(result.status)
            is ResponseResult.Success -> ResponseResult.Success(result.data!!.toDomain())
        }
    }

    override suspend fun patchProfile(profileId: Long, patchProfileRequest: PatchProfileRequest): ResponseResult<PatchProfileModel> {
        return when (val result = profileDataSource.requestPatchProfile(profileId, patchProfileRequest)) {
            is ResponseResult.Exception -> ResponseResult.Exception(
                result.e,
                EXCEPTION_NETWORK_ERROR_MESSAGE
            )

            is ResponseResult.ServerError -> ResponseResult.ServerError(result.status)
            is ResponseResult.Success -> ResponseResult.Success(result.data!!.toDomain())
        }
    }

    override suspend fun deleteProfile(profileId: Long): ResponseResult<Unit> {
        return when (val result = profileDataSource.requestDeleteProfile(profileId)) {
            is ResponseResult.Exception -> ResponseResult.Exception(
                result.e,
                EXCEPTION_NETWORK_ERROR_MESSAGE
            )

            is ResponseResult.ServerError -> ResponseResult.ServerError(result.status)
            is ResponseResult.Success -> result
        }
    }

    companion object {
        private const val EXCEPTION_NETWORK_ERROR_MESSAGE =
            "네트워크 연결이 불안정합니다.\n연결을 재설정한 후 다시 시도해 주세요."
    }
}