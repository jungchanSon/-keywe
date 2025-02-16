package com.ssafy.keywe.data.dto.order

import com.ssafy.keywe.Token
import okhttp3.MultipartBody

data class MenuPostRequest(
    val menu: List<MenuPostItemRequest>,
    val menuImage: MultipartBody.Part?
)

data class MenuPostItemRequest(
    val menuName: String,
    val menuCategoryName: String,
    val menuDescription: String,
    val menuRecipe: String,
    val menuPrice: Int,
    val options: List<OptionPostRequest>
)

data class OptionPostRequest(
    val optionType: String,
    val optionName: String,
    val optionPrice: Int,
    val optionsValueGroup: List<OptionsValueGroupResponse>
)

data class MenuPatchRequest(
    val menu: List<MenuPatchItemRequest>,
    val menuImage: MultipartBody.Part?
)

data class MenuPatchItemRequest(
    val menuName: String?,
    val menuCategoryName: String?,
    val menuDescription: String?,
    val menuRecipe: String?,
    val menuPrice: Int?
)

data class PostOrderRequest(
    val phoneNumber: String,
    val menuList: List<OrderMenuItem>
)

data class OrderMenuItem(
    val menuId: Long,
    val menuCount: Int,
    val optionList: List<OrderOptionItem>
)

data class OrderOptionItem(
    val optionValueId: Long,
    val optionCount: Int
)

data class VerificationUserRequest(
    val phone: String,
    val password: String
)