package com.ssafy.keywe.presentation.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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
import com.ssafy.keywe.presentation.order.viewmodel.CartItem
import com.ssafy.keywe.presentation.order.viewmodel.OrderViewModel
import com.ssafy.keywe.presentation.order.viewmodel.OptionData
import com.ssafy.keywe.ui.theme.greyBackgroundColor
import com.ssafy.keywe.ui.theme.orangeColor
import com.ssafy.keywe.ui.theme.whiteBackgroundColor

@Composable
fun MenuDetailScreen(
    navController: NavController,
    menuId: Int,
    viewModel: OrderViewModel = hiltViewModel()
) {
    val menu = viewModel.getMenuDataById(menuId)
    val menuPrice = menu?.price ?: 0

    val selectedSize = remember { mutableStateOf("Tall") }
    val selectedTemperature = remember { mutableStateOf("Hot") }
    val extraOptions = remember { mutableStateMapOf<String, Int>() }
    val totalPrice = remember { mutableIntStateOf(menuPrice) }
    val sizePriceMap = mapOf("Tall" to 0, "Grande" to 500, "Venti" to 1000)

    val options = remember { viewModel.getExtraOptions() }

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
                ) {
                    item {
                        MenuDetailMenu(
                            modifier = Modifier.fillMaxWidth(),
                            menuId = menuId,
                            menuPrice = totalPrice.intValue,
                            viewModel
                        )
                        Spacer(modifier = Modifier
                            .height(12.dp)
                            .background(greyBackgroundColor)
                        )
                    }

                    item {
                        MenuDetailCommonOption(
                            onSizeSelected = { size ->
                                val oldSizePrice = sizePriceMap[selectedSize.value] ?: 0
                                val newSizePrice = sizePriceMap[size] ?: 0

                                selectedSize.value = size

                                totalPrice.intValue = menuPrice + newSizePrice +
                                        extraOptions.entries.sumOf { (optionName, quantity) ->
                                            quantity * (options.find { it.name == optionName }?.price ?: 0)
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
                            options = options,
                            onOptionSelected = { name, newAmount, optionPrice ->
                                extraOptions[name] = newAmount
                                totalPrice.intValue = menuPrice + sizePriceMap[selectedSize.value]!! +
                                        extraOptions.entries.sumOf { (optionName, quantity) ->
                                            quantity * (options.find { it.name == optionName }?.price ?: 0)
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
                    totalPrice = totalPrice.intValue,
                    navController = navController
                )
            }
        }
    }
}