package com.ssafy.keywe.data.order

import com.google.gson.Gson
import com.ssafy.keywe.data.ResponseResult
import com.ssafy.keywe.data.dto.mapper.toDomain
import com.ssafy.keywe.data.dto.mapper.toPatchRequest
import com.ssafy.keywe.data.dto.mapper.toRequest
import com.ssafy.keywe.data.dto.mapper.toSimpleDomain
import com.ssafy.keywe.data.dto.order.CategoryRequest
import com.ssafy.keywe.data.dto.order.MenuPostRequest
import com.ssafy.keywe.domain.order.OrderRepository
import com.ssafy.keywe.domain.order.CategoryModel
import com.ssafy.keywe.domain.order.MenuDetailModel
import com.ssafy.keywe.domain.order.MenuModel
import com.ssafy.keywe.domain.order.MenuOptionModel
import com.ssafy.keywe.domain.order.MenuSimpleModel
import com.ssafy.keywe.domain.order.OptionPostModel
import com.ssafy.keywe.domain.order.OptionsModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

//interface OrderRepository {
//    suspend fun postCategory(category: CategoryModel): ResponseResult<Unit>
//    suspend fun getCategory(): ResponseResult<List<CategoryModel>>
//    suspend fun updateCategory(categoryId: Long, categoryName: String): ResponseResult<Unit>
//    suspend fun deleteCategory(categoryId: Long): ResponseResult<Unit>
//
//    suspend fun postMenu(menuPostModel: MenuModel): ResponseResult<MenuModel>
//    suspend fun updateMenu(): ResponseResult<Unit>
//}


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

    override suspend fun postMenu(menuPostModel: MenuModel): ResponseResult<MenuModel> {
        val menuRequest = menuPostModel.toRequest()

        val gson = Gson()
        val menuJson = gson.toJson(menuRequest.menu)
        val menuRequestBody = menuJson.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        return runCatching {
            when (val result = orderDataSource.postMenu(menuRequestBody, menuRequest.menuImage)) {
                is ResponseResult.Success -> ResponseResult.Success(result.data.toDomain())  // ✅ 변환 적용
                is ResponseResult.ServerError -> ResponseResult.ServerError(result.status)
                is ResponseResult.Exception -> ResponseResult.Exception(result.e, EXCEPTION_NETWORK_ERROR_MESSAGE)
            }
        }.getOrElse {
            ResponseResult.Exception(it, EXCEPTION_NETWORK_ERROR_MESSAGE)
        }
    }

    override suspend fun updateMenu(menuId: Long, menuPatchModel: MenuModel): ResponseResult<Unit> {
        val menuRequest = menuPatchModel.toPatchRequest()

        val gson = Gson()
        val menuJson = gson.toJson(menuRequest.menu)
        val menuRequestBody = menuJson.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        return runCatching {
            when (val result = orderDataSource.updateMenu(menuId, menuRequestBody, menuRequest.menuImage)) {
                is ResponseResult.Success -> result
                is ResponseResult.ServerError -> ResponseResult.ServerError(result.status)
                is ResponseResult.Exception -> ResponseResult.Exception(result.e, EXCEPTION_NETWORK_ERROR_MESSAGE)
            }
        }.getOrElse {
            ResponseResult.Exception(it, EXCEPTION_NETWORK_ERROR_MESSAGE)
        }
    }

    override suspend fun getAllMenu(): ResponseResult<List<MenuSimpleModel>> {
        return runCatching {
            when (val result = orderDataSource.getAllMenu()) {
                is ResponseResult.Success -> ResponseResult.Success(result.data.map { it.toSimpleDomain() })
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

    override suspend fun getDetailMenu(menuId: Long): ResponseResult<MenuDetailModel> {
        return runCatching {
            when (val result = orderDataSource.getDetailMenu(menuId)) {
                is ResponseResult.Success -> ResponseResult.Success(result.data.toDomain()) // ✅ 변환 적용
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

    override suspend fun getCategoryMenu(categoryId: Long): ResponseResult<List<MenuSimpleModel>> {
        return runCatching {
            when (val result = orderDataSource.getCategoryMenu(categoryId)) {
                is ResponseResult.Success -> ResponseResult.Success(result.data.map { it.toSimpleDomain() })
                is ResponseResult.ServerError -> ResponseResult.ServerError(result.status)
                is ResponseResult.Exception -> ResponseResult.Exception(result.e, EXCEPTION_NETWORK_ERROR_MESSAGE)
            }
        }.getOrElse {
            ResponseResult.Exception(it, EXCEPTION_NETWORK_ERROR_MESSAGE)
        }
    }

    override suspend fun deleteMenu(menuId: Long): ResponseResult<Unit> {
        return runCatching {
            when (val result = orderDataSource.deleteMenu(menuId)) {
                is ResponseResult.Success -> result
                is ResponseResult.ServerError -> ResponseResult.ServerError(result.status)
                is ResponseResult.Exception -> ResponseResult.Exception(
                    result.e, EXCEPTION_NETWORK_ERROR_MESSAGE
                )
            }
        }.getOrElse {
            ResponseResult.Exception(it, EXCEPTION_NETWORK_ERROR_MESSAGE)
        }
    }

    override suspend fun postOption(menuId: Long, option: OptionPostModel): ResponseResult<MenuOptionModel> {
        val optionRequest = option.toRequest()

        return runCatching {
            when (val result = orderDataSource.postOption(menuId, optionRequest)) {
                is ResponseResult.Success -> ResponseResult.Success(result.data.toDomain())
                is ResponseResult.ServerError -> ResponseResult.ServerError(result.status)
                is ResponseResult.Exception -> ResponseResult.Exception(result.e, EXCEPTION_NETWORK_ERROR_MESSAGE)
            }
        }.getOrElse {
            ResponseResult.Exception(it, EXCEPTION_NETWORK_ERROR_MESSAGE)
        }
    }

    override suspend fun updateOption(menuId: Long, optionValueId: Long, option: OptionPostModel): ResponseResult<MenuOptionModel> {
        val optionRequest = option.toRequest()

        return runCatching {
            when (val result = orderDataSource.updateOption(menuId, optionValueId, optionRequest)) {
                is ResponseResult.Success -> ResponseResult.Success(result.data.toDomain())
                is ResponseResult.ServerError -> ResponseResult.ServerError(result.status)
                is ResponseResult.Exception -> ResponseResult.Exception(result.e, EXCEPTION_NETWORK_ERROR_MESSAGE)
            }
        }.getOrElse {
            ResponseResult.Exception(it, EXCEPTION_NETWORK_ERROR_MESSAGE)
        }
    }

    override suspend fun deleteOption(menuId: Long, optionValueId: Long): ResponseResult<Unit> {
        return runCatching {
            when (val result = orderDataSource.deleteOption(menuId, optionValueId)) {
                is ResponseResult.Success -> result
                is ResponseResult.ServerError -> ResponseResult.ServerError(result.status)
                is ResponseResult.Exception -> ResponseResult.Exception(
                    result.e, EXCEPTION_NETWORK_ERROR_MESSAGE
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
