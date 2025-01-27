package com.ssafy.keywe.domain.auth

import com.ssafy.keywe.data.ResponseResult
import com.ssafy.keywe.data.dto.auth.MITILoginRequest
import com.ssafy.keywe.domain.LoginModel

interface AuthRepository {
    suspend fun login(loginRequest: MITILoginRequest): ResponseResult<LoginModel>
}