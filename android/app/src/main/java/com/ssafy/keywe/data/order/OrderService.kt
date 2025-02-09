package com.ssafy.keywe.data.order

import com.ssafy.keywe.data.dto.order.CategoryRequest
import com.ssafy.keywe.data.dto.order.CategoryResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface OrderService {

    @POST(CATEGORY_POST_GET_PATH)
    suspend fun postCategory(
        @Body categoryRequest: CategoryRequest,
    ): Response<Unit>

    @GET(CATEGORY_POST_GET_PATH)
    suspend fun getCategory(
    ): Response<List<CategoryResponse>>

    @PATCH(CATEGORY_PATCH_DELETE_PATH)
    suspend fun updateCategory(
        @Path("categoryId") categoryId: Long,
        @Body categoryRequest: CategoryRequest
    ): Response<Unit>

    @DELETE(CATEGORY_PATCH_DELETE_PATH)
    suspend fun deleteCategory(
        @Path("categoryId") categoryId: Long
    ): Response<Unit>

    companion object {
        private const val CATEGORY_POST_GET_PATH = "/category"
        private const val CATEGORY_PATCH_DELETE_PATH = "/category/{categoryId}"
    }
}