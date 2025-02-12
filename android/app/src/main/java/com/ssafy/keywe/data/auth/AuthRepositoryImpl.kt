package com.ssafy.keywe.data.auth

import com.ssafy.keywe.data.ResponseResult
import com.ssafy.keywe.data.dto.auth.CEOLoginRequest
import com.ssafy.keywe.data.dto.auth.LoginRequest
import com.ssafy.keywe.data.dto.auth.SelectProfileRequest
import com.ssafy.keywe.data.dto.auth.SignUpRequest
import com.ssafy.keywe.data.dto.mapper.toDomain
import com.ssafy.keywe.domain.auth.AuthRepository
import com.ssafy.keywe.domain.auth.CEOLoginModel
import com.ssafy.keywe.domain.auth.LoginModel
import com.ssafy.keywe.domain.auth.SelectProfileModel
import com.ssafy.keywe.domain.auth.SignUpModel
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource,
) : AuthRepository {
    override suspend fun login(loginRequest: LoginRequest): ResponseResult<LoginModel> {
        return when (val result = authDataSource.requestLogin(loginRequest)) {
            is ResponseResult.Exception -> ResponseResult.Exception(
                result.e,
                EXCEPTION_NETWORK_ERROR_MESSAGE
            )

            is ResponseResult.ServerError -> ResponseResult.ServerError(result.status)
            is ResponseResult.Success -> ResponseResult.Success(result.data!!.toDomain())
        }
    }

    override suspend fun ceoLogin(ceoLoginRequest: CEOLoginRequest): ResponseResult<CEOLoginModel> {
        return when (val result = authDataSource.requestCeoLogin(ceoLoginRequest)) {
            is ResponseResult.Exception -> ResponseResult.Exception(
                result.e, EXCEPTION_NETWORK_ERROR_MESSAGE
            )

            is ResponseResult.ServerError -> ResponseResult.ServerError(result.status)
            is ResponseResult.Success -> ResponseResult.Success(result.data!!.toDomain())
        }
    }

    override suspend fun signUp(signUpRequest: SignUpRequest): ResponseResult<SignUpModel> {
        return when (val result = authDataSource.requestSignUp(signUpRequest)) {
            is ResponseResult.Exception -> ResponseResult.Exception(
                result.e,
                EXCEPTION_NETWORK_ERROR_MESSAGE
            )

            is ResponseResult.ServerError -> ResponseResult.ServerError(result.status)
            is ResponseResult.Success -> ResponseResult.Success(result.data!!.toDomain())
        }
    }

    override suspend fun selectProfile(selectProfileRequest: SelectProfileRequest): ResponseResult<SelectProfileModel> {
        return when (val result = authDataSource.requestSelectProfile(selectProfileRequest)) {
            is ResponseResult.Exception -> ResponseResult.Exception(
                result.e,
                EXCEPTION_NETWORK_ERROR_MESSAGE
            )

            is ResponseResult.ServerError -> ResponseResult.ServerError(result.status)
            is ResponseResult.Success -> ResponseResult.Success(result.data!!.toDomain())
        }
    }

    companion object {
        private const val EXCEPTION_NETWORK_ERROR_MESSAGE =
            "네트워크 연결이 불안정합니다.\n연결을 재설정한 후 다시 시도해 주세요."
    }

}