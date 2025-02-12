package com.ssafy.keywe.data.dto.order

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

data class PostCartItemsRequest(
    val phoneNumber: String,
    val menuList: List<CartItemsRequest>
)

data class CartItemsRequest(
    val menuId: Long,
    val menuCount: Long,
    val optionList: List<OptionListRequest>
)

data class OptionListRequest(
    val optionValueId: Long,
    val optionCount: Long
)