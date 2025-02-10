package com.ssafy.keywe.data.auth

import com.ssafy.keywe.data.dto.auth.CEOLoginRequest
import com.ssafy.keywe.data.dto.auth.CEOLoginResponse
import com.ssafy.keywe.data.dto.auth.LoginRequest
import com.ssafy.keywe.data.dto.auth.LoginResponse
import com.ssafy.keywe.data.dto.auth.SelectProfileRequest
import com.ssafy.keywe.data.dto.auth.SelectProfileResponse
import com.ssafy.keywe.data.dto.auth.SignUpRequest
import com.ssafy.keywe.data.dto.auth.SignUpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface AuthService {

    @POST(USER_LOGIN_PATH)
    suspend fun userLogin(
        @Body loginRequest: LoginRequest,
//        @Header("Authorization") authorization: Boole3an = false,
    ): Response<LoginResponse>

    @POST(CEO_LOGIN_PATH)
    suspend fun ceoLogin(
        @Body ceoLoginRequest: CEOLoginRequest
    ): Response<CEOLoginResponse>

    @POST(PROFILE_SELECT_PATH)
    suspend fun selectProfile(
        @Body selectProfileRequest: SelectProfileRequest
    ): Response<SelectProfileResponse>


    @POST(SIGNUP_PATH)
    suspend fun signup(
        @Body signUpRequest: SignUpRequest,
//        @Header("Authorization") authorization: Boolean = false,
    ): Response<SignUpResponse>

    companion object {
        private const val USER_LOGIN_PATH = "/auth/user/login"
        private const val CEO_LOGIN_PATH = "/auth/ceo/login"
        private const val PROFILE_SELECT_PATH = "/auth/user/profile"
        private const val SIGNUP_PATH = "/user"
    }
}