package com.ssafy.keywe.presentation.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ssafy.keywe.common.app.DefaultAppBar
import com.ssafy.keywe.presentation.order.component.MenuCartBottom
import com.ssafy.keywe.ui.theme.whiteBackgroundColor

@Composable
fun MenuCartScreen(navController: NavController) {
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
            // 상단
//            MenuCartMenu(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .align(Alignment.TopCenter) // 상단 정렬
//            )

            // 하단 고정 영역
            Box(
                modifier = Modifier
//                        .fillMaxSize()
//                        .height(152.dp)
                    .padding(innerPadding)
                    .padding(vertical = 24.dp)
                    .background(whiteBackgroundColor)
                    .align(Alignment.BottomCenter),
            ) {
                MenuCartBottom(3, 9600)
            }

        }
    }
}