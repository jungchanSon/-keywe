package com.ssafy.keywe.data.dto.mapper

import com.google.gson.Gson
import com.ssafy.keywe.data.dto.order.MenuDetailResponse
import com.ssafy.keywe.data.dto.order.MenuOptionResponse
import com.ssafy.keywe.data.dto.order.MenuPatchItemRequest
import com.ssafy.keywe.data.dto.order.MenuPatchRequest
import com.ssafy.keywe.data.dto.order.MenuPostItemRequest
import com.ssafy.keywe.data.dto.order.MenuPostRequest
import com.ssafy.keywe.data.dto.order.MenuPostResponse
import com.ssafy.keywe.data.dto.order.MenuSimpleResponse
import com.ssafy.keywe.data.dto.order.OptionPostRequest
import com.ssafy.keywe.data.dto.order.OptionsValueGroupResponse
import com.ssafy.keywe.data.dto.order.OptionsResponse
import com.ssafy.keywe.data.dto.order.OrderMenuItem
import com.ssafy.keywe.data.dto.order.OrderOptionItem
import com.ssafy.keywe.data.dto.order.PostOrderRequest
import com.ssafy.keywe.data.dto.order.PostOrderResponse
import com.ssafy.keywe.data.dto.order.VerificationUserResponse
import com.ssafy.keywe.domain.order.MenuDetailModel
import com.ssafy.keywe.domain.order.MenuModel
import com.ssafy.keywe.domain.order.MenuOptionModel
import com.ssafy.keywe.domain.order.MenuSimpleModel
import com.ssafy.keywe.domain.order.OptionPostModel
import com.ssafy.keywe.domain.order.OptionsValueGroupModel
import com.ssafy.keywe.domain.order.OptionsModel
import com.ssafy.keywe.domain.order.OrderMenuItemModel
import com.ssafy.keywe.domain.order.OrderModel
import com.ssafy.keywe.domain.order.OrderOptionItemModel
import com.ssafy.keywe.domain.order.OrderResponseModel
import com.ssafy.keywe.domain.order.VerificationUserResponseModel
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
    val imagePart: MultipartBody.Part? = if (!this.image.isNullOrEmpty()) {
        val file = File(this.image)
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        MultipartBody.Part.createFormData("menuImage", file.name, requestBody)
    } else {
        null  // ✅ Retrofit에서 null을 허용하도록 처리
    }

    return MenuPostRequest(
        menu = listOf(
            MenuPostItemRequest(
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


fun OptionsModel.toRequest(): OptionPostRequest {
    return OptionPostRequest(
        optionType = this.optionType,
        optionName = this.optionName,
        optionPrice = this.optionPrice,
        optionsValueGroup  = this.optionsValueGroup.map { it.toRequest() }
    )
}

fun OptionsValueGroupModel.toRequest(): OptionsValueGroupResponse {
    return OptionsValueGroupResponse(
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
        image = "",
        options = this.options.map { it.toDomain() }
    )
}

fun OptionsResponse.toDomain(): OptionsModel {
    return OptionsModel(
        optionId = this.optionId,
        optionType = this.optionType,
        optionName = this.optionName,
        optionPrice = this.optionPrice,
        optionsValueGroup = this.optionsValueGroup.map { it.toDomain() }
    )
}

fun OptionsValueGroupResponse.toDomain(): OptionsValueGroupModel {
    return OptionsValueGroupModel(
        optionValueId = this.optionValueId,
        optionValue = this.optionValue
    )
}

fun MenuModel.toPatchRequest(): MenuPatchRequest {
    val imagePart: MultipartBody.Part? = if (!this.image.isNullOrEmpty()) {
        val file = File(this.image)
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        MultipartBody.Part.createFormData("menuImage", file.name, requestBody)
    } else {
        null
    }

    return MenuPatchRequest(
        menu = listOf(
            MenuPatchItemRequest(
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
        image = this.image
    )
}

fun MenuDetailResponse.toDomain(): MenuDetailModel {
    return MenuDetailModel(
        menuId = this.menuId,
        menuName = this.menuName,
        menuDescription = this.menuDescription,
        menuRecipe = this.menuRecipe,
        menuPrice = this.menuPrice,
        image = this.image,
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

fun OptionPostModel.toRequest(): OptionPostRequest {
    return OptionPostRequest(
        optionType = this.optionType,
        optionName = this.optionName,
        optionPrice = this.optionPrice,
        optionsValueGroup = this.optionsValueGroup.map { it.toRequest() }
    )
}

fun OrderModel.toRequest(): PostOrderRequest {
    return PostOrderRequest(
        phoneNumber = this.phoneNumber,
        menuList = this.menuList.map { it.toRequest() }
    )
}

fun OrderMenuItemModel.toRequest(): OrderMenuItem {
    return OrderMenuItem(
        menuId = this.menuId,
        menuCount = this.menuCount,
        optionList = this.optionList.map { it.toRequest() }
    )
}

fun OrderOptionItemModel.toRequest(): OrderOptionItem {
    return OrderOptionItem(
        optionValueId = this.optionValueId,
        optionCount = this.optionCount
    )
}

fun PostOrderResponse.toDomain(): OrderResponseModel {
    return OrderResponseModel(
        orderId = this.orderId
    )
}

fun VerificationUserResponse.toDomain(): VerificationUserResponseModel {
    return VerificationUserResponseModel(
        accessToken = this.accessToken,
        profileId = this.profileId
    )
}
