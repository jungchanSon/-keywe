package com.ssafy.keywe.presentation.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.navigation.NavController
import com.ssafy.keywe.common.app.DefaultAppBar
import com.ssafy.keywe.presentation.order.component.MenuDetailBottom
import com.ssafy.keywe.presentation.order.component.MenuDetailCommonOption
import com.ssafy.keywe.presentation.order.component.MenuDetailMenu
import com.ssafy.keywe.presentation.order.component.MenuExtraOption
import com.ssafy.keywe.presentation.order.component.Spacer
import com.ssafy.keywe.ui.theme.whiteBackgroundColor

@Composable
fun MenuDetailScreen(
    navController: NavController,
    menuId: Int,
) {
    val menuName: String = ""
    val menuPrice: Int = 10000
    val menuImageURL: String = ""
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
                menuName = menuName,
                menuPrice = totalPrice.intValue,
                menuImageURL = menuImageURL
            )

            // 하단 고정 영역
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(370.dp) // 고정 높이
                    .align(Alignment.BottomCenter) // 하단 정렬
                    .background(whiteBackgroundColor)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom
                ) {
                    Spacer(24)
                    MenuDetailCommonOption(onSizeSelected = { size -> selectedSize.value = size },
                        onTemperatureSelected = { temp -> selectedTemperature.value = temp })
                    Spacer(12)
                    MenuExtraOption(onOptionSelected = { name, newAmount, optionPrice ->
                        val oldAmount = extraOptions[name] ?: 0
                        val priceChange = (newAmount - oldAmount) * (optionPrice)

                        extraOptions[name] = newAmount
                        totalPrice.intValue += priceChange
                    })
                    MenuDetailBottom(
                        onAddToCart = {
                            val newCartItem = CartItem(
                                id = cartItems.size + 1,
                                name = menuName,
                                price = totalPrice.value,
                                quantity = 1,
                                imageURL = menuImageURL,
                                size = selectedSize.value,
                                temperature = selectedTemperature.value,
                                extraOptions = extraOptions.toMap()
                            )

                            val existingItemIndex = cartItems.indexOfFirst {
                                it.name == newCartItem.name && it.size == newCartItem.size && it.temperature == newCartItem.temperature && it.extraOptions == newCartItem.extraOptions
                            }

                            if (existingItemIndex != -1) {
                                cartItems[existingItemIndex] = cartItems[existingItemIndex].copy(
                                    quantity = cartItems[existingItemIndex].quantity + 1
                                )
                            } else {
                                cartItems.add(newCartItem)
                            }
                        }, navController = navController
                    )
                }
            }
        }
    }
}