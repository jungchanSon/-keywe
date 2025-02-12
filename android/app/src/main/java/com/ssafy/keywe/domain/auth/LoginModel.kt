package com.ssafy.keywe.domain.auth

data class LoginModel(
    val accessToken: String,
)

data class CEOLoginModel(
    val accessToken: String,
    val refreshToken: String
)

data class SelectProfileModel(
    val accessToken: String,
//    val refreshToken: String
)