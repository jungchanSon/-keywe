package com.ssafy.keywe.data.auth

import com.ssafy.keywe.data.ApiResponseHandler.handleApiResponse
import com.ssafy.keywe.data.ResponseResult
import com.ssafy.keywe.data.dto.auth.CEOLoginRequest
import com.ssafy.keywe.data.dto.auth.CEOLoginResponse
import com.ssafy.keywe.data.dto.auth.LoginRequest
import com.ssafy.keywe.data.dto.auth.LoginResponse
import com.ssafy.keywe.data.dto.auth.SelectProfileRequest
import com.ssafy.keywe.data.dto.auth.SelectProfileResponse
import com.ssafy.keywe.data.dto.auth.SignUpRequest
import com.ssafy.keywe.data.dto.auth.SignUpResponse
import javax.inject.Inject

class AuthRemoteDataSource @Inject constructor(private val authService: AuthService) :
    AuthDataSource {
    override suspend fun requestLogin(loginRequest: LoginRequest): ResponseResult<LoginResponse> =
        handleApiResponse {
            authService.userLogin(loginRequest)
        }

    override suspend fun requestCeoLogin(ceoLoginRequest: CEOLoginRequest): ResponseResult<CEOLoginResponse> =
        handleApiResponse {
            authService.ceoLogin(ceoLoginRequest)
        }

    override suspend fun requestSignUp(signUpRequest: SignUpRequest): ResponseResult<SignUpResponse> =
        handleApiResponse {
            authService.signup(signUpRequest)
        }

    override suspend fun requestSelectProfile(selectProfileRequest: SelectProfileRequest): ResponseResult<SelectProfileResponse> =
        handleApiResponse {
            authService.selectProfile(selectProfileRequest)
        }
}