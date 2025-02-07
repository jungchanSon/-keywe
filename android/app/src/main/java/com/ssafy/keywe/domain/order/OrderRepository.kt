package com.ssafy.keywe.domain.order

import com.ssafy.keywe.data.ResponseResult

interface OrderRepository {
    suspend fun postCategory(category: CategoryModel): ResponseResult<Unit>
    suspend fun getCategory(): ResponseResult<List<CategoryModel>>
    suspend fun updateCategory(category: CategoryModel): ResponseResult<Unit>
    suspend fun deleteCategory(categoryId: Long): ResponseResult<Unit>
}