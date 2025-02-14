package com.ssafy.keywe.data.profile

import com.ssafy.keywe.data.ResponseResult
import com.ssafy.keywe.data.dto.mapper.toDomain
import com.ssafy.keywe.data.dto.profile.GetProfileRequest
import com.ssafy.keywe.data.dto.profile.PostProfileRequest
import com.ssafy.keywe.data.dto.profile.UpdateProfileRequest
import com.ssafy.keywe.domain.profile.GetProfileDetailModel
import com.ssafy.keywe.domain.profile.GetProfileListModel
import com.ssafy.keywe.domain.profile.PostProfileModel
import com.ssafy.keywe.domain.profile.ProfileRepository
import com.ssafy.keywe.domain.profile.UpdateProfileModel
import javax.inject.Inject


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
    override suspend fun getProfileList(): ResponseResult<List<GetProfileListModel>> {
        return when (val result = profileDataSource.requestGetProfileList()) {
            is ResponseResult.Exception -> ResponseResult.Exception(
                result.e, EXCEPTION_NETWORK_ERROR_MESSAGE
            )

            is ResponseResult.ServerError -> ResponseResult.ServerError(result.status)
            is ResponseResult.Success -> ResponseResult.Success(result.data.map { it.toDomain() })
        }
    }

    override suspend fun getProfileDetail(getProfileRequest: GetProfileRequest): ResponseResult<GetProfileDetailModel> {
        return when (val result = profileDataSource.requestGetProfileDetail(getProfileRequest)) {
            is ResponseResult.Exception -> ResponseResult.Exception(
                result.e, EXCEPTION_NETWORK_ERROR_MESSAGE
            )

            is ResponseResult.ServerError -> ResponseResult.ServerError(result.status)
            is ResponseResult.Success -> ResponseResult.Success(result.data!!.toDomain())
        }
    }

    override suspend fun postProfile(postProfileRequest: PostProfileRequest): ResponseResult<PostProfileModel> {
        return when (val result = profileDataSource.requestPostProfile(postProfileRequest)) {
            is ResponseResult.Exception -> ResponseResult.Exception(
                result.e, EXCEPTION_NETWORK_ERROR_MESSAGE
            )

            is ResponseResult.ServerError -> ResponseResult.ServerError(result.status)
            is ResponseResult.Success -> ResponseResult.Success(result.data!!.toDomain())
        }
    }

    override suspend fun updateProfile(updateProfileRequest: UpdateProfileRequest): ResponseResult<UpdateProfileModel> {
        return when (val result = profileDataSource.requestUpdateProfile(updateProfileRequest)) {
            is ResponseResult.Exception -> ResponseResult.Exception(
                result.e, EXCEPTION_NETWORK_ERROR_MESSAGE
            )

            is ResponseResult.ServerError -> ResponseResult.ServerError(result.status)
            is ResponseResult.Success -> ResponseResult.Success(result.data!!.toDomain())
        }
    }

    override suspend fun deleteProfile(profileId: Long): ResponseResult<Unit> {
        return when (val result = profileDataSource.requestDeleteProfile(profileId)) {
            is ResponseResult.Exception -> ResponseResult.Exception(
                result.e, EXCEPTION_NETWORK_ERROR_MESSAGE
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