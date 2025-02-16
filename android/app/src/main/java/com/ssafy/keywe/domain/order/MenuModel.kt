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

data class OrderModel(
    val phoneNumber: String,
    val menuList: List<OrderMenuItemModel>
)

data class OrderMenuItemModel(
    val menuId: Long,
    val menuCount: Int,
    val optionList: List<OrderOptionItemModel>
)

data class OrderOptionItemModel(
    val optionValueId: Long,
    val optionCount: Int
)

data class OrderResponseModel(
    val orderId: String
)

data class VerificationUserModel(
    val phone: String,
    val password: String
)

data class VerificationUserResponseModel(
    val accessToken: String,
    val profileId: String
)