package com.ssafy.keywe.data.dto.order

data class MenuPostResponse(
    val menuId: Long,
    val menuName: String,
    val menuRecipe: String,
    val menuPrice: Int,
    val options: List<Options>
)

data class Options(
    val optionId: Long,
    val optionType: String,
    val optionName: String,
    val optionPrice: Int,
    val optionValueGroup: List<OptionValueGroup>
)

data class OptionValueGroup(
    val optionValueId: Long,
    val optionValue: String
)

data class MenuSimpleResponse(
    val menuId: Long,
    val menuName: String,
    val menuRecipe: String,
    val menuPrice: Int,
    val image: String?
)

data class MenuDetailResponse(
    val menuId: Long,
    val menuName: String,
    val menuDescription: String,
    val menuRecipe: String,
    val menuPrice: Int,
    val image: String?,
    val options: List<Options>
)

data class MenuOptionResponse(
    val menuId: Long,
    val menuName: String,
    val menuPrice: Int,
    val options: List<Options>
)