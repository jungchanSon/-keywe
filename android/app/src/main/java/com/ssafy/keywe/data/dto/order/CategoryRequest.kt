package com.ssafy.keywe.data.dto.order

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryRequest(
    @SerialName("categoryName") val categoryName: String,
)