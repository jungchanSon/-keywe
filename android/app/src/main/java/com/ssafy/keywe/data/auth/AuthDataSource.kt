package com.ssafy.keywe.data.auth

import com.ssafy.keywe.data.ResponseResult
import com.ssafy.keywe.data.dto.auth.CEOLoginRequest
import com.ssafy.keywe.data.dto.auth.CEOLoginResponse
import com.ssafy.keywe.data.dto.auth.LoginRequest
import com.ssafy.keywe.data.dto.auth.LoginResponse
import com.ssafy.keywe.data.dto.auth.SignUpRequest
import com.ssafy.keywe.data.dto.auth.SignUpResponse

interface AuthDataSource {
    suspend fun requestLogin(loginRequest: LoginRequest): ResponseResult<LoginResponse>
    suspend fun requestCeoLogin(ceoLoginRequest: CEOLoginRequest): ResponseResult<CEOLoginResponse>
    suspend fun requestSignUp(signUpRequest: SignUpRequest): ResponseResult<SignUpResponse>
}