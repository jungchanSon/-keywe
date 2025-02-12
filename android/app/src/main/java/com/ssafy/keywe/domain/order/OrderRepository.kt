package com.ssafy.keywe.domain.order

import com.ssafy.keywe.data.ResponseResult

interface OrderRepository {
    suspend fun postCategory(category: CategoryModel): ResponseResult<Unit>
    suspend fun getCategory(): ResponseResult<List<CategoryModel>>
    suspend fun updateCategory(categoryId: Long, categoryName: String): ResponseResult<Unit>
    suspend fun deleteCategory(categoryId: Long): ResponseResult<Unit>

    suspend fun postMenu(menuPostModel: MenuModel): ResponseResult<MenuModel>
    suspend fun updateMenu(menuId: Long, menuPatchModel: MenuModel): ResponseResult<Unit>
    suspend fun getAllMenu(): ResponseResult<List<MenuSimpleModel>>
    suspend fun getDetailMenu(menuId: Long): ResponseResult<MenuDetailModel>
    suspend fun getCategoryMenu(categoryId: Long): ResponseResult<List<MenuSimpleModel>>
    suspend fun deleteMenu(menuId: Long): ResponseResult<Unit>

    suspend fun postOption(menuId: Long, option: OptionPostModel): ResponseResult<MenuOptionModel>
    suspend fun updateOption(menuId: Long, optionValueId: Long, option: OptionPostModel): ResponseResult<MenuOptionModel>
    suspend fun deleteOption(menuId: Long, optionValueId: Long): ResponseResult<Unit>

    suspend fun postCartItems(): ResponseResult<PostCartItemsModel>

}