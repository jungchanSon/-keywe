package com.ssafy.keywe.data.order

import com.ssafy.keywe.data.ResponseResult
import com.ssafy.keywe.data.dto.mapper.toDomain
import com.ssafy.keywe.data.dto.mapper.toRequest
import com.ssafy.keywe.data.dto.order.CategoryRequest
import com.ssafy.keywe.domain.order.OrderRepository
import com.ssafy.keywe.domain.order.CategoryModel
import javax.inject.Inject

interface OrderRepository {
    suspend fun postCategory(category: CategoryModel): ResponseResult<Unit>
    suspend fun getCategory(): ResponseResult<List<CategoryModel>>
    suspend fun updateCategory(categoryId: Long, categoryName: String): ResponseResult<Unit>
    suspend fun deleteCategory(categoryId: Long): ResponseResult<Unit>
}


class OrderRepositoryImpl @Inject constructor(
    private val orderDataSource: OrderDataSource,
) : OrderRepository {

    override suspend fun postCategory(category: CategoryModel): ResponseResult<Unit> {
        val categoryRequest = category.toRequest()
        return runCatching {
            when (val result = orderDataSource.postCategory(categoryRequest)) {
                is ResponseResult.Success -> result
                is ResponseResult.ServerError -> ResponseResult.ServerError(result.status)
                is ResponseResult.Exception -> ResponseResult.Exception(
                    result.e,
                    EXCEPTION_NETWORK_ERROR_MESSAGE
                )
            }
        }.getOrElse {
            ResponseResult.Exception(it, EXCEPTION_NETWORK_ERROR_MESSAGE)
        }
    }

    override suspend fun getCategory(): ResponseResult<List<CategoryModel>> {
        return runCatching {
            when (val result = orderDataSource.getCategory()) {
                is ResponseResult.Success -> ResponseResult.Success(result.data.map { it.toDomain() })
                is ResponseResult.ServerError -> ResponseResult.ServerError(result.status)
                is ResponseResult.Exception -> ResponseResult.Exception(
                    result.e,
                    EXCEPTION_NETWORK_ERROR_MESSAGE
                )
            }
        }.getOrElse {
            ResponseResult.Exception(it, EXCEPTION_NETWORK_ERROR_MESSAGE)
        }
    }

    override suspend fun updateCategory(
        categoryId: Long,
        categoryName: String
    ): ResponseResult<Unit> {
        val categoryRequest = CategoryRequest(categoryName)

        return runCatching {
            when (val result = orderDataSource.updateCategory(categoryId, categoryRequest)) {
                is ResponseResult.Success -> result
                is ResponseResult.ServerError -> ResponseResult.ServerError(result.status)
                is ResponseResult.Exception -> ResponseResult.Exception(
                    result.e,
                    EXCEPTION_NETWORK_ERROR_MESSAGE
                )
            }
        }.getOrElse {
            ResponseResult.Exception(it, EXCEPTION_NETWORK_ERROR_MESSAGE)
        }
    }

    override suspend fun deleteCategory(categoryId: Long): ResponseResult<Unit> {  // 🔹 categoryId 추가
        return runCatching {
            when (val result = orderDataSource.deleteCategory(categoryId)) {
                is ResponseResult.Success -> result
                is ResponseResult.ServerError -> ResponseResult.ServerError(result.status)
                is ResponseResult.Exception -> ResponseResult.Exception(
                    result.e,
                    EXCEPTION_NETWORK_ERROR_MESSAGE
                )
            }
        }.getOrElse {
            ResponseResult.Exception(it, EXCEPTION_NETWORK_ERROR_MESSAGE)
        }
    }


    companion object {
        private const val EXCEPTION_NETWORK_ERROR_MESSAGE =
            "네트워크 연결이 불안정합니다.\n연결을 재설정한 후 다시 시도해 주세요."
    }
}
