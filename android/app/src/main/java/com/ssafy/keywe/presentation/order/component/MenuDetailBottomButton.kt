package com.ssafy.keywe.presentation.order.component

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ssafy.keywe.common.Route
import com.ssafy.keywe.common.app.BottomButton
import com.ssafy.keywe.presentation.order.viewmodel.MenuViewModel
import com.ssafy.keywe.ui.theme.greyBackgroundColor
import com.ssafy.keywe.ui.theme.polishedSteelColor
import com.ssafy.keywe.ui.theme.primaryColor
import com.ssafy.keywe.ui.theme.titleTextColor
import com.ssafy.keywe.ui.theme.whiteBackgroundColor

@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun MenuDetailBottom(
    menuId: Int, selectedSize: String, selectedTemperature: String, extraOptions: Map<String, Int>,
    totalPrice: Int,
    navController: NavController
) {
    val parentBackStackEntry = navController.getBackStackEntry<Route.MenuBaseRoute.MenuRoute>()
    val viewModel = hiltViewModel<MenuViewModel>(parentBackStackEntry)


    Box(modifier = Modifier.background(greyBackgroundColor)) {
        Row(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f)) {
                MenuDetailBottomBackButton(
                    "담기",
                    menuId = menuId,
                    selectedSize = selectedSize,
                    selectedTemperature = selectedTemperature,
                    extraOptions = extraOptions,
                    totalPrice = totalPrice,
                    viewModel = viewModel,
                    navController = navController
                )
            }
            Box(modifier = Modifier.weight(1f)) {
                MenuDetailBottomCartButton("주문하기", onClick = {})
            }
        }
    }
}

@Composable
fun MenuDetailBottomBackButton(
    content: String,
    menuId: Int,
    selectedSize: String,
    selectedTemperature: String,
    extraOptions: Map<String, Int>,
    totalPrice: Int,
    viewModel: MenuViewModel,
    navController: NavController
) {
    BottomButton(
        content = content, onClick = {
            viewModel.addToCart(
                menuId = menuId,
                size = selectedSize,
                temperature = selectedTemperature,
                extraOptions = extraOptions,
                totalPrice = totalPrice
            )
            navController.popBackStack()
//            navController.navigate(Route.MenuBaseRoute.MenuRoute)
        }, modifier = Modifier
            .fillMaxWidth(), colors = ButtonColors(
            containerColor = whiteBackgroundColor,
            contentColor = titleTextColor,
            disabledContentColor = polishedSteelColor,
            disabledContainerColor = greyBackgroundColor
        )
    )
}

@Composable
fun MenuDetailBottomCartButton(
    content: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
) {
    BottomButton(
        content = content,
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier

            .fillMaxWidth(),
        colors = ButtonColors(
            containerColor = primaryColor,
            contentColor = whiteBackgroundColor,
            disabledContentColor = polishedSteelColor,
            disabledContainerColor = greyBackgroundColor
        )
    )
}
