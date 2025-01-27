package com.ssafy.keywe.data.auth

import com.ssafy.keywe.data.ApiResponseHandler.handleApiResponse
import com.ssafy.keywe.data.ResponseResult
import com.ssafy.keywe.data.dto.auth.MITILoginRequest
import com.ssafy.keywe.data.dto.auth.MITILoginResponse
import com.ssafy.keywe.data.dto.auth.MITIResponse
import javax.inject.Inject

class AuthRemoteDataSource @Inject constructor(private val authService: AuthService) :
    AuthDataSource {
    override suspend fun requestLogin(loginRequest: MITILoginRequest): ResponseResult<MITIResponse<MITILoginResponse>> =
        handleApiResponse {
            authService.loginMiti(loginRequest)
        }
}