package com.ssafy.keywe.data.login

import com.ssafy.keywe.data.ResponseResult
import com.ssafy.keywe.data.dto.login.MITILoginRequest
import com.ssafy.keywe.domain.LoginModel

interface LoginRepository {
    suspend fun login(loginRequest: MITILoginRequest): ResponseResult<LoginModel>
//    suspend fun login(loginRequest: LoginRequest): Response<LoginResponse> {
//        return loginApiService.login(loginRequest)
//    }
//
//    suspend fun test(): Response<TestResponse> {
//
//
//        return loginApiService.test()
//    }
//
//    suspend fun mitiLogin(loginRequest: MITILoginRequest): Response<MITIResponse<MITILoginResponse>> {
//        return loginApiService.loginMiti(loginRequest)
//    }
}