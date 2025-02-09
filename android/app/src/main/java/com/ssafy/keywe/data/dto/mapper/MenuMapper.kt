package com.ssafy.keywe.data.dto.mapper

import com.google.gson.Gson
import com.ssafy.keywe.data.dto.order.MenuDetailResponse
import com.ssafy.keywe.data.dto.order.MenuOptionResponse
import com.ssafy.keywe.data.dto.order.MenuPatchItem
import com.ssafy.keywe.data.dto.order.MenuPatchRequest
import com.ssafy.keywe.data.dto.order.MenuPostItem
import com.ssafy.keywe.data.dto.order.MenuPostRequest
import com.ssafy.keywe.data.dto.order.MenuPostResponse
import com.ssafy.keywe.data.dto.order.MenuSimpleResponse
import com.ssafy.keywe.data.dto.order.OptionPost
import com.ssafy.keywe.data.dto.order.OptionValueGroup
import com.ssafy.keywe.data.dto.order.Options
import com.ssafy.keywe.domain.order.MenuDetailModel
import com.ssafy.keywe.domain.order.MenuModel
import com.ssafy.keywe.domain.order.MenuOptionModel
import com.ssafy.keywe.domain.order.MenuSimpleModel
import com.ssafy.keywe.domain.order.OptionPostModel
import com.ssafy.keywe.domain.order.OptionValueGroupModel
import com.ssafy.keywe.domain.order.OptionsModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

fun MenuModel.toRequest(): MenuPostRequest {
    val gson = Gson()

    // 옵션을 JSON으로 변환
    val optionsJson = gson.toJson(this.options)
    val optionsRequestBody = optionsJson.toRequestBody("application/json".toMediaTypeOrNull())

    // 이미지 변환
    val imagePart: MultipartBody.Part? = if (!this.imageUrl.isNullOrEmpty()) {
        val file = File(this.imageUrl)
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        MultipartBody.Part.createFormData("menuImage", file.name, requestBody)
    } else {
        null  // ✅ Retrofit에서 null을 허용하도록 처리
    }

    return MenuPostRequest(
        menu = listOf(
            MenuPostItem(
                menuName = this.menuName,
                menuCategoryName = this.menuCategory,
                menuDescription = this.menuDescription,
                menuRecipe = this.menuRecipe,
                menuPrice = this.menuPrice,
                options = this.options.map { it.toRequest() }
            )
        ),
        menuImage = imagePart // ✅ null 허용
    )
}


fun OptionsModel.toRequest(): OptionPost {
    return OptionPost(
        optionType = this.optionType,
        optionName = this.optionName,
        optionPrice = this.optionPrice,
        optionValueGroup  = this.optionValueGroup.map { it.toRequest() }
    )
}

fun OptionValueGroupModel.toRequest(): OptionValueGroup {
    return OptionValueGroup(
        optionValueId = this.optionValueId,
        optionValue = this.optionValue
    )
}

fun MenuPostResponse.toDomain(): MenuModel {
    return MenuModel(
        menuId = this.menuId,
        menuName = this.menuName,
        menuCategory = "",
        menuDescription = "",
        menuRecipe = "",
        menuPrice = this.menuPrice,
        imageUrl = "",
        options = this.options.map { it.toDomain() }
    )
}

fun Options.toDomain(): OptionsModel {
    return OptionsModel(
        optionType = this.optionType,
        optionName = this.optionName,
        optionPrice = this.optionPrice,
        optionValueGroup = this.optionValueGroup.map { it.toDomain() }
    )
}

fun OptionValueGroup.toDomain(): OptionValueGroupModel {
    return OptionValueGroupModel(
        optionValueId = this.optionValueId,
        optionValue = this.optionValue
    )
}

fun MenuModel.toPatchRequest(): MenuPatchRequest {
    val imagePart: MultipartBody.Part? = if (!this.imageUrl.isNullOrEmpty()) {
        val file = File(this.imageUrl)
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        MultipartBody.Part.createFormData("menuImage", file.name, requestBody)
    } else {
        null
    }

    return MenuPatchRequest(
        menu = listOf(
            MenuPatchItem(
                menuName = this.menuName,
                menuCategoryName = this.menuCategory,
                menuDescription = this.menuDescription,
                menuRecipe = this.menuRecipe,
                menuPrice = this.menuPrice
            )
        ),
        menuImage = imagePart
    )
}

fun MenuSimpleResponse.toSimpleDomain(): MenuSimpleModel {
    return MenuSimpleModel(
        menuId = this.menuId,
        menuName = this.menuName,
        menuRecipe = this.menuRecipe,
        menuPrice = this.menuPrice,
        imageUrl = this.image
    )
}

fun MenuDetailResponse.toDomain(): MenuDetailModel {
    return MenuDetailModel(
        menuId = this.menuId,
        menuName = this.menuName,
        menuDescription = this.menuDescription,
        menuRecipe = this.menuRecipe,
        menuPrice = this.menuPrice,
        imageUrl = this.image,
        options = this.options.map { it.toDomain() }
    )
}

fun MenuOptionResponse.toDomain(): MenuOptionModel {
    return MenuOptionModel(
        menuId = this.menuId,
        menuName = this.menuName,
        menuPrice = this.menuPrice,
        options = this.options.map { it.toDomain() }
    )
}

fun OptionPostModel.toRequest(): OptionPost {
    return OptionPost(
        optionType = this.optionType,
        optionName = this.optionName,
        optionPrice = this.optionPrice,
        optionValueGroup = this.optionValueGroup.map { it.toRequest() }
    )
}

