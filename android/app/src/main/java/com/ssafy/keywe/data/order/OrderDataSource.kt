package com.ssafy.keywe.data.order

import com.ssafy.keywe.data.ResponseResult
import com.ssafy.keywe.data.dto.order.CategoryRequest
import com.ssafy.keywe.data.dto.order.CategoryResponse
import com.ssafy.keywe.data.dto.order.MenuDetailResponse
import com.ssafy.keywe.data.dto.order.MenuOptionResponse
import com.ssafy.keywe.data.dto.order.MenuPostResponse
import com.ssafy.keywe.data.dto.order.MenuSimpleResponse
import com.ssafy.keywe.data.dto.order.OptionPostRequest
import com.ssafy.keywe.data.dto.order.PostOrderRequest
import com.ssafy.keywe.data.dto.order.PostOrderResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody


interface OrderDataSource {
    suspend fun requestPostCategory(categoryRequest: CategoryRequest): ResponseResult<Unit>
    suspend fun requestGetCategory(): ResponseResult<List<CategoryResponse>>
    suspend fun requestUpdateCategory(categoryId: Long, categoryRequest: CategoryRequest): ResponseResult<Unit>
    suspend fun requestDeleteCategory(categoryId: Long): ResponseResult<Unit>

    suspend fun requestPostMenu(menu: RequestBody, menuImage: MultipartBody.Part?): ResponseResult<MenuPostResponse>
    suspend fun requestUpdateMenu(menuId: Long, menu: RequestBody, menuImage: MultipartBody.Part?): ResponseResult<Unit>
    suspend fun requestGetAllMenu(): ResponseResult<List<MenuSimpleResponse>>
    suspend fun requestGetDetailMenu(menuId: Long): ResponseResult<MenuDetailResponse>
    suspend fun requestGetCategoryMenu(categoryId: Long): ResponseResult<List<MenuSimpleResponse>>
    suspend fun requestDeleteMenu(menuId: Long): ResponseResult<Unit>

    suspend fun requestPostOption(menuId: Long, optionRequest: OptionPostRequest): ResponseResult<MenuOptionResponse>
    suspend fun requestUpdateOption(menuId: Long, optionValueId: Long, optionRequest: OptionPostRequest): ResponseResult<MenuOptionResponse>
    suspend fun requestDeleteOption(menuId: Long, optionValueId: Long): ResponseResult<Unit>

    suspend fun requestPostOrder(postOrderRequest: PostOrderRequest): ResponseResult<PostOrderResponse>
}