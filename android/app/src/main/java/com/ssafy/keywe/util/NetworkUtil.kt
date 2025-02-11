package com.ssafy.keywe.util

import kotlinx.serialization.json.Json

object NetworkUtil {
    const val BASE_URL = "http://i12a404.p.ssafy.io:8080"
    val jsonBuilder = Json { coerceInputValues = true }
    const val AUTHORIZATION = "Authorization"

    val WITH_TOKEN = listOf("/auth/user/login")
}