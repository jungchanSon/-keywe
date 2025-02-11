package com.ssafy.keywe.presentation.order.component

import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.ssafy.keywe.common.Route
import com.ssafy.keywe.presentation.order.viewmodel.MenuViewModel
import com.ssafy.keywe.ui.theme.noRippleClickable
import com.ssafy.keywe.ui.theme.whiteBackgroundColor
import kotlinx.coroutines.launch

@Composable
fun MenuMenuList(
    navController: NavController,
    viewModel: MenuViewModel
) {
    val filteredMenuList by viewModel.filteredMenuItems.collectAsState()
    val listState = rememberLazyGridState() // 스크롤 상태 관리
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(viewModel.selectedCategory.collectAsState().value) {
        coroutineScope.launch {
            listState.scrollToItem(0) // 카테고리가 변경될 때 첫 번째 아이템으로 스크롤
        }
    }

    Box(
        modifier = Modifier
            .background(Color.Transparent)
            .fillMaxSize()
            .padding(horizontal = 24.dp),
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // 2열 그리드
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            state = listState
        ) {
            items(filteredMenuList) { menu ->
                Log.d("menu data", "$menu")
                MenuMenuScreen(
                    menuId = menu.menuId,
                    selectItem = {
                        navController.navigate(Route.MenuBaseRoute.MenuDetailRoute(menu.menuId))
                    },
                    viewModel
                )
            }
        }
    }
}

@Composable
fun MenuMenuScreen(
    menuId: Long,
    selectItem: () -> Unit,
    viewModel: MenuViewModel
) {
    Log.d("Menu ID", "$menuId")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp)
            .wrapContentHeight()
            .noRippleClickable { selectItem() },
        contentAlignment = Alignment.BottomCenter
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
            Log.d("menu Column", "$menuId")
            MenuImage(menuId, viewModel)

            MenuDescription(menuId, viewModel)
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