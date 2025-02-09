package com.ssafy.keywe.data.dto.mapper

import com.ssafy.keywe.data.dto.order.CategoryRequest
import com.ssafy.keywe.data.dto.order.CategoryResponse
import com.ssafy.keywe.domain.order.CategoryModel

// API 응답 → 앱 내부 모델 변환 (서버 → 앱)
fun CategoryResponse.toDomain(): CategoryModel = CategoryModel (
        categoryId = this.categoryId,
        categoryName = this.categoryName
)

// 앱 내부 모델 → API 요청 DTO 변환 (앱 → 서버)
fun CategoryModel.toRequest(): CategoryRequest = CategoryRequest(
        categoryName = this.categoryName
)