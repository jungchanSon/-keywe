package com.ssafy.keywe.data.dto.order

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryResponse(

    @SerialName("categoryId")
    val categoryId: Long,

    @SerialName("categoryName")
    val categoryName: String,

)