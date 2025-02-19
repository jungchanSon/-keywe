package com.ssafy.keywe.presentation.order

//import com.ssafy.keywe.webrtc.data.Drag
//import com.ssafy.keywe.webrtc.data.Touch
import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ssafy.keywe.R
import com.ssafy.keywe.common.BottomRoute
import com.ssafy.keywe.common.Route
import com.ssafy.keywe.common.app.DefaultOrderAppBar
import com.ssafy.keywe.data.TokenManager
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
import com.ssafy.keywe.ui.theme.primaryColor
import com.ssafy.keywe.ui.theme.titleTextColor
import com.ssafy.keywe.ui.theme.whiteBackgroundColor
import com.ssafy.keywe.webrtc.data.KeyWeButtonEvent
import com.ssafy.keywe.webrtc.data.STOMPTYPE
import com.ssafy.keywe.webrtc.screen.closeSTOMP
import com.ssafy.keywe.webrtc.viewmodel.KeyWeViewModel
import com.ssafy.keywe.webrtc.viewmodel.SignalViewModel


@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun MenuCartScreen(
    navController: NavController,
    menuCartViewModel: MenuCartViewModel,
    appBarViewModel: OrderAppBarViewModel = hiltViewModel(),
    keyWeViewModel: KeyWeViewModel,
    signalViewModel: SignalViewModel = hiltViewModel(),
    tokenManager: TokenManager,
    storeId: Long,
) {
    val cartItems by menuCartViewModel.cartItems.collectAsState()
    Log.d("MenuCartScreen", "cartItems: $cartItems")
    val isDeleteDialogOpen by menuCartViewModel.isDeleteDialogOpen.collectAsState()
    val isCompleteOrder by menuCartViewModel.isCompleteOrder.collectAsState()
    val selectedCartItem by menuCartViewModel.selectedCartItem.collectAsState()

    val isStopCallingDialogOpen by appBarViewModel.isStopCallingDialogOpen.collectAsStateWithLifecycle()
    val isAllDeleteDialogOpen = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val isKiosk = tokenManager.isKiosk
    val message by signalViewModel.stompMessageFlow.collectAsStateWithLifecycle()

    BackHandler {
        if (!isKiosk) keyWeViewModel.sendButtonEvent(KeyWeButtonEvent.BackButton)
        navController.popBackStack()
    }
    LaunchedEffect(message) {
        Log.d("MenuCartScreen", "message: $message")
        message?.let {
            if (it.type == STOMPTYPE.END) {
                Log.d("WaitingRoomScreen", "종료")
                disConnect(
                    context, keyWeViewModel, appBarViewModel, isKiosk, navController, tokenManager
                )
            }
        }
    }

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
                onCancel = {
                    menuCartViewModel.closeDeleteDialog()
                    if (!isKiosk) keyWeViewModel.sendButtonEvent(KeyWeButtonEvent.CartCloseDialog)
                },
                onConfirm = {
                    selectedCartItem?.let { cartItem ->
                        menuCartViewModel.removeFromCart(cartItem.id)
                    }
                    if (!isKiosk) keyWeViewModel.sendButtonEvent(KeyWeButtonEvent.CartAcceptDialog)
                })
        }

        // 전체 삭제 다이얼로그
        if (isAllDeleteDialogOpen.value) {
            MenuCartDeleteDialog(title = "전체 메뉴 삭제",
                description = "장바구니에 담긴 모든 메뉴를 삭제하시겠습니까?",
                onCancel = {
                    isAllDeleteDialogOpen.value = false
                    if (!isKiosk) keyWeViewModel.sendButtonEvent(KeyWeButtonEvent.CartCloseDialog)
                },
                onConfirm = {
                    menuCartViewModel.clearCart()
                    isAllDeleteDialogOpen.value = false
                    if (!isKiosk) keyWeViewModel.sendButtonEvent(KeyWeButtonEvent.CartAcceptDialog)

                })
        }

        // 주문 완료 다이얼로그
        if (isCompleteOrder) {
            MenuCartFinishDialog(title = "주문 완료", description = "주문이 정상적으로 완료되었습니다.", onConfirm = {
                menuCartViewModel.clearCart()
                menuCartViewModel.closeCompleteOrderDialog()
                closeSTOMP(context)
                keyWeViewModel.exit()
                tokenManager.clearKeyWeToken()
                Log.d("MenuScreen", "종료")
                Toast.makeText(context, "대리주문이 종료됩니다.", Toast.LENGTH_LONG).show()
                // 키오스크
                if (isKiosk) {
                    Log.d("Back", "Back")
                    navController.navigate(Route.MenuBaseRoute.KioskHomeRoute) {
                        popUpTo(Route.MenuBaseRoute.KioskHomeRoute) {
                            inclusive = true
                        }
                    }
                } else {
                    // 사용자 홈으로
                    navController.navigate(BottomRoute.ProfileRoute) {
                        popUpTo(BottomRoute.ProfileRoute) {
                            inclusive = true
                        }
                    }
                }
        

//                navController.navigate(Route.MenuBaseRoute.MenuRoute(storeId))
//                navController.navigate(Route.MenuBaseRoute.KioskHomeRoute)
            })
        }

        // 통화 종료 다이얼로그
        if (isStopCallingDialogOpen) {
            MenuCartDeleteDialog(title = "통화 종료", description = "통화를 종료하시겠습니까?", onCancel = {
                appBarViewModel.closeDialog()
//                    if (!isKiosk) keyWeViewModel.sendButtonEvent(KeyWeButtonEvent.CartCloseDialog)

            }, onConfirm = {

                /* 너의 action */
                disConnect(
                    context, keyWeViewModel, appBarViewModel, isKiosk, navController, tokenManager
                )
//                    if (!isKiosk) keyWeViewModel.sendButtonEvent(KeyWeButtonEvent.CartAcceptDialog)

            })
        }

    }
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Scaffold(
            topBar = {
                DefaultOrderAppBar(
                    title = "장바구니",
                    navController = navController,
                    viewModel = appBarViewModel,
                    keyWeViewModel = keyWeViewModel,
                    isKiosk = isKiosk
                )
            }, modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(whiteBackgroundColor),
            ) {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .then(if (isKiosk) {
                        Modifier
                            .border(
                                width = 2.dp,
                                color = primaryColor,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .pointerInput(Unit) {
                                awaitPointerEventScope {
                                    while (true) {
                                        val event =
                                            awaitPointerEvent(PointerEventPass.Initial) // 모든 터치 이벤트 감지
                                        event.changes.forEach { it.consume() } // 터치 이벤트 소비하여 상위로 전달되지 않게 만듦
                                    }
                                }
                            }
//
                    } else {
                        Modifier // isKiosk가 false일 경우 추가적인 Modifier 없음
                    })) {
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

                                modifier = Modifier
                                    .semantics {
                                        contentDescription = "cart_open_dialog"
                                    }
                                    .noRippleClickable {
                                        isAllDeleteDialogOpen.value = true
                                        if (!isKiosk) keyWeViewModel.sendButtonEvent(
                                            KeyWeButtonEvent.CartOpenDialog
                                        )
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
                                    cartItem = item,
                                    viewModel = menuCartViewModel,
                                    storeId,
                                    keyWeViewModel,
                                    isKiosk
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
                            verticalArrangement = Arrangement.spacedBy(
                                20.dp, Alignment.CenterVertically
                            ),
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
                }

                // 하단 고정 영역
                Box(
                    modifier = Modifier
                        .padding(top = 12.dp)
//                        .align(Alignment.BottomCenter)
                        .background(whiteBackgroundColor)
                        .fillMaxWidth(),
                ) {
                    MenuCartBottom(
                        cartItems.sumOf { it.quantity },
                        cartItems.sumOf { it.price * it.quantity },
                        menuCartViewModel,
                    )
                }
            }
        }

        if (isKiosk) {
            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp)
//                    .pointerInput(Unit) {
//                        awaitPointerEventScope {
//                            while (true) {
//                                val event = awaitPointerEvent()
//                                event.changes.forEach { it.consume() } // 🔹 터치 이벤트 강제 차단
//                            }
//                        }
//                    },
//                    .background(titleTextColor.copy(alpha = 0.5f))
            ) {
                FloatingUsingButton()
            }
        } else {
        }
    }
}
