package com.ssafy.keywe.data.dto.order

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class MenuPostResponse(
    val menuId: Long,
    val menuName: String,
    val menuRecipe: String,
    val menuPrice: Int,
    val options: List<OptionsResponse>
)

data class OptionsResponse(
    val optionId: Long,
    val optionType: String,
    val optionName: String,
    val optionPrice: Int,
    val optionsValueGroup: List<OptionsValueGroupResponse>
)

data class OptionsValueGroupResponse(
    val optionValueId: Long,
    val optionValue: String
)


data class MenuSimpleResponse(
    @SerialName("menuId") val menuId: Long,
    @SerialName("menuName") val menuName: String,
    @SerialName("menuRecipe") val menuRecipe: String,
    @SerialName("menuPrice") val menuPrice: Int,
    @SerialName("image") val image: String?
)

data class MenuDetailResponse(
    val menuId: Long,
    val menuName: String,
    val menuDescription: String,
    val menuRecipe: String,
    val menuPrice: Int,
    val image: String?,
    val options: List<OptionsResponse>
)

data class MenuOptionResponse(
    val menuId: Long,
    val menuName: String,
    val menuPrice: Int,
    val options: List<OptionsResponse>
)

data class PostOrderResponse(
    val orderId: String
)