package com.ssafy.keywe.data.order

import android.util.Log
import com.ssafy.keywe.data.ApiResponseHandler.handleApiResponse
import com.ssafy.keywe.data.ResponseResult
import com.ssafy.keywe.data.TokenManager
import com.ssafy.keywe.data.dto.order.CategoryRequest
import com.ssafy.keywe.data.dto.order.CategoryResponse
import com.ssafy.keywe.data.dto.order.MenuDetailResponse
import com.ssafy.keywe.data.dto.order.MenuOptionResponse
import com.ssafy.keywe.data.dto.order.MenuPostResponse
import com.ssafy.keywe.data.dto.order.MenuSimpleResponse
import com.ssafy.keywe.data.dto.order.OptionPostRequest
import com.ssafy.keywe.data.dto.order.PostOrderRequest
import com.ssafy.keywe.data.dto.order.PostOrderResponse
import com.ssafy.keywe.data.dto.order.VerificationUserRequest
import com.ssafy.keywe.data.dto.order.VerificationUserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class OrderRemoteDataSource @Inject constructor(
    private val orderService: OrderService, private val tokenManager: TokenManager,
) : OrderDataSource {

    override suspend fun requestPostCategory(categoryRequest: CategoryRequest): ResponseResult<Unit> =
        handleApiResponse {
            orderService.postCategory(
                categoryRequest
            )
        }

    override suspend fun requestGetCategory(): ResponseResult<List<CategoryResponse>> =
        handleApiResponse {
            orderService.getCategory(
            )
        }


    override suspend fun requestUpdateCategory(
        categoryId: Long,
        categoryRequest: CategoryRequest,
    ): ResponseResult<Unit> = handleApiResponse {
        orderService.updateCategory(
            categoryId, categoryRequest
        )
    }

    override suspend fun requestDeleteCategory(categoryId: Long): ResponseResult<Unit> =
        handleApiResponse {
            orderService.deleteCategory(
                categoryId
            )
        }

    override suspend fun requestPostMenu(
        menu: RequestBody,
        menuImage: MultipartBody.Part?,
    ): ResponseResult<MenuPostResponse> {
        return handleApiResponse {
            orderService.postMenu(menu, menuImage)
        }
    }

    override suspend fun requestUpdateMenu(
        menuId: Long,
        menu: RequestBody,
        menuImage: MultipartBody.Part?,
    ): ResponseResult<Unit> {
        return handleApiResponse {
            orderService.updateMenu(menuId, menu, menuImage)
        }
    }

    override suspend fun requestGetAllMenu(storeId: Long): ResponseResult<List<MenuSimpleResponse>> =
        handleApiResponse {
            orderService.getMenuAll(storeId)
        }


    override suspend fun requestGetDetailMenu(
        menuId: Long,
        storeId: Long,
    ): ResponseResult<MenuDetailResponse> = handleApiResponse {
        val result = orderService.getDetailMenu(menuId, storeId)
        Log.d("Test", result.body().toString())
        result
    }

    override suspend fun requestGetCategoryMenu(
        categoryId: Long,
        storeId: Long,
    ): ResponseResult<List<MenuSimpleResponse>> = handleApiResponse {
        orderService.getCategoryMenu(categoryId, storeId)
    }

    override suspend fun requestDeleteMenu(menuId: Long): ResponseResult<Unit> = handleApiResponse {
        orderService.deleteMenu(menuId)
    }

    override suspend fun requestPostOption(
        menuId: Long,
        optionRequest: OptionPostRequest,
    ): ResponseResult<MenuOptionResponse> = handleApiResponse {
        orderService.postOption(menuId, optionRequest)
    }

    override suspend fun requestUpdateOption(
        menuId: Long,
        optionValueId: Long,
        optionRequest: OptionPostRequest,
    ): ResponseResult<MenuOptionResponse> = handleApiResponse {
        orderService.updateOption(menuId, optionValueId, optionRequest)
    }

    override suspend fun requestDeleteOption(
        menuId: Long,
        optionValueId: Long,
    ): ResponseResult<Unit> = handleApiResponse {
        orderService.deleteOption(menuId, optionValueId)
    }

    override suspend fun requestPostOrder(postOrderRequest: PostOrderRequest): ResponseResult<PostOrderResponse> =
        handleApiResponse {
            orderService.postOrder(postOrderRequest)
        }

    override suspend fun requestVerificationUser(verificationUserRequest: VerificationUserRequest): ResponseResult<VerificationUserResponse> =
        handleApiResponse {
            orderService.verificationUser(verificationUserRequest)
        }


}

