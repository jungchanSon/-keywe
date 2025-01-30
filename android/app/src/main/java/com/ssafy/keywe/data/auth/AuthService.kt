package com.ssafy.keywe.data.auth

import com.ssafy.keywe.data.dto.auth.LoginRequest
import com.ssafy.keywe.data.dto.auth.LoginResponse
import com.ssafy.keywe.data.dto.auth.SignUpRequest
import com.ssafy.keywe.data.dto.auth.SignUpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface AuthService {

    @POST(LOGIN_PATH)
    suspend fun login(
        @Body loginRequest: LoginRequest,
//        @Header("Authorization") authorization: Boolean = false,
    ): Response<LoginResponse>

    @POST(SIGNUP_PATH)
    suspend fun signup(
        @Body signUpRequest: SignUpRequest,
//        @Header("Authorization") authorization: Boolean = false,
    ): Response<SignUpResponse>

    companion object {
        private const val LOGIN_PATH = "/auth/user/login"
        private const val SIGNUP_PATH = "/user"
    }
}