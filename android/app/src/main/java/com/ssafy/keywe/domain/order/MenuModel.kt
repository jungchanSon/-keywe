package com.ssafy.keywe.domain.order

data class MenuModel(
    val menuId: Long,
    val menuName: String,
    val menuCategory: String,
    val menuDescription: String,
    val menuRecipe: String,
    val menuPrice: Int,
    val image: String? = null,
    val options: List<OptionsModel>
)

data class OptionsModel(
    val optionId: Long,
    val optionName: String,
    val optionType: String,
    val optionPrice: Int,
    val optionsValueGroup: List<OptionsValueGroupModel>
)

data class OptionsValueGroupModel(
    val optionValueId: Long,
    val optionValue: String
)

data class MenuSimpleModel(
    val menuId: Long,
    val menuName: String,
    val menuRecipe: String,
    val menuPrice: Int,
    val image: String? = null
)

data class MenuDetailModel(
    val menuId: Long,
    val menuName: String,
    val menuDescription: String,
    val menuRecipe: String,
    val menuPrice: Int,
    val image: String? = null,
    val options: List<OptionsModel>
)

data class MenuOptionModel(
    val menuId: Long,
    val menuName: String,
    val menuPrice: Int,
    val options: List<OptionsModel>
)

data class OptionPostModel(
    val optionId: Long,
    val optionType: String,
    val optionName: String,
    val optionPrice: Int,
    val optionsValueGroup: List<OptionsValueGroupModel>
)