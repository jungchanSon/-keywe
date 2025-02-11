package com.ssafy.keywe.presentation.order.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.keywe.data.ResponseResult
import com.ssafy.keywe.domain.order.CategoryModel
import com.ssafy.keywe.domain.order.MenuDetailModel
import com.ssafy.keywe.domain.order.MenuSimpleModel
import com.ssafy.keywe.domain.order.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MenuData(
    val id: Long,
    val category: String,
    val name: String,
    val recipe: String,
    val price: Int,
    val description: String,
    val image: String
)

data class MenuCategory(
    val id: Long,
    val name: String
)

//data class CartItem(
//    val id: Long,
//    val menuId: Long,
//    val name: String,
//    val price: Int,
//    val quantity: Int,
//    val image: String?,
//    val size: String,
//    val temperature: String,
//    val extraOptions: Map<String, Int>
//)

data class OptionData(
    val name: String,
    val price: Int,
)

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val repository: OrderRepository
) : ViewModel() {

    init {
            getAllCategories()
            getMenuByCategory("전체")
    }

    private val _categories = MutableStateFlow(listOf(CategoryModel(0, "전체")))
    val categories: StateFlow<List<CategoryModel>> = _categories.asStateFlow()

    private val _selectedCategory = MutableStateFlow("전체")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _filteredMenuItems = MutableStateFlow<List<MenuSimpleModel>>(emptyList())
    val filteredMenuItems: StateFlow<List<MenuSimpleModel>> = _filteredMenuItems.asStateFlow()

    private fun getAllCategories() {
        viewModelScope.launch {
            when (val result = repository.getCategory()) {
                is ResponseResult.Success -> {
                    val categoriesWithAll = listOf(CategoryModel(0, "전체")) + result.data
                    _categories.value = categoriesWithAll
                    println("getCategory 결과: ${result.data}")
                }

                is ResponseResult.ServerError -> {
                    println("서버 에러: ${result.status}")  // 서버 오류 로그
                }

                is ResponseResult.Exception -> {
                    println("예외 발생: ${result.e.message}")  // 예외 로그
                }
            }
        }
    }

    fun setSelectedCategory(category: String) {
        _selectedCategory.value = category
        getMenuByCategory(category)
    }

    private fun getMenuByCategory(category: String) {
        viewModelScope.launch {
            if (category == "전체") {
                // 전체 카테고리 선택 시 모든 메뉴 가져오기
                when (val result = repository.getAllMenu()) {
                    is ResponseResult.Success -> {
                        _filteredMenuItems.value = result.data
                        println("getAllMenu 결과: ${result.data}")
                    }

                    is ResponseResult.ServerError -> {
                        println("서버 에러: ${result.status}")
                    }

                    is ResponseResult.Exception -> {
                        println("예외 발생: ${result.e.message}")
                    }
                }
            } else {
                Log.d("Category Selected Screen", "HI")
                val categoryId =
                    _categories.value.find { it.categoryName == category }?.categoryId ?: 0
                Log.d("Category Selected Screen", "${categories.value}")

                when (val result = repository.getCategoryMenu(categoryId)) {
                    is ResponseResult.Success -> {
                        _filteredMenuItems.value = result.data
                        println("getCategoryMenu 결과: ${result.data}")
                    }

                    is ResponseResult.ServerError -> {
                        println("서버 에러: ${result.status}")
                    }

                    is ResponseResult.Exception -> {
                        println("예외 발생: ${result.e.message}")
                    }
                }
            }
        }
    }

    fun getMenuSimpleModelById(id: Long): MenuSimpleModel? {
        return filteredMenuItems.value.find { it.menuId == id }
    }
}
