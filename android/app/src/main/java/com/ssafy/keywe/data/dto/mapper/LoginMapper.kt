package com.ssafy.keywe.data.dto.mapper

import com.ssafy.keywe.data.dto.auth.MITILoginResponse
import com.ssafy.keywe.domain.LoginModel

fun MITILoginResponse.toDomain(): LoginModel = LoginModel(
    id = id, email = email, nickname = nickname, signupMethod = signupMethod, token = token
)
