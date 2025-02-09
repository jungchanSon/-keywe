package com.ssafy.keywe.data.order

import com.ssafy.keywe.data.ApiResponseHandler.handleApiResponse
import com.ssafy.keywe.data.ResponseResult
import com.ssafy.keywe.data.TokenManager
import com.ssafy.keywe.data.dto.order.CategoryRequest
import com.ssafy.keywe.data.dto.order.CategoryResponse
import javax.inject.Inject

class OrderRemoteDataSource @Inject constructor(
    private val orderService: OrderService, private val tokenManager: TokenManager
) : OrderDataSource {

    override suspend fun postCategory(categoryRequest: CategoryRequest): ResponseResult<Unit> =
        handleApiResponse {
            orderService.postCategory(
                categoryRequest
            )
        }

    override suspend fun getCategory(): ResponseResult<List<CategoryResponse>> =
        handleApiResponse {
            orderService.getCategory(
            )
        }


    override suspend fun updateCategory(categoryId: Long, categoryRequest: CategoryRequest): ResponseResult<Unit> =
        handleApiResponse {
            orderService.updateCategory(
                categoryId,
                categoryRequest
            )
        }

    override suspend fun deleteCategory(categoryId: Long): ResponseResult<Unit> =
        handleApiResponse {
            orderService.deleteCategory(
                categoryId
            )
        }
}