package com.ssafy.keywe.domain.auth

import com.ssafy.keywe.data.ResponseResult
import com.ssafy.keywe.data.dto.auth.LoginRequest
import com.ssafy.keywe.data.dto.auth.SignUpRequest

interface AuthRepository {
    suspend fun login(loginRequest: LoginRequest): ResponseResult<LoginModel>
    suspend fun signUp(signUpRequest: SignUpRequest): ResponseResult<SignUpModel>
}