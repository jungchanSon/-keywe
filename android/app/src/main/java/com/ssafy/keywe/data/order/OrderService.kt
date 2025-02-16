package com.ssafy.keywe.data.order

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
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

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
        @Body categoryRequest: CategoryRequest,
    ): Response<Unit>

    @DELETE(CATEGORY_PATCH_DELETE_PATH)
    suspend fun deleteCategory(
        @Path("categoryId") categoryId: Long,
    ): Response<Unit>

    @Multipart
    @POST(MENU_POST_GET_PATH)
    suspend fun postMenu(
        @Part("menu") menu: RequestBody,
        @Part menuImage: MultipartBody.Part?,
    ): Response<MenuPostResponse>

    @Multipart
    @PATCH(MENU_GET_PATCH_DELETE_PATH)
    suspend fun updateMenu(
        @Path("menuId") menuId: Long,
        @Part("menu") menu: RequestBody,
        @Part menuImage: MultipartBody.Part?,
    ): Response<Unit>

    @GET(MENU_POST_GET_PATH)
    suspend fun getMenuAll(
        @Query("sid") storeId: Long,
    ): Response<List<MenuSimpleResponse>>

    @GET(MENU_GET_PATCH_DELETE_PATH)
    suspend fun getDetailMenu(
        @Path("menuId") menuId: Long,
    ): Response<MenuDetailResponse>

    @GET(MENU_POST_GET_PATH)
    suspend fun getCategoryMenu(
        @Query("cid") categoryId: Long,
        @Query("sid") storeId: Long,
    ): Response<List<MenuSimpleResponse>>

    @DELETE(MENU_GET_PATCH_DELETE_PATH)
    suspend fun deleteMenu(
        @Path("menuId") menuId: Long,
    ): Response<Unit>

    @POST(OPTION_POST_PATH)
    suspend fun postOption(
        @Path("menuId") menuId: Long,
        @Body optionRequest: OptionPostRequest,
    ): Response<MenuOptionResponse>

    @PATCH(OPTION_PATCH_DELETE_PATH)
    suspend fun updateOption(
        @Path("menuId") menuId: Long,
        @Path("optionValueId") optionValueId: Long,
        @Body optionRequest: OptionPostRequest,
    ): Response<MenuOptionResponse>

    @DELETE(OPTION_PATCH_DELETE_PATH)
    suspend fun deleteOption(
        @Path("menuId") menuId: Long,
        @Path("optionValueId") optionValueId: Long,
    ): Response<Unit>

    @POST(ORDER_POST_PATH)
    suspend fun postOrder(
        @Body postOrderRequest: PostOrderRequest,
    ): Response<PostOrderResponse>

    @POST(VERIFICATE_POST_PATH)
    suspend fun verificationUser(
        @Body verificationUserRequest: VerificationUserRequest,
    ): Response<VerificationUserResponse>

    companion object {
        private const val CATEGORY_POST_GET_PATH = "/category"
        private const val CATEGORY_PATCH_DELETE_PATH = "/category/{categoryId}"
        private const val MENU_POST_GET_PATH = "/menu"
        private const val MENU_GET_PATCH_DELETE_PATH = "/menu/{menuId}"
        private const val OPTION_POST_PATH = "/menu/{menuId}/options"
        private const val OPTION_PATCH_DELETE_PATH = "/menu/{menuId}/options/{optionValueId}"
        private const val ORDER_POST_PATH = "/order"
        private const val VERIFICATE_POST_PATH = "/auth/user/kiosk-login"
    }
}