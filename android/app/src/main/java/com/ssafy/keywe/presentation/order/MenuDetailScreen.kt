package com.ssafy.keywe.presentation.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ssafy.keywe.common.app.DefaultAppBar
import com.ssafy.keywe.presentation.order.component.MenuDetailBottom
import com.ssafy.keywe.presentation.order.component.MenuDetailCommonOption
import com.ssafy.keywe.presentation.order.component.MenuDetailMenu
import com.ssafy.keywe.presentation.order.component.MenuExtraOption
import com.ssafy.keywe.presentation.order.viewmodel.CartItem
import com.ssafy.keywe.presentation.order.viewmodel.MenuViewModel
import com.ssafy.keywe.ui.theme.greyBackgroundColor
import com.ssafy.keywe.ui.theme.whiteBackgroundColor

@Composable
fun MenuDetailScreen(
    navController: NavController,
    menuId: Int,
    viewModel: MenuViewModel = hiltViewModel()
) {
    val menu = viewModel.getMenuDataById(menuId)
    val menuPrice = menu?.price ?: 0

    val cartItems: MutableList<CartItem> = mutableListOf()
    val selectedSize = remember { mutableStateOf("Tall") }
    val selectedTemperature = remember { mutableStateOf("Hot") }
    val extraOptions = remember { mutableStateMapOf<String, Int>() }
    val totalPrice = remember { mutableIntStateOf(menuPrice) }

    Scaffold(
        topBar = { DefaultAppBar(title = "주문하기", navController = navController) },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
                .background(whiteBackgroundColor),
        ) {
            // 상단: MenuDetailMenu
            MenuDetailMenu(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter),
                menuId = menuId,
                menuPrice = totalPrice.intValue,
                viewModel
            )

            // 하단 고정 영역
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter) // 하단 정렬
                    .background(greyBackgroundColor)
            ) {
                Column(
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Spacer(modifier = Modifier.height(24.dp))
                    MenuDetailCommonOption(onSizeSelected = { size -> selectedSize.value = size },
                        onTemperatureSelected = { temp -> selectedTemperature.value = temp })
                    Spacer(modifier = Modifier.height(12.dp))
                    MenuExtraOption(onOptionSelected = { name, newAmount, optionPrice ->
                        val oldAmount = extraOptions[name] ?: 0
                        val priceChange = (newAmount - oldAmount) * (optionPrice)

                        extraOptions[name] = newAmount
                        totalPrice.intValue += priceChange
                    })
                    MenuDetailBottom(
                        menuId = menuId,
                        selectedSize = selectedSize.value,
                        selectedTemperature = selectedTemperature.value,
                        extraOptions = extraOptions.toMap(),
                        navController = navController
                    )
                }
            }
        }
    }
}