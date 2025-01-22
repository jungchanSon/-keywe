package com.ssafy.keywe.data.login

import com.ssafy.keywe.data.ResponseResult
import com.ssafy.keywe.data.dto.login.LoginRequest
import com.ssafy.keywe.data.dto.login.LoginResponse
import com.ssafy.keywe.data.dto.login.MITILoginRequest
import com.ssafy.keywe.data.dto.login.MITILoginResponse
import com.ssafy.keywe.data.dto.login.MITIResponse
import com.ssafy.keywe.data.dto.login.TestResponse
import retrofit2.Response
import javax.inject.Inject

interface LoginApiRepository
{
    suspend fun login(loginRequest:MITILoginRequest) : ResponseResult<MITIResponse<MITILoginResponse>>
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