package com.ssafy.keywe.data.dto.order

import okhttp3.MultipartBody

data class MenuPostRequest(
    val menu: List<MenuPostItem>,
    val menuImage: MultipartBody.Part?
)

data class MenuPostItem(
    val menuName: String,
    val menuCategoryName: String,
    val menuDescription: String,
    val menuRecipe: String,
    val menuPrice: Int,
    val options: List<OptionPost>
)

data class OptionPost(
    val optionType: String,
    val optionName: String,
    val optionPrice: Int,
    val optionValueGroup: List<OptionValueGroup>
)

data class MenuPatchRequest(
    val menu: List<MenuPatchItem>,
    val menuImage: MultipartBody.Part?
)

data class MenuPatchItem(
    val menuName: String?,
    val menuCategoryName: String?,
    val menuDescription: String?,
    val menuRecipe: String?,
    val menuPrice: Int?
)

