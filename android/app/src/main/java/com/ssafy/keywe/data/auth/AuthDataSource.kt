package com.ssafy.keywe.data.auth

import com.ssafy.keywe.data.ResponseResult
import com.ssafy.keywe.data.dto.auth.MITILoginRequest
import com.ssafy.keywe.data.dto.auth.MITILoginResponse
import com.ssafy.keywe.data.dto.auth.MITIResponse

interface AuthDataSource {
    suspend fun requestLogin(loginRequest: MITILoginRequest):
            ResponseResult<MITIResponse<MITILoginResponse>>
}