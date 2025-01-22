package com.ssafy.keywe.data.login

import com.ssafy.keywe.data.ResponseResult
import com.ssafy.keywe.data.dto.Status
import com.ssafy.keywe.data.dto.login.MITILoginRequest
import com.ssafy.keywe.data.dto.login.MITILoginResponse
import com.ssafy.keywe.data.dto.login.MITIResponse
import javax.inject.Inject

class LoginApiImplRepository(
    @Inject private val loginDataSource: LoginDataSource,
) : LoginApiRepository {
    override suspend fun login(loginRequest: MITILoginRequest) :  ResponseResult<MITIResponse<MITILoginResponse>>  {

        return when (val result = loginDataSource.requestLogin(loginRequest)) {
            is ResponseResult.Exception -> ResponseResult.Exception(result.e, EXCEPTION_NETWORK_ERROR_MESSAGE)
            is ResponseResult.ServerError -> ResponseResult.ServerError(result.status)
            is ResponseResult.Success -> ResponseResult.Success(result.data)
        }

    }
    companion object {
        private const val EXCEPTION_NETWORK_ERROR_MESSAGE = "네트워크 연결이 불안정합니다.\n연결을 재설정한 후 다시 시도해 주세요."
    }

}