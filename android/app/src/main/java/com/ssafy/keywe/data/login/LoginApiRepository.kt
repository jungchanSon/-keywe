package com.ssafy.keywe.data.login

import com.ssafy.keywe.data.dto.login.LoginRequest
import com.ssafy.keywe.data.dto.login.LoginResponse
import com.ssafy.keywe.data.dto.login.TestResponse
import retrofit2.Response
import javax.inject.Inject

class LoginApiRepository
@Inject constructor(
    private val loginApiService: LoginApiService,
) {
    suspend fun login(loginRequest: LoginRequest): Response<LoginResponse> {
        return loginApiService.login(loginRequest)
    }

    suspend fun test(): Response<TestResponse> {
        

        return loginApiService.test()
    }
}