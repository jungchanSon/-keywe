package com.ssafy.keywe.presentation.order

import android.annotation.SuppressLint
import android.view.MenuItem
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Scaffold
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ssafy.keywe.R
import com.ssafy.keywe.common.app.DefaultAppBar
import com.ssafy.keywe.presentation.order.component.MenuCategoryScreen
import com.ssafy.keywe.presentation.order.component.MenuMenuList
import com.ssafy.keywe.presentation.order.component.MenuSubCategory
import com.ssafy.keywe.presentation.order.component.Spacer
import com.ssafy.keywe.ui.theme.primaryColor

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MenuScreen(navController: NavController) {
    Scaffold(
        topBar = { DefaultAppBar(title = "주문하기", navController = navController) },
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("menuCart") },
                elevation = FloatingActionButtonDefaults.elevation(Dp(4F)),
                containerColor = primaryColor,
                shape = CircleShape
            ) {
                Image(
                    painter = painterResource(R.drawable.rounded_shopping_cart_24),
                    contentDescription = "Cart Button"
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
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
