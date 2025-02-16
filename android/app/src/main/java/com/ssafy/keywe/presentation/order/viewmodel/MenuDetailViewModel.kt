package com.ssafy.keywe.presentation.order.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.keywe.data.ResponseResult
import com.ssafy.keywe.domain.order.MenuDetailModel
import com.ssafy.keywe.domain.order.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuDetailViewModel @Inject constructor(
    private val repository: OrderRepository,
) : ViewModel() {

    private val _selectedDetailMenu = MutableStateFlow<MenuDetailModel?>(null)
    val selectedDetailMenu: StateFlow<MenuDetailModel?> = _selectedDetailMenu.asStateFlow()

    fun fetchMenuDetailById(id: Long, storeId: Long) {
        Log.d("Fetch Menu", ":$id")

        viewModelScope.launch {
            when (val result = repository.getDetailMenu(id, storeId)) {
                is ResponseResult.Success -> {
                    _selectedDetailMenu.value = result.data
                    println("getDetailMenu 결과: ${result.data}")
                }

                is ResponseResult.ServerError -> {
                    println("서버 에러: ${result.status}")  // 서버 오류 로그
                }

                is ResponseResult.Exception -> {
                    println("예외 발생: ${result.e.message}")  // 예외 로그
                }
            }
        }
        Log.d("selectDetailMenu 메뉴:", "${selectedDetailMenu.value}")
//        return selectedDetailMenu.value
    }

//    fun getExtraOptions(): List<OptionData> = extraOptions

    fun getMenuDetailModelById(id: Long): MenuDetailModel? {
        return if (selectedDetailMenu.value?.menuId == id) selectedDetailMenu.value else null
    }

    private val sizePriceMap = mapOf("Tall" to 0, "Grande" to 500, "Venti" to 1000)

}