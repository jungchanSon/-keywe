package com.ssafy.keywe.data.dto.mapper

import com.ssafy.keywe.data.dto.order.CategoryResponse
import com.ssafy.keywe.domain.order.CategoryModel

fun CategoryResponse.toDomain(): CategoryModel = CategoryModel (
        categoryId = this.categoryId,
        categoryName = this.categoryName
)