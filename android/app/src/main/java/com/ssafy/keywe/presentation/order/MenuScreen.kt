package com.ssafy.keywe.presentation.order

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ssafy.keywe.R
import com.ssafy.keywe.common.Route
import com.ssafy.keywe.common.app.DefaultAppBar
import com.ssafy.keywe.presentation.order.component.MenuCategoryScreen
import com.ssafy.keywe.presentation.order.component.MenuMenuList
import com.ssafy.keywe.presentation.order.component.MenuSubCategory
import com.ssafy.keywe.presentation.order.viewmodel.MenuCartViewModel
import com.ssafy.keywe.presentation.order.viewmodel.MenuViewModel
import com.ssafy.keywe.ui.theme.primaryColor
import com.ssafy.keywe.ui.theme.whiteBackgroundColor

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MenuScreen(
    navController: NavController, menuViewModel: MenuViewModel = hiltViewModel(), menuCartViewModel: MenuCartViewModel
) {
//    val cartViewModel: MenuCartViewModel = hiltViewModel()

    Scaffold(topBar = { DefaultAppBar(title = "주문하기", navController = navController) },
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingCartButton(navController, menuCartViewModel)
        }) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
        ) {
            MenuCategoryScreen(menuViewModel)
            MenuSubCategory("Popular Coffee")

            MenuMenuList(
                navController = navController, menuViewModel, menuCartViewModel
            )
        }
    }
}

@Composable
fun FloatingCartButton(navController: NavController, menuCartViewModel: MenuCartViewModel = hiltViewModel()) {
//    val cartItemCount by viewModel.cartItemCount.collectAsState()
    val cartItems by menuCartViewModel.cartItems.collectAsState()
    val cartItemsCount = cartItems.sumOf { it.quantity }

    Box(
        contentAlignment = Alignment.TopEnd, // 오른쪽 상단 정렬
        modifier = Modifier
            .size(48.dp) // FloatingActionButton 크기와 동일하게 설정
            .background(whiteBackgroundColor, shape = CircleShape)
            .shadow(4.dp, CircleShape, clip = false)
    ) {
        FloatingActionButton(
            onClick = { navController.navigate(Route.MenuBaseRoute.MenuCartRoute) },
            containerColor = Color.White,
            contentColor = Color.White,
            shape = CircleShape,
            elevation = FloatingActionButtonDefaults.elevation(0.dp),
            modifier = Modifier.size(48.dp)
                .border(2.dp, primaryColor, CircleShape)
        ) {
            Image(
                modifier = Modifier.background(whiteBackgroundColor),
                painter = painterResource(R.drawable.outline_shopping_cart_24),
                contentDescription = "Cart Button"
            )
        }
    }

    if (cartItemsCount > 0) {
        Box(
            modifier = Modifier
                .size(17.dp) // 뱃지 크기 조정
                .offset(30.dp, 0.dp)
                .background(primaryColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (cartItemsCount < 100) cartItemsCount.toString() else "99+",
                color = whiteBackgroundColor,
                fontSize = if (cartItemsCount < 100) 12.sp else 9.sp
            )
        }
    }
}