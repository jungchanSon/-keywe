package com.ssafy.keywe.data.auth

import com.ssafy.keywe.data.ResponseResult
import com.ssafy.keywe.data.dto.auth.MITILoginRequest
import com.ssafy.keywe.data.dto.mapper.toDomain
import com.ssafy.keywe.domain.LoginModel
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource,
) : AuthRepository {
    override suspend fun login(loginRequest: MITILoginRequest): ResponseResult<LoginModel> {

        return when (val result = authDataSource.requestLogin(loginRequest)) {
            is ResponseResult.Exception -> ResponseResult.Exception(
                result.e,
                EXCEPTION_NETWORK_ERROR_MESSAGE
            )

            is ResponseResult.ServerError -> ResponseResult.ServerError(result.status)
            is ResponseResult.Success -> ResponseResult.Success(result.data.data!!.toDomain())
        }

    }

    companion object {
        private const val EXCEPTION_NETWORK_ERROR_MESSAGE =
            "네트워크 연결이 불안정합니다.\n연결을 재설정한 후 다시 시도해 주세요."
    }

}