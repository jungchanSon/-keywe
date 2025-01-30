package com.ssafy.keywe.data.dto.mapper

import com.ssafy.keywe.data.dto.auth.LoginResponse
import com.ssafy.keywe.domain.auth.LoginModel

fun LoginResponse.toDomain(): LoginModel = LoginModel(
    accessToken
)