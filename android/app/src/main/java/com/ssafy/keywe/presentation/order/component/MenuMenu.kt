package com.ssafy.keywe.presentation.order.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.ssafy.keywe.common.Route
import com.ssafy.keywe.presentation.order.viewmodel.MenuCartViewModel
import com.ssafy.keywe.presentation.order.viewmodel.MenuViewModel
import com.ssafy.keywe.ui.theme.noRippleClickable
import com.ssafy.keywe.ui.theme.whiteBackgroundColor
import com.ssafy.keywe.webrtc.data.KeyWeButtonEvent
import com.ssafy.keywe.webrtc.viewmodel.KeyWeViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch


@OptIn(FlowPreview::class)
@Composable
fun MenuMenuList(
    navController: NavController,
    viewModel: MenuViewModel,
    menuCartViewModel: MenuCartViewModel,
    isKeyWe: Boolean = false,
    storeId: Long,
    keyWeViewModel: KeyWeViewModel,
    isKiosk: Boolean = false,
) {
    val filteredMenuList by viewModel.filteredMenuItems.collectAsState()
    val listState = rememberLazyGridState() // 스크롤 상태 관리
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(viewModel.selectedCategory.collectAsState().value) {
        coroutineScope.launch {
            listState.animateScrollToItem(
                index = 0,
            )
        }
    }


    // [변경된 부분 1] 스크롤 상태 전송 (로컬 -> 원격)
    if(!isKiosk){
        LaunchedEffect(listState) {
            snapshotFlow { listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset }
                .debounce(100) // 100ms마다 업데이트 전송
                .collect { (index, offset) ->
                    Log.d("MenuMenuList", "스크롤 상태 전송 (로컬 -> 원격) : $index, $offset")
                    // 스크롤 상태를 원격에 전송하는 함수 (구현 필요)
                    keyWeViewModel.sendScrollData(
                        firstVisibleItemIndex = index,
                        firstVisibleItemScrollOffset = offset
                    )
                }
        }
    }


    // [변경된 부분 2] 원격 스크롤 상태 수신 후 동기화 (원격 -> 로컬)
    LaunchedEffect(keyWeViewModel.remoteScrollState) {
        coroutineScope.launch {
            keyWeViewModel.remoteScrollState.collect { scrollData ->
                scrollData?.let {
                    listState.animateScrollToItem(
                        index = it.firstVisibleItemIndex,
                        scrollOffset = scrollData.firstVisibleItemScrollOffset
                    )
                }
            }
        }

    }

    // Accompanist Snapper를 이용한 스냅 플링 동작 생성
    val snapFlingBehavior = rememberSnapFlingBehavior(lazyGridState = listState, snapPosition = SnapPosition.Start)

    Box(
        modifier = Modifier
            .background(Color.Transparent)
            .fillMaxSize()
            .padding(horizontal = 24.dp),
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // 2열 그리드
            modifier = Modifier.fillMaxSize(),
            flingBehavior = snapFlingBehavior,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            state = listState
        ) {
            items(filteredMenuList) { menu ->
//                Log.d("menu data", "$menu")
                MenuMenuScreen(
                    menuId = menu.menuId,
                    selectItem = {
                        if (isKeyWe) {

                            Log.d("MenuScreen", "Click Menu = ${menu.menuId}")
                            if (!isKiosk) {
                                keyWeViewModel.sendButtonEvent(
                                    KeyWeButtonEvent.MenuSelect(
                                        menuId = menu.menuId, storeId = storeId
                                    )
                                )
                            }
                            navController.navigate(
                                Route.MenuBaseRoute.MenuDetailRoute(
                                    menu.menuId, storeId
                                )
                            )
                        } else navController.navigate(
                            Route.MenuBaseRoute.DefaultMenuDetailRoute(
                                menu.menuId, storeId
                            )
                        )
                    },
                    viewModel,
                    menuCartViewModel,
                    storeId,
                    isKiosk = isKiosk,
                    keyWeViewModel = keyWeViewModel
                )
            }
        }
    }
}

@Composable
fun MenuMenuScreen(
    menuId: Long,
    selectItem: () -> Unit,
    viewModel: MenuViewModel,
    menuCartViewModel: MenuCartViewModel,
    storeId: Long,
    isKiosk: Boolean,
    keyWeViewModel: KeyWeViewModel,
) {
//    Log.d("Menu ID", "$menuId")

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 24.dp)
        .wrapContentHeight()
        .noRippleClickable { selectItem() }
        .semantics {
            contentDescription = "menu_item_$menuId"
        }, contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp)
                .padding(bottom = 15.dp)
                .wrapContentHeight(),
            verticalArrangement = Arrangement.spacedBy(7.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
//            Log.d("menu Column", "$menuId")
            MenuImage(menuId, viewModel)

            MenuDescription(
                menuId = menuId,
                viewModel = viewModel,
                menuCartViewModel = menuCartViewModel,
                keyWeViewModel = keyWeViewModel,
                storeId = storeId,
                isKiosk = isKiosk
            )
        }

        val density = LocalDensity.current.density // Density 가져오기

        Box(
            modifier = Modifier
                .zIndex(-1f)
                .fillMaxWidth()
                .height(145.dp)
                .shadow(
                    elevation = 10.dp,
                    spotColor = Color(0x88696969),
                    ambientColor = Color(0x11696969)
                )
                .graphicsLayer(
                    rotationX = 15f, cameraDistance = 8f * density
                )
                .background(
                    whiteBackgroundColor, shape = RoundedCornerShape(24.dp)
                )
        )

    }
}