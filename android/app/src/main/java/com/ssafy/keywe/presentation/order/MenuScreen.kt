package com.ssafy.keywe.presentation.order

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
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
import com.ssafy.keywe.presentation.order.component.MenuCartDeleteDialog
import com.ssafy.keywe.presentation.order.component.MenuCategoryScreen
import com.ssafy.keywe.presentation.order.component.MenuMenuList
import com.ssafy.keywe.presentation.order.component.MenuSubCategory
import com.ssafy.keywe.presentation.order.viewmodel.MenuCartViewModel
import com.ssafy.keywe.presentation.order.viewmodel.MenuViewModel
import com.ssafy.keywe.presentation.order.viewmodel.OrderAppBarViewModel
import com.ssafy.keywe.ui.theme.primaryColor
import com.ssafy.keywe.ui.theme.titleTextColor
import com.ssafy.keywe.ui.theme.whiteBackgroundColor
import com.ssafy.keywe.webrtc.data.Drag
import com.ssafy.keywe.webrtc.data.MessageType
import com.ssafy.keywe.webrtc.data.STOMPTYPE
import com.ssafy.keywe.webrtc.data.Touch
import com.ssafy.keywe.webrtc.screen.closeSTOMP
import com.ssafy.keywe.webrtc.viewmodel.KeyWeViewModel
import com.ssafy.keywe.webrtc.viewmodel.SignalViewModel

@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MenuScreen(
    navController: NavController,
    menuViewModel: MenuViewModel = hiltViewModel(),
    menuCartViewModel: MenuCartViewModel = hiltViewModel(),
    appBarViewModel: OrderAppBarViewModel = hiltViewModel(),
    keyWeViewModel: KeyWeViewModel,
    signalViewModel: SignalViewModel = hiltViewModel(),
    tokenManager: TokenManager,
    storeId: Long,
) {

    Log.d("MenuSCreen", "storeId = $storeId")
    val message by signalViewModel.stompMessageFlow.collectAsStateWithLifecycle()
    val isStopCallingDialogOpen by appBarViewModel.isStopCallingDialogOpen.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val isKiosk = tokenManager.isKiosk

    BackHandler {
        // 여기서 로컬에서는 아무 네비게이션 동작을 하지 않고,
        // 원격 기기에 뒤로가기 명령을 전송합니다.
        appBarViewModel.openDialog()
    }

    LaunchedEffect(message) {
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
                color = if (isStopCallingDialogOpen) titleTextColor.copy(
                    alpha = 0.5f
                ) else Color.Transparent
            )
    ) {
        // 통화 종료 다이얼로그
        if (isStopCallingDialogOpen) {
            MenuCartDeleteDialog(title = "통화 종료",
                description = "통화를 종료하시겠습니까?",
                onCancel = { appBarViewModel.closeDialog() },
                onConfirm = {
                    /* 너의 action */
                    disConnect(
                        context,
                        keyWeViewModel,
                        appBarViewModel,
                        isKiosk,
                        navController,
                        tokenManager
                    )
                })
        }
    }




    Scaffold(topBar = {
        DefaultOrderAppBar(
            title = "주문하기",
            navController = navController,
            viewModel = appBarViewModel,
            keyWeViewModel = keyWeViewModel,
            isRoot = true
        )
    }, modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            awaitEachGesture {
                // 첫 터치 다운 이벤트를 감지하되, 소비하지 않음 (requireUnconsumed = false)
                val down = awaitFirstDown(requireUnconsumed = false)
                val startPosition = down.position
                Log.d("ParentGesture", "Down at: $startPosition")
                var isDragging = false

                // 터치가 계속되는 동안 이벤트를 관찰
                do {
                    // Initial 패스로 이벤트를 관찰 (이벤트 소비 X)
                    val event = awaitPointerEvent(PointerEventPass.Initial)
                    event.changes.forEach { change ->
                        // 터치 이동이 발생하면 드래그로 판단
                        val delta = change.positionChange()
                        if (delta != Offset.Zero) {
                            isDragging = true
                            Log.d(
                                "ParentGesture",
                                "Dragging: current x=${change.position.x}, y=${change.position.y}, delta=(${
                                    delta.x
                                }, ${delta.y})"
                            )
                        }
                    }
                    // 터치가 여전히 진행 중이면 반복
                } while (event.changes.any { it.pressed })

                // 드래그가 없었으면 클릭으로 간주
                if (!isDragging) {
                    Log.d("ParentGesture", "Click detected at: $startPosition")
                }
            }
        }
        .pointerInteropFilter { motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
//                val x = ScreenRatioUtil.pixelToDp(motionEvent.x, density)
//                val y = ScreenRatioUtil.pixelToDp(motionEvent.y, density)

                    Log.d(
                        "sendGesture",
                        "실제 클릭한 위치 x PX = ${motionEvent.x} y DP = ${motionEvent.y}"
                    )
//                Log.d("sendGesture", "실제 클릭한 위치 x DP = ${x} y DP = ${y}")
                    if (!isKiosk) keyWeViewModel.sendClickGesture(
                        Touch(
                            MessageType.Touch, motionEvent.x, motionEvent.y,
                        )
                    )
                    println("Tapped at x=${motionEvent.x}, y=${motionEvent.y}")
                }

                MotionEvent.ACTION_MOVE -> {
                    if (!isKiosk) keyWeViewModel.sendClickGesture(
                        Drag(
                            MessageType.Drag, motionEvent.x, motionEvent.y,
                        )
                    )
                    println("Moved at x=${motionEvent.x}, y=${motionEvent.y}")
                }

                else -> {
                    Log.d("MotionEvent", "click")
                }
            }
            false
        },


        floatingActionButton = {
            FloatingCartButton(navController, menuCartViewModel, storeId)
        }) {
        Column(
            modifier = Modifier.fillMaxHeight()
        ) {
            /// 메뉴 내용
            MenuCategoryScreen(menuViewModel, storeId)
            MenuSubCategory("Popular Coffee")

            MenuMenuList(
                navController = navController,
                menuViewModel,
                menuCartViewModel,
                isKeyWe = true,
                storeId
            )
        }
    }
}

fun disConnect(
    context: Context,
    keyWeViewModel: KeyWeViewModel,
    appBarViewModel: OrderAppBarViewModel,
    isKiosk: Boolean,
    navController: NavController,
    tokenManager: TokenManager,
) {
    closeSTOMP(context)
    keyWeViewModel.exit()
    appBarViewModel.closeDialog()
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
        navController.navigate(BottomRoute.HomeRoute) {
            popUpTo(navController.graph.startDestinationId) {
                inclusive = true
            }
        }
    }
}

@Composable
fun FloatingCartButton(
    navController: NavController,
    menuCartViewModel: MenuCartViewModel = hiltViewModel(),
    storeId: Long,
) {
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
            onClick = { navController.navigate(Route.MenuBaseRoute.MenuCartRoute(storeId)) },
            containerColor = Color.White,
            contentColor = Color.White,
            shape = CircleShape,
            elevation = FloatingActionButtonDefaults.elevation(0.dp),
            modifier = Modifier
                .size(48.dp)
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