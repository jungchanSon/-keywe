package com.ssafy.keywe.presentation.order

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ssafy.keywe.common.Route
import com.ssafy.keywe.common.app.DefaultAppBar
import com.ssafy.keywe.presentation.order.component.HorizontalDivider
import com.ssafy.keywe.presentation.order.component.MenuCartBottom
import com.ssafy.keywe.presentation.order.component.MenuCartMenuBox
import com.ssafy.keywe.presentation.order.viewmodel.CartItem
import com.ssafy.keywe.presentation.order.viewmodel.MenuViewModel
import com.ssafy.keywe.ui.theme.whiteBackgroundColor


@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun MenuCartScreen(
    navController: NavController,

    ) {

    val parentBackStackEntry = navController.getBackStackEntry<Route.MenuBaseRoute.MenuRoute>();
    val viewModel = hiltViewModel<MenuViewModel>(parentBackStackEntry)
    val cartItems by viewModel.cartItems.collectAsState()

    Scaffold(
        topBar = { DefaultAppBar(title = "장바구니", navController = navController) },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(whiteBackgroundColor),
        ) {
            // 상단 - 스크롤이 가능한 장바구니 항목 리스트
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(top = 24.dp, bottom = 152.dp),
            ) {
                items(cartItems) { item ->
                    MenuCartMenuBox(
                        cartItem = item,
                        viewModel = viewModel // ✅ ViewModel을 전달하여 관리
                    )

                    Box(
                        modifier = Modifier
                            .height(20.dp)
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 10.dp),
                    ) {
                        HorizontalDivider()
                    }
                }
            }

            // 하단 고정 영역
            Box(
                modifier = Modifier
//                    .padding(vertical = 24.dp)
                    .align(Alignment.BottomCenter)
                    .background(whiteBackgroundColor)
                    .fillMaxWidth(),
            ) {
                MenuCartBottom(cartItems.sumOf { it.quantity },
                    cartItems.sumOf { it.price * it.quantity })
            }
        }
    }
}
