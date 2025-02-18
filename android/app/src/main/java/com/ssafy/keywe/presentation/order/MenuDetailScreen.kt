package com.ssafy.keywe.presentation.order

//import com.ssafy.keywe.webrtc.data.Drag
//import com.ssafy.keywe.webrtc.data.Touch
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ssafy.keywe.common.app.DefaultOrderAppBar
import com.ssafy.keywe.data.TokenManager
import com.ssafy.keywe.domain.order.OptionsModel
import com.ssafy.keywe.presentation.order.component.MenuCartDeleteDialog
import com.ssafy.keywe.presentation.order.component.MenuDetailBottom
import com.ssafy.keywe.presentation.order.component.MenuDetailCommonOption
import com.ssafy.keywe.presentation.order.component.MenuDetailExtraOption
import com.ssafy.keywe.presentation.order.component.MenuDetailMenu
import com.ssafy.keywe.presentation.order.viewmodel.MenuCartViewModel
import com.ssafy.keywe.presentation.order.viewmodel.MenuDetailViewModel
import com.ssafy.keywe.presentation.order.viewmodel.OrderAppBarViewModel
import com.ssafy.keywe.ui.theme.greyBackgroundColor
import com.ssafy.keywe.ui.theme.titleTextColor
import com.ssafy.keywe.ui.theme.whiteBackgroundColor
import com.ssafy.keywe.webrtc.data.STOMPTYPE
import com.ssafy.keywe.webrtc.viewmodel.KeyWeViewModel
import com.ssafy.keywe.webrtc.viewmodel.SignalViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MenuDetailScreen(
    navController: NavController,
    menuDetailViewModel: MenuDetailViewModel,
    menuCartViewModel: MenuCartViewModel,
    appBarViewModel: OrderAppBarViewModel,
    menuId: Long,
    keyWeViewModel: KeyWeViewModel,
    signalViewModel: SignalViewModel,
    tokenManager: TokenManager,
    storeId: Long,
    isKiosk: Boolean = false
) {
    Log.d("Menu Detail", ":$menuId")
    val context = LocalContext.current

    val menu by menuDetailViewModel.selectedDetailMenu.collectAsState()
    val isKiosk = tokenManager.isKiosk
    val message by signalViewModel.stompMessageFlow.collectAsStateWithLifecycle()

    // 데이터 가져오기
    LaunchedEffect(menuId) {
        menuDetailViewModel.fetchMenuDetailById(menuId, storeId)
    }

    if (menu == null) {
        // 데이터 로딩 중이므로 기본 UI를 표시 (예: 로딩 화면)
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator() // 로딩 표시
        }
        return
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


    val menuPrice = menu?.menuPrice ?: 0

    var selectedSize = remember { mutableStateOf("Tall") }
    var selectedTemperature = remember { mutableStateOf("Hot") }

    val extraOptions = remember { mutableStateMapOf<Long, Pair<String, Int>>() }
    val totalPrice = remember(menuPrice) { mutableStateOf(menuPrice) }
    Log.d("MenuDetail Menu totalPrice", "${totalPrice.value}")

    val commonOptionList = menu?.options?.filter { it.optionType == "Common" } ?: emptyList()
    val sizeOptions = commonOptionList.find { it.optionName.equals("size", ignoreCase = true) }
    val temperatureOptions =
        commonOptionList.find { it.optionName.contains("temp", ignoreCase = true) }

    val sizePriceMap = if (sizeOptions != null) {
        mapOf(
            "Tall" to 0,
            "Grande" to sizeOptions.optionPrice,
            "Venti" to (2 * sizeOptions.optionPrice)
        )
    } else {
        mapOf("Tall" to 0, "Grande" to 500, "Venti" to 1000) // 기본 가격
    }

    val sizeValues =
        sizeOptions?.optionsValueGroup?.map { it.optionValue } ?: listOf("Tall", "Grande", "Venti")
    val temperatureValues =
        temperatureOptions?.optionsValueGroup?.map { it.optionValue } ?: listOf("Hot", "Ice")

    val extraOptionList = menu?.options?.filter { it.optionType == "Extra" } ?: emptyList()

    val sortedExtraOptions =
        extraOptionList.sortedWith(compareBy<OptionsModel> { it.optionType }.thenBy {
            val match = Regex("\\d+").find(it.optionName)
            match?.value?.toIntOrNull() ?: Int.MAX_VALUE
        })

    val isStopCallingDialogOpen by appBarViewModel.isStopCallingDialogOpen.collectAsStateWithLifecycle()

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
//                    if (!isKiosk) keyWeViewModel.sendButtonEvent(KeyWeButtonEvent.CartCloseDialog)

            }, onConfirm = {
                /* 너의 action */
                disConnect(
                    context,
                    keyWeViewModel,
                    appBarViewModel,
                    isKiosk,
                    navController,
                    tokenManager
                )
//                    if (!isKiosk) keyWeViewModel.sendButtonEvent(KeyWeButtonEvent.CartAcceptDialog)

            })
        }
    }

    Scaffold(
        topBar = {
            DefaultOrderAppBar(
                title = "주문하기",
                navController = navController,
                viewModel = appBarViewModel,
                keyWeViewModel = keyWeViewModel
            )
        }, modifier = Modifier.fillMaxSize()
//        .pointerInteropFilter { motionEvent ->
//            when (motionEvent.action) {
//                MotionEvent.ACTION_DOWN -> {
////                val x = ScreenRatioUtil.pixelToDp(motionEvent.x, density)
////                val y = ScreenRatioUtil.pixelToDp(motionEvent.y, density)
//
//                    Log.d(
//                        "sendGesture",
//                        "실제 클릭한 위치 x PX = ${motionEvent.x} y DP = ${motionEvent.y}"
//                    )
////                Log.d("sendGesture", "실제 클릭한 위치 x DP = ${x} y DP = ${y}")
//                    if (!isKiosk) keyWeViewModel.sendClickGesture(
//                        Touch(
//                            MessageType.Touch, motionEvent.x, motionEvent.y,
//                        )
//                    )
//                    println("Tapped at x=${motionEvent.x}, y=${motionEvent.y}")
//                }
//
//                MotionEvent.ACTION_MOVE -> {
//                    if (!isKiosk) keyWeViewModel.sendClickGesture(
//                        Drag(
//                            MessageType.Drag, motionEvent.x, motionEvent.y,
//                        )
//                    )
//                    println("Moved at x=${motionEvent.x}, y=${motionEvent.y}")
//                }
//
//                else -> {
//                    Log.d("MotionEvent", "click")
//                }
//            }
//            false
//        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
                .background(whiteBackgroundColor),
        ) {
            Column(
                modifier = Modifier.align(Alignment.TopCenter)
            ) {
                // 상단 스크롤 가능 영역
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    item {
                        MenuDetailMenu(
                            modifier = Modifier.fillMaxWidth(),
                            menuId = menuId,
                            menuPrice = totalPrice.value,
                            menuDetailViewModel,
                            storeId,
                        )
                        Spacer(
                            modifier = Modifier
                                .height(12.dp)
                                .background(greyBackgroundColor)
                        )
                    }

                    item {
                        MenuDetailCommonOption(
                            sizeOptions = sizeValues,
                            temperatureOptions = temperatureValues,
                            selectedSize = selectedSize.value, // 🔹 MutableState<String> 자체를 전달
                            selectedTemperature = selectedTemperature.value,
                            onSizeSelected = { size ->
                                selectedSize.value = size
                                totalPrice.value = menuPrice + (sizePriceMap[selectedSize.value]
                                    ?: 0) + extraOptions.entries.sumOf { (optionId, pair) ->
                                    val optionPrice = extraOptionList.find { option ->
                                        option.optionsValueGroup.any { it.optionValueId == optionId }
                                    }?.optionPrice ?: 0
                                    optionPrice * pair.second
                                }
                            },
                            onTemperatureSelected = { temp -> selectedTemperature.value = temp },
                            isKiosk = isKiosk,
                            keyWeViewModel = keyWeViewModel
                        )
                        Spacer(
                            modifier = Modifier
                                .height(12.dp)
                                .fillMaxWidth()
                                .background(greyBackgroundColor)
                        )
                    }

                    item {

                        MenuDetailExtraOption(
                            options = sortedExtraOptions,
                            onOptionSelected = { id, name, newAmount ->
                                if (newAmount == 0) extraOptions.remove(id)
                                else extraOptions[id] = name to newAmount
                            },
                            isKiosk = isKiosk,
                            keyWeViewModel = keyWeViewModel
                        )

                        totalPrice.value = menuPrice + (sizePriceMap[selectedSize.value]
                            ?: 0) + extraOptions.entries.sumOf { (optionId, pair) ->
                            val optionPrice = extraOptionList.find { option ->
                                option.optionsValueGroup.any { it.optionValueId == optionId }
                            }?.optionPrice ?: 0
                            optionPrice * pair.second
                        }
                    }
                }
            }

            // 하단 고정 영역
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter) // 하단 정렬
            ) {
                MenuDetailBottom(
                    menuId = menuId,
                    selectedSize = selectedSize.value,
                    selectedTemperature = selectedTemperature.value,
                    extraOptions = extraOptions.mapValues { it.value },
                    totalPrice = totalPrice.value,
                    navController = navController,
                    menuCartViewModel = menuCartViewModel,
                    storeId = storeId,
                    isKeyWe = true
                )
            }
        }
    }
}