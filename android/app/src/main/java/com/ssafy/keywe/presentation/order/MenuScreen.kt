package com.ssafy.keywe.presentation.order

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ssafy.keywe.R
import com.ssafy.keywe.common.Route
import com.ssafy.keywe.common.app.DefaultAppBar
import com.ssafy.keywe.presentation.order.component.MenuCategoryScreen
import com.ssafy.keywe.presentation.order.component.MenuMenuList
import com.ssafy.keywe.presentation.order.component.MenuSubCategory
import com.ssafy.keywe.presentation.order.viewmodel.MenuViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ssafy.keywe.ui.theme.primaryColor
import com.ssafy.keywe.ui.theme.whiteBackgroundColor

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MenuScreen(
    navController: NavController,
    viewModel: MenuViewModel = hiltViewModel()
) {

    Scaffold(topBar = { DefaultAppBar(title = "주문하기", navController = navController) },
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingCartButton(navController, viewModel)
        }) {
        Column(
            modifier = Modifier.fillMaxHeight()
        ) {
            MenuCategoryScreen()
            MenuSubCategory("Popular Coffee")

            MenuMenuList(
                navController = navController,
                viewModel
            )
        }
    }
}

@Composable
fun FloatingCartButton(navController: NavController, viewModel: MenuViewModel) {
    val cartItemCount by viewModel.cartItemCount.collectAsState()

    Box(
        contentAlignment = Alignment.TopEnd, // 오른쪽 상단 정렬
        modifier = Modifier.size(48.dp) // FloatingActionButton 크기와 동일하게 설정
    ) {
        FloatingActionButton(
            onClick = { navController.navigate(Route.MenuBaseRoute.MenuCartRoute) },
            elevation = FloatingActionButtonDefaults.elevation(Dp(4F)),
            containerColor = primaryColor,
            shape = CircleShape,
            modifier = Modifier.size(48.dp) // FAB 크기 고정
        ) {
            Image(
                painter = painterResource(R.drawable.rounded_shopping_cart_24),
                contentDescription = "Cart Button"
            )
        }

        if (cartItemCount > 0) {
            Box(
                modifier = Modifier
                    .size(10.dp) // 뱃지 크기 조정
                    .offset(x = (-8).dp, y = 4.dp), // FAB 내부 오른쪽 상단에 배치
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = cartItemCount.toString(), color = whiteBackgroundColor, fontSize = 12.sp
                )
            }
        }
    }
}