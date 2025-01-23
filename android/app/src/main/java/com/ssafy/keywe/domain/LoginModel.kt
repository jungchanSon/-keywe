package com.ssafy.keywe.domain

import com.ssafy.keywe.data.dto.login.TokenResponse

data class LoginModel(
    val id: Int,
    val email: String,
    val nickname: String,
    val signupMethod: String,
    val token: TokenResponse,
)
