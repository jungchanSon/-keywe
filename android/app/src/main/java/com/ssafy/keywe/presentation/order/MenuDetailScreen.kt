package com.ssafy.keywe.presentation.order

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ssafy.keywe.common.app.DefaultAppBar
import com.ssafy.keywe.presentation.order.component.MenuDetailBottom
import com.ssafy.keywe.presentation.order.component.MenuDetailCommonOption
import com.ssafy.keywe.presentation.order.component.MenuDetailExtraOption
import com.ssafy.keywe.presentation.order.component.MenuDetailMenu
import com.ssafy.keywe.presentation.order.viewmodel.MenuCartViewModel
import com.ssafy.keywe.presentation.order.viewmodel.MenuDetailViewModel
import com.ssafy.keywe.ui.theme.greyBackgroundColor
import com.ssafy.keywe.ui.theme.whiteBackgroundColor

@Composable
fun MenuDetailScreen(
    navController: NavController,
    menuDetailViewModel: MenuDetailViewModel,
    menuCartViewModel: MenuCartViewModel,
    menuId: Long
) {
    Log.d("Menu Detail", ":$menuId")

//    val menu = viewModel.fetchMenuDetailById(menuId)
    val menu by menuDetailViewModel.selectedDetailMenu.collectAsState()

    // 데이터 가져오기
    LaunchedEffect(menuId) {
        menuDetailViewModel.fetchMenuDetailById(menuId)
    }

    if (menu == null) {
        // 데이터 로딩 중이므로 기본 UI를 표시 (예: 로딩 화면)
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator() // 로딩 표시
        }
        return
    }

    val menuPrice = menu?.menuPrice ?: 0

    val selectedSize = remember { mutableStateOf("Tall") }
    val selectedTemperature = remember { mutableStateOf("Hot") }
    val extraOptions = remember { mutableStateMapOf<String, Int>() }
    val totalPrice = remember(menuPrice) { mutableStateOf(menuPrice) }
    Log.d("MenuDetail Menu totalPrice", "${totalPrice.value}")

    val commonOptionList = menu?.options?.filter { it.optionType == "Common" } ?: emptyList()
    val sizeOptions = commonOptionList.find { it.optionName.equals("size", ignoreCase = true) }
    val temperatureOptions = commonOptionList.find { it.optionName.contains("temp", ignoreCase = true) }

    val sizePriceMap = if (sizeOptions != null) {
        mapOf(
            "Tall" to 0,
            "Grande" to sizeOptions.optionPrice,
            "Venti" to (2 * sizeOptions.optionPrice)
        )
    } else {
        mapOf("Tall" to 0, "Grande" to 500, "Venti" to 1000) // 기본 가격
    }

    val sizeValues = sizeOptions?.optionsValueGroup?.map { it.optionValue } ?: listOf("Tall", "Grande", "Venti")
    val temperatureValues = temperatureOptions?.optionsValueGroup?.map { it.optionValue } ?: listOf("Hot", "Ice")

    val extraOptionList = menu?.options ?.filter { it.optionType == "Extra" } ?: emptyList()

//    val options = remember { viewModel.getExtraOptions() }

    Scaffold(
        topBar = { DefaultAppBar(title = "주문하기", navController = navController) },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
                .background(whiteBackgroundColor),
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
            ) {
                // 상단 스크롤 가능 영역
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    item {
                        MenuDetailMenu(
                            modifier = Modifier.fillMaxWidth(),
                            menuId = menuId,
                            menuPrice = totalPrice.value,
                            menuDetailViewModel
                        )
                        Spacer(modifier = Modifier
                            .height(12.dp)
                            .background(greyBackgroundColor)
                        )
                    }

                    item {
                        MenuDetailCommonOption(
                            sizeOptions = sizeValues,
                            temperatureOptions = temperatureValues,
                            onSizeSelected = { size ->
//                                val oldSizePrice = sizePriceMap[selectedSize.value] ?: 0
                                val newSizePrice = sizePriceMap[size] ?: 0

                                selectedSize.value = size

                                totalPrice.value =  menuPrice + newSizePrice +
                                        extraOptions.entries.sumOf { (optionName, quantity) ->
                                            quantity * (extraOptionList.find {
                                                it.optionsValueGroup.firstOrNull()?.optionValue == optionName
                                            }?.optionPrice ?: 0)
                                        }
                            },
                            onTemperatureSelected = { temp -> selectedTemperature.value = temp }
                        )
                        Spacer(modifier = Modifier
                            .height(12.dp)
                            .fillMaxWidth()
                            .background(greyBackgroundColor)
                        )
                    }

                    item {
                        MenuDetailExtraOption(
                            options = extraOptionList,
                            onOptionSelected = { name, newAmount, optionPrice ->
                                extraOptions[name] = newAmount
                                totalPrice.value = menuPrice + (sizePriceMap[selectedSize.value] ?: 0) +
                                        extraOptions.entries.sumOf { (optionName, quantity) ->
                                            quantity * (extraOptionList.find {
                                                it.optionsValueGroup.firstOrNull()?.optionValue == optionName
                                            }?.optionPrice ?: 0)
                                        }
                            }
                        )
                    }
                }
            }

            // 하단 고정 영역
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter) // 하단 정렬
            ) {
                MenuDetailBottom(
                    menuId = menuId,
                    selectedSize = selectedSize.value,
                    selectedTemperature = selectedTemperature.value,
                    extraOptions = extraOptions.toMap(),
                    totalPrice = totalPrice.value,
                    navController = navController,
                    menuCartViewModel = menuCartViewModel
                )
            }
        }
    }
}