package com.ssafy.keywe.data.order

import com.ssafy.keywe.data.ResponseResult
import com.ssafy.keywe.data.dto.order.CategoryRequest
import com.ssafy.keywe.data.dto.order.CategoryResponse

interface OrderDataSource {
    suspend fun postCategory(categoryRequest: CategoryRequest): ResponseResult<Unit>
    suspend fun getCategory(): ResponseResult<List<CategoryResponse>>
    suspend fun updateCategory(categoryRequest: CategoryRequest): ResponseResult<Unit>
    suspend fun deleteCategory(categoryId: Long): ResponseResult<Unit>
}