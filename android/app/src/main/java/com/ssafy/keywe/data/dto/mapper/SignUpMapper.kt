package com.ssafy.keywe.data.dto.mapper

import com.ssafy.keywe.data.dto.auth.SignUpResponse
import com.ssafy.keywe.domain.auth.SignUpModel

fun SignUpResponse.toDomain(): SignUpModel = SignUpModel(this.accessToken)
