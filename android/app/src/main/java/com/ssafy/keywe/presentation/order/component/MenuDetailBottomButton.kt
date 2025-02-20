package com.ssafy.keywe.presentation.order.component

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ssafy.keywe.common.Route
import com.ssafy.keywe.common.app.BottomButton
import com.ssafy.keywe.presentation.order.viewmodel.MenuCartViewModel
import com.ssafy.keywe.ui.theme.greyBackgroundColor
import com.ssafy.keywe.ui.theme.polishedSteelColor
import com.ssafy.keywe.ui.theme.primaryColor
import com.ssafy.keywe.ui.theme.titleTextColor
import com.ssafy.keywe.ui.theme.whiteBackgroundColor
import com.ssafy.keywe.webrtc.data.KeyWeButtonEvent
import com.ssafy.keywe.webrtc.viewmodel.KeyWeViewModel
import kotlinx.coroutines.delay

@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun MenuDetailBottom(
    menuId: Long,
    selectedSize: String,
    selectedTemperature: String,
    extraOptions: Map<Long, Pair<String, Int>>,
    totalPrice: Int,
    navController: NavController,
    menuCartViewModel: MenuCartViewModel,
    storeId: Long,
    isKeyWe: Boolean = false,
    keyWeViewModel: KeyWeViewModel = hiltViewModel(),
    isKiosk: Boolean = false,
) {

//    val parentBackStackEntry =
//        if (isKeyWe) navController.getBackStackEntry<Route.MenuBaseRoute.MenuRoute>()
//        else navController.getBackStackEntry<Route.MenuBaseRoute.DefaultMenuRoute>()
//    val viewModel = hiltViewModel<MenuDetailViewModel>(parentBackStackEntry)


    Box(modifier = Modifier.background(greyBackgroundColor)) {
        Row(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f)) {
                MenuDetailBottomBackButton(
                    content = "담기",
                    menuId = menuId,
                    selectedSize = selectedSize,
                    selectedTemperature = selectedTemperature,
                    extraOptions = extraOptions,
                    totalPrice = totalPrice,
                    navController = navController,
                    menuCartViewModel = menuCartViewModel,
                    storeId = storeId,
                    keyWeViewModel = keyWeViewModel,
                    isKiosk
                )
            }
            Box(modifier = Modifier.weight(1f)) {
                MenuDetailBottomCartButton(
                    content = "주문하기",
                    menuId = menuId,
                    selectedSize = selectedSize,
                    selectedTemperature = selectedTemperature,
                    extraOptions = extraOptions,
                    totalPrice = totalPrice,
                    navController = navController,
                    menuCartViewModel = menuCartViewModel,
                    storeId = storeId,
                    isKeyWe = isKeyWe,
                    keyWeViewModel = keyWeViewModel,
                    isKiosk
                )
            }
        }
    }
}

@Composable
fun MenuDetailBottomBackButton(
    content: String,
    menuId: Long,
    selectedSize: String,
    selectedTemperature: String,
    extraOptions: Map<Long, Pair<String, Int>>,
    totalPrice: Int,
    navController: NavController,
    menuCartViewModel: MenuCartViewModel,
    storeId: Long,
    keyWeViewModel: KeyWeViewModel,
    isKiosk: Boolean = false,
) {
    val selectedOptions = if (extraOptions.isNotEmpty()) {
        extraOptions.mapValues { (_, pair) -> pair.second }
    } else {
        mapOf() // 기본값 설정
    }

//    Log.d("MenuDetailBottomBackButton", "🔥 extraOptions: $extraOptions")
//    Log.d("MenuDetailBottomBackButton", "🔥 selectedOptions: $selectedOptions")

    var addToCartTrigger by remember { mutableStateOf(false) }

    BottomButton(
        content = content, onClick = {
            Log.d("MenuDetailBottomBackButton", "🛒 addToCart 호출됨!")

            menuCartViewModel.addToCart(
                menuId = menuId,
                size = selectedSize,
                temperature = selectedTemperature,
                selectedOptions = selectedOptions,
                totalPrice = totalPrice,
                storeId = storeId
            )
            addToCartTrigger = true

            navController.popBackStack()
            if (!isKiosk) keyWeViewModel.sendButtonEvent(KeyWeButtonEvent.StoreButton)
        }, modifier = Modifier
            .fillMaxWidth()
            .semantics {
                contentDescription = "menu_detail_store_button"
            }, colors = ButtonColors(
            containerColor = whiteBackgroundColor,
            contentColor = titleTextColor,
            disabledContentColor = polishedSteelColor,
            disabledContainerColor = greyBackgroundColor
        )
    )

    LaunchedEffect(addToCartTrigger) {
        if (addToCartTrigger) {
            delay(100) // 업데이트 반영 기다리기
            Log.d(
                "MenuDetailBottomBackButton",
                "🛒 addToCart 이후 장바구니 상태: ${menuCartViewModel.cartItems.value}"
            )
            addToCartTrigger = false // 다시 초기화
        }
    }
}

@Composable
fun MenuDetailBottomCartButton(
    content: String,
    menuId: Long,
    selectedSize: String,
    selectedTemperature: String,
    extraOptions: Map<Long, Pair<String, Int>>,
    totalPrice: Int,
    navController: NavController,
    menuCartViewModel: MenuCartViewModel,
    storeId: Long,
    isKeyWe: Boolean,
    keyWeViewModel: KeyWeViewModel,
    isKiosk: Boolean = false,
) {
    val selectedOptions = if (extraOptions.isNotEmpty()) {
        extraOptions.mapValues { (_, pair) -> pair.second }
    } else {
        mapOf() // 기본값 설정
    }

//    Log.d("MenuDetailBottomBackButton", "🔥 extraOptions: $extraOptions")
//    Log.d("MenuDetailBottomBackButton", "🔥 selectedOptions: $selectedOptions")

    var addToCartTrigger by remember { mutableStateOf(false) }

    BottomButton(
        content = content, onClick = {
            Log.d("MenuDetailBottomBackButton", "🛒 addToCart 호출됨!")
            Log.d(
                "MenuDetailBottomBackButton",
                "menuId = $menuId, size = $selectedSize, temperature = $selectedTemperature, extraOptions = $extraOptions, totalPrice = $totalPrice, storeId = $storeId  selectedOptions: $selectedOptions"
            )
            menuCartViewModel.addToCart(
                menuId = menuId,
                size = selectedSize,
                temperature = selectedTemperature,
                selectedOptions = selectedOptions,
                totalPrice = totalPrice,
                storeId = storeId
            )
            addToCartTrigger = true
            if (!isKiosk) keyWeViewModel.sendButtonEvent(KeyWeButtonEvent.OrderButton)

            if (isKeyWe) {
                navController.navigate(Route.MenuBaseRoute.MenuCartRoute(storeId)) {
                    popUpTo(Route.MenuBaseRoute.MenuDetailRoute(menuId, storeId)) {
                        inclusive = true
                    }

                    launchSingleTop = true
                }
            } else {
                navController.navigate(Route.MenuBaseRoute.DefaultMenuCartRoute(storeId)) {
                    popUpTo(
                        Route.MenuBaseRoute.DefaultMenuDetailRoute(
                            menuId,
                            storeId
                        )
                    ) { inclusive = true }
                    launchSingleTop = true
                }
            }

        }, modifier = Modifier
            .fillMaxWidth()
            .semantics {
                contentDescription = "menu_detail_order_button"
            }, colors = ButtonColors(
            containerColor = primaryColor,
            contentColor = whiteBackgroundColor,
            disabledContentColor = polishedSteelColor,
            disabledContainerColor = greyBackgroundColor
        )
    )

    LaunchedEffect(addToCartTrigger) {
        if (addToCartTrigger) {
            delay(100) // 업데이트 반영 기다리기
            Log.d(
                "MenuDetailBottomBackButton",
                "🛒 addToCart 이후 장바구니 상태: ${menuCartViewModel.cartItems.value}"
            )
            addToCartTrigger = false // 다시 초기화
        }
    }
}
