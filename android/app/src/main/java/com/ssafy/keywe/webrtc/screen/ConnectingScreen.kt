package com.ssafy.keywe.webrtc.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ssafy.keywe.R
import com.ssafy.keywe.common.Route
import com.ssafy.keywe.common.app.DefaultOrderAppBar
import com.ssafy.keywe.presentation.order.viewmodel.MenuCartViewModel
import com.ssafy.keywe.presentation.order.viewmodel.OrderAppBarViewModel
import com.ssafy.keywe.ui.theme.h4
import kotlinx.coroutines.delay

@Composable
fun ConnectingScreen(
    navController: NavController,
    menuCartViewModel: MenuCartViewModel,
    appBarViewModel: OrderAppBarViewModel = hiltViewModel()
){
    LaunchedEffect(Unit) {
        delay(3000) // 3초 대기
        navController.navigate(Route.MenuBaseRoute.MenuRoute) {
            popUpTo(Route.MenuBaseRoute.MenuRoute) { inclusive = false } // 스택 정리 (옵션)
        }
    }

    Scaffold(
        topBar = {
            DefaultOrderAppBar(
                title = "장바구니", navController = navController, viewModel = appBarViewModel
            )
        },
        modifier = Modifier.fillMaxSize()
    ){ innerpadding ->
        Column(modifier = Modifier
            .padding(innerpadding)
            .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center)
        {
            Image(
                painter = painterResource(id = R.drawable.humanimage),
                contentDescription = null,
                modifier = Modifier
                    .size(width = 190.dp, height = 231.dp)
            )

            Box(modifier = Modifier.width(300.dp).height(153.dp), contentAlignment = Alignment.Center){
                Text("김싸피님과\n연결되었습니다.", style= h4, textAlign = TextAlign.Center)
            }
        }
    }
}