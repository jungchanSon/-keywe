package com.ssafy.keywe.data.dto.mapper

import com.ssafy.keywe.data.dto.auth.CEOLoginResponse
import com.ssafy.keywe.data.dto.auth.LoginResponse
import com.ssafy.keywe.data.dto.auth.SelectProfileResponse
import com.ssafy.keywe.domain.auth.CEOLoginModel
import com.ssafy.keywe.domain.auth.LoginModel
import com.ssafy.keywe.domain.auth.SelectProfileModel

fun LoginResponse.toDomain(): LoginModel = LoginModel(
    accessToken
)

fun CEOLoginResponse.toDomain(): CEOLoginModel = CEOLoginModel(
    accessToken = this.accessToken,
    refreshToken = this.refreshToken
)

fun SelectProfileResponse.toDomain(): SelectProfileModel = SelectProfileModel(
    accessToken = this.accessToken,
    refreshToken = this.refreshToken
)