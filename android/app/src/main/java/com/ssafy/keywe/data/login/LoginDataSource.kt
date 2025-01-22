package com.ssafy.keywe.data.login

import com.ssafy.keywe.data.ResponseResult
import com.ssafy.keywe.data.dto.login.LoginRequest
import com.ssafy.keywe.data.dto.login.LoginResponse
import com.ssafy.keywe.data.dto.login.MITILoginRequest
import com.ssafy.keywe.data.dto.login.MITILoginResponse
import com.ssafy.keywe.data.dto.login.MITIResponse

interface LoginDataSource {
    suspend fun requestLogin(loginRequest: MITILoginRequest):
            ResponseResult<MITIResponse<MITILoginResponse>>
}