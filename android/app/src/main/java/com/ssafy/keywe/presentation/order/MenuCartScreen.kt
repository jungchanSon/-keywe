package com.ssafy.keywe.presentation.order

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ssafy.keywe.R
import com.ssafy.keywe.common.Route
import com.ssafy.keywe.common.app.DefaultOrderAppBar
import com.ssafy.keywe.presentation.order.component.HorizontalDivider
import com.ssafy.keywe.presentation.order.component.MenuCartBottom
import com.ssafy.keywe.presentation.order.component.MenuCartDeleteDialog
import com.ssafy.keywe.presentation.order.component.MenuCartFinishDialog
import com.ssafy.keywe.presentation.order.component.MenuCartMenuBox
import com.ssafy.keywe.presentation.order.viewmodel.MenuCartViewModel
import com.ssafy.keywe.presentation.order.viewmodel.OrderAppBarViewModel
import com.ssafy.keywe.ui.theme.caption
import com.ssafy.keywe.ui.theme.h6sb
import com.ssafy.keywe.ui.theme.noRippleClickable
import com.ssafy.keywe.ui.theme.polishedSteelColor
import com.ssafy.keywe.ui.theme.titleTextColor
import com.ssafy.keywe.ui.theme.whiteBackgroundColor


@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun MenuCartScreen(
    navController: NavController,
    menuCartViewModel: MenuCartViewModel,
    appBarViewModel: OrderAppBarViewModel = hiltViewModel()
) {
    val cartItems by menuCartViewModel.cartItems.collectAsState()
    Log.d("MenuCartScreen", "cartItems: $cartItems")
    val isDeleteDialogOpen by menuCartViewModel.isDeleteDialogOpen.collectAsState()
    val isCompleteOrder by menuCartViewModel.isCompleteOrder.collectAsState()
    val selectedCartItem by menuCartViewModel.selectedCartItem.collectAsState()

    val isStopCallingDialogOpen by appBarViewModel.isStopCallingDialogOpen.collectAsStateWithLifecycle()
    val isAllDeleteDialogOpen = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .zIndex(1f)
            .fillMaxSize()
            .background(
                color = if (isDeleteDialogOpen || isAllDeleteDialogOpen.value || isCompleteOrder || isStopCallingDialogOpen) titleTextColor.copy(
                    alpha = 0.5f
                ) else Color.Transparent
            )
    ) {
        // 개별 삭제 다이얼로그
        if (isDeleteDialogOpen && selectedCartItem != null) {
            MenuCartDeleteDialog(title = "장바구니 삭제",
                description = "선택한 상품을 장바구니에서 삭제하시겠습니까?",
                onCancel = { menuCartViewModel.closeDeleteDialog() },
                onConfirm = {
                    selectedCartItem?.let { cartItem ->
                        menuCartViewModel.removeFromCart(cartItem.id)
                    }
                })
        }

        // 전체 삭제 다이얼로그
        if (isAllDeleteDialogOpen.value) {
            MenuCartDeleteDialog(title = "전체 메뉴 삭제",
                description = "장바구니에 담긴 모든 메뉴를 삭제하시겠습니까?",
                onCancel = { isAllDeleteDialogOpen.value = false },
                onConfirm = {
                    menuCartViewModel.clearCart()
                    isAllDeleteDialogOpen.value = false
                })
        }

        // 주문 완료 다이얼로그
        if (isCompleteOrder) {
            MenuCartFinishDialog(title = "주문 완료", description = "주문이 정상적으로 완료되었습니다.", onConfirm = {
                menuCartViewModel.closeCompleteOrderDialog()
                navController.navigate(Route.MenuBaseRoute.MenuRoute)
            })
        }

        // 통화 종료 다이얼로그
        if (isStopCallingDialogOpen) {
            MenuCartDeleteDialog(title = "통화 종료",
                description = "통화를 종료하시겠습니까?",
                onCancel = { appBarViewModel.toggleUnconnect() },
                onConfirm = {

                    /* 너의 action */
                    appBarViewModel.toggleUnconnect()
                })
        }

    }
    Scaffold(
        topBar = {
            DefaultOrderAppBar(
                title = "장바구니", navController = navController, viewModel = appBarViewModel
            )
        }, modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(whiteBackgroundColor),
        ) {
            if (cartItems.isNotEmpty()) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.End // 텍스트를 오른쪽 정렬
                ) {
                    Text(text = "전체 삭제",
                        style = caption.copy(fontSize = 14.sp, letterSpacing = 0.em),
                        color = polishedSteelColor,
                        modifier = Modifier.noRippleClickable {
                                isAllDeleteDialogOpen.value = true
                            })
                }


                // 상단 - 스크롤이 가능한 장바구니 항목 리스트
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .padding(top = 24.dp, bottom = 130.dp),
                ) {
                    items(cartItems, key = { it.id }) { item ->
                        MenuCartMenuBox(
                            cartItem = item, viewModel = menuCartViewModel
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
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
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
                    cartItems.sumOf { it.price * it.quantity },
                    menuCartViewModel
                )
            }
        }
    }
}
