package com.ssafy.keywe.data.dto.mapper

import com.ssafy.keywe.data.dto.auth.CEOLoginResponse
import com.ssafy.keywe.data.dto.auth.LoginResponse
import com.ssafy.keywe.domain.auth.CEOLoginModel
import com.ssafy.keywe.domain.auth.LoginModel

fun LoginResponse.toDomain(): LoginModel = LoginModel(
    accessToken
)

fun CEOLoginResponse.toDomain(): CEOLoginModel = CEOLoginModel(
    accessToken = this.accessToken,
    refreshToken = this.refreshToken
)