package com.ssafy.keywe.domain.auth

import com.ssafy.keywe.data.ResponseResult
import com.ssafy.keywe.data.dto.auth.CEOLoginRequest
import com.ssafy.keywe.data.dto.auth.LoginRequest
import com.ssafy.keywe.data.dto.auth.SelectProfileRequest
import com.ssafy.keywe.data.dto.auth.SignUpRequest

interface AuthRepository {
    suspend fun login(loginRequest: LoginRequest): ResponseResult<LoginModel>
    suspend fun ceoLogin(ceoLoginRequest: CEOLoginRequest): ResponseResult<CEOLoginModel>
    suspend fun selectProfile(selectProfileRequest: SelectProfileRequest): ResponseResult<SelectProfileModel>
    suspend fun signUp(signUpRequest: SignUpRequest): ResponseResult<SignUpModel>
}