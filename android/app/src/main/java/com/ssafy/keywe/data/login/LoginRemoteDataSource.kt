package com.ssafy.keywe.data.login

import com.ssafy.keywe.data.ApiResponseHandler.handleApiResponse
import com.ssafy.keywe.data.ResponseResult
import com.ssafy.keywe.data.dto.login.MITILoginRequest
import com.ssafy.keywe.data.dto.login.MITILoginResponse
import com.ssafy.keywe.data.dto.login.MITIResponse
import javax.inject.Inject

class LoginRemoteDataSource @Inject
constructor(private val loginService: LoginService) : LoginDataSource {
    override suspend fun requestLogin(loginRequest: MITILoginRequest):
            ResponseResult<MITIResponse<MITILoginResponse>> =
        handleApiResponse {
            loginService.loginMiti(loginRequest)
        }
}