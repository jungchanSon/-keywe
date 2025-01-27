package com.ssafy.keywe.presentation.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ssafy.keywe.common.app.DefaultAppBar
import com.ssafy.keywe.presentation.order.component.MenuDetailBottom
import com.ssafy.keywe.presentation.order.component.MenuDetailCommonOption
import com.ssafy.keywe.presentation.order.component.MenuDetailMenu
import com.ssafy.keywe.presentation.order.component.MenuExtraOption
import com.ssafy.keywe.presentation.order.component.Spacer
import com.ssafy.keywe.ui.theme.whiteBackgroundColor

@Composable
fun MenuDetailScreen(navController: NavController) {
    Scaffold(
        topBar = { DefaultAppBar(title = "주문하기", navController = navController) },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(whiteBackgroundColor),
        ) {
            // 상단: MenuDetailMenu
            MenuDetailMenu(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter) // 상단 정렬
            )

            // 하단 고정 영역
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(370.dp) // 고정 높이
                    .align(Alignment.BottomCenter) // 하단 정렬
                    .background(whiteBackgroundColor)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom
                ) {
                    Spacer(24)
                    MenuDetailCommonOption()
                    Spacer(12)
                    MenuExtraOption()
                    MenuDetailBottom()
                }
            }
        }
    }
}