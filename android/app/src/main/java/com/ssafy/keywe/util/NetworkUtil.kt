package com.ssafy.keywe.util

import kotlinx.serialization.json.Json

object NetworkUtil {
    const val BASE_URL = "http://43.201.85.41:8080"
    val jsonBuilder = Json { coerceInputValues = true }
    const val AUTHORIZATION = "Authorization"
}