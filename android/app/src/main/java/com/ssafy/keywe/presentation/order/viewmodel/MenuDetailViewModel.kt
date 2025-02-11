package com.ssafy.keywe.presentation.order.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.keywe.data.ResponseResult
import com.ssafy.keywe.domain.order.MenuDetailModel
import com.ssafy.keywe.domain.order.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuDetailViewModel @Inject constructor(
    private val repository: OrderRepository,
) : ViewModel() {

//    private val extraOptions = listOf(
//        OptionData("샷 추가", 500),
//        OptionData("시럽 추가", 300),
//        OptionData("바닐라 시럽 추가", 300),
//        OptionData("휘핑 추가", 700),
//        OptionData("샷 추가1", 500),
//        OptionData("시럽 추가1", 300),
//        OptionData("바닐라 시럽 추가1", 300),
//        OptionData("휘핑 추가1", 700),
//        OptionData("샷 추가2", 500),
//        OptionData("시럽 추가2", 300),
//        OptionData("바닐라 시럽 추가2", 300),
//        OptionData("휘핑 추가2", 700),
//        OptionData("샷 추가3", 500),
//        OptionData("시럽 추가3", 300),
//        OptionData("바닐라 시럽 추가3", 300),
//        OptionData("휘핑 추가3", 700),
//    )

    private val _selectedDetailMenu = MutableStateFlow<MenuDetailModel?>(null)
    val selectedDetailMenu: StateFlow<MenuDetailModel?> = _selectedDetailMenu.asStateFlow()

    fun     fetchMenuDetailById(id: Long): MenuDetailModel? {
        Log.d("Fetch Menu", ":$id")

        viewModelScope.launch {
            when (val result = repository.getDetailMenu(id)) {
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
        return selectedDetailMenu.value
    }

//    fun getExtraOptions(): List<OptionData> = extraOptions

    private val sizePriceMap = mapOf("Tall" to 0, "Grande" to 500, "Venti" to 1000)

}