package com.ssafy.keywe.presentation.order

//import com.ssafy.keywe.webrtc.data.Drag
//import com.ssafy.keywe.webrtc.data.Touch
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
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
import com.ssafy.keywe.webrtc.data.KeyWeButtonEvent
import com.ssafy.keywe.webrtc.data.STOMPTYPE
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
            MenuCartDeleteDialog(title = "통화 종료", description = "통화를 종료하시겠습니까?", onCancel = {
                appBarViewModel.closeDialog()
//                if (!isKiosk) keyWeViewModel.sendButtonEvent(KeyWeButtonEvent.CartCloseDialog)

            }, onConfirm = {
                /* 너의 action */
                disConnect(
                    context, keyWeViewModel, appBarViewModel, isKiosk, navController, tokenManager
                )
//                keyWeViewModel.sendButtonEvent(KeyWeButtonEvent.CartAcceptDialog)
            })
        }
    }




    Scaffold(topBar = {
        DefaultOrderAppBar(
            title = "주문하기",
            navController = navController,
            viewModel = appBarViewModel,
            keyWeViewModel = keyWeViewModel,
            isRoot = true,
            isKiosk = isKiosk
        )
    }, modifier = Modifier.fillMaxSize(), floatingActionButton = {
        FloatingCartButton(navController, menuCartViewModel, storeId, keyWeViewModel, isKiosk)
    }) {
        Column(
            modifier = Modifier.fillMaxHeight()
        ) {
            /// 메뉴 내용
            MenuCategoryScreen(menuViewModel, keyWeViewModel, storeId, isKiosk)
            MenuSubCategory("Popular Coffee")

            MenuMenuList(
                navController = navController,
                menuViewModel,
                menuCartViewModel,
                isKeyWe = true,
                storeId,
                keyWeViewModel = keyWeViewModel,
                isKiosk
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
        navController.navigate(BottomRoute.ProfileRoute) {
            popUpTo(BottomRoute.ProfileRoute) {
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
    keyWeViewModel: KeyWeViewModel,
    isKiosk: Boolean = false,
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
        FloatingActionButton(onClick = {
            navController.navigate(Route.MenuBaseRoute.MenuCartRoute(storeId))
            if (!isKiosk) keyWeViewModel.sendButtonEvent(KeyWeButtonEvent.MenuCart)
        },
            containerColor = Color.White,
            contentColor = Color.White,
            shape = CircleShape,
            elevation = FloatingActionButtonDefaults.elevation(0.dp),
            modifier = Modifier
                .size(48.dp)
                .border(2.dp, primaryColor, CircleShape)
                .semantics {
                    contentDescription = "menu_cart"
                }) {
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