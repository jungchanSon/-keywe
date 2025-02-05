package com.ssafy.keywe.presentation.order

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ssafy.keywe.R
import com.ssafy.keywe.common.Route
import com.ssafy.keywe.common.app.DefaultAppBar
import com.ssafy.keywe.presentation.order.component.HorizontalDivider
import com.ssafy.keywe.presentation.order.component.MenuCartBottom
import com.ssafy.keywe.presentation.order.component.MenuCartDeleteDialog
import com.ssafy.keywe.presentation.order.component.MenuCartMenuBox
import com.ssafy.keywe.presentation.order.viewmodel.CartItem
import com.ssafy.keywe.presentation.order.viewmodel.MenuViewModel
import com.ssafy.keywe.ui.theme.h6sb
import com.ssafy.keywe.ui.theme.titleTextColor
import com.ssafy.keywe.ui.theme.whiteBackgroundColor


@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun MenuCartScreen(
    navController: NavController,

    ) {

    val parentBackStackEntry = navController.getBackStackEntry<Route.MenuBaseRoute.MenuRoute>();
    val viewModel = hiltViewModel<MenuViewModel>(parentBackStackEntry)
    val cartItems by viewModel.cartItems.collectAsState()
    val isDeleteDialogOpen by viewModel.isDeleteDialogOpen.collectAsState()
    val selectedCartItem by viewModel.selectedCartItem.collectAsState()

    Box(
        modifier = Modifier
            .zIndex(1f)
            .fillMaxSize()
            .background(color = if (isDeleteDialogOpen) titleTextColor.copy(alpha = 0.5f) else Color.Transparent)
    ) {
        // ✅ Scaffold 외부에서 다이얼로그 호출
        if (isDeleteDialogOpen && selectedCartItem != null) {
            MenuCartDeleteDialog(title = "장바구니 삭제",
                description = "선택한 상품을 장바구니에서 삭제하시겠습니까?",
                onCancel = { viewModel.closeDeleteDialog() },
                onConfirm = {
                    selectedCartItem?.let { cartItem ->
                        viewModel.removeFromCart(cartItem.id)
                    }
                })
        }
    }
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
            if (cartItems.isNotEmpty()) {

                // 상단 - 스크롤이 가능한 장바구니 항목 리스트
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .padding(top = 24.dp, bottom = 152.dp),
                ) {
                    items(cartItems) { item ->
                        MenuCartMenuBox(
                            cartItem = item, viewModel = viewModel, navController = navController
                        )

                        Box(
                            modifier = Modifier
                                .height(20.dp)
                                .fillMaxWidth()
                                .padding(vertical = 10.dp),
                        ) {
                            HorizontalDivider()
                        }
                    }
                }

            } else {
                Column (
                    modifier = Modifier.fillMaxWidth().fillMaxHeight()
                        .padding(bottom = 104.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        modifier = Modifier.size(300.dp),
                        painter = painterResource(R.drawable.empty_cart),
                        contentDescription = "emptyCart",
                    )
                    Text(text = "장바구니가 비어있어요.", style = h6sb)
                }

            }
            // 하단 고정 영역
            Box(
                modifier = Modifier
                    .padding(top = 12.dp)
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
