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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ssafy.keywe.R
import com.ssafy.keywe.common.Route
import com.ssafy.keywe.common.app.DefaultAppBar
import com.ssafy.keywe.presentation.order.component.MenuCategoryScreen
import com.ssafy.keywe.presentation.order.component.MenuMenuList
import com.ssafy.keywe.presentation.order.component.MenuSubCategory
import com.ssafy.keywe.ui.theme.primaryColor
import com.ssafy.keywe.ui.theme.whiteBackgroundColor

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MenuScreen(navController: NavController) {
    val cartItems: List<CartItem> = listOf()
    val totalCartQuantity by remember { derivedStateOf { cartItems.sumOf { it.quantity } } }

    Scaffold(topBar = { DefaultAppBar(title = "주문하기", navController = navController) },
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingCartButton(navController, totalCartQuantity)
        }) {
        Column(
            modifier = Modifier.fillMaxHeight()
//            verticalArrangement = Arrangement.spacedBy(22.dp)
        ) {
            MenuCategoryScreen()
            MenuSubCategory("Popular Coffee")

            val menuList = listOf(
                MenuData(
                    "아메리카노",
                    "커피+물",
                    2000,
                    "https://github.com/Bheinarl/Android-Studio-Study/blob/master/cafemocha.png?raw=true"
                ),
                MenuData(
                    "카페라떼",
                    "커피+우유",
                    3000,
                    "https://github.com/Bheinarl/Android-Studio-Study/blob/master/cafemocha.png?raw=true"
                ),
                MenuData(
                    "카푸치노",
                    "커피+거품 우유",
                    3500,
                    "https://github.com/Bheinarl/Android-Studio-Study/blob/master/cafemocha.png?raw=true"
                ),
                MenuData(
                    "카페모카",
                    "커피+초콜릿+우유",
                    4000,
                    "https://github.com/Bheinarl/Android-Studio-Study/blob/master/cafemocha.png?raw=true"
                ),
                MenuData(
                    "에스프레소",
                    "진한 커피 샷",
                    2500,
                    "https://github.com/Bheinarl/Android-Studio-Study/blob/master/cafemocha.png?raw=true"
                ),
                MenuData(
                    "에스프레소",
                    "진한 커피 샷",
                    2500,
                    "https://github.com/Bheinarl/Android-Studio-Study/blob/master/cafemocha.png?raw=true"
                ),
                MenuData(
                    "에스프레소",
                    "진한 커피 샷",
                    2500,
                    "https://github.com/Bheinarl/Android-Studio-Study/blob/master/cafemocha.png?raw=true"
                ),
            )

            MenuMenuList(menuList, navController)
        }
    }
}

@Composable
fun FloatingCartButton(navController: NavController, cartItemCount: Int) {
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