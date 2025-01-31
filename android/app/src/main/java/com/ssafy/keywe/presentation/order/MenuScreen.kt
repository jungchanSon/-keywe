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
import com.ssafy.keywe.presentation.order.component.Quadruple
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
                Quadruple(
                    "아메리카노",
                    "커피+물",
                    2000,
                    "https://file.notion.so/f/f/6e800ee0-e9da-4766-b834-502cf74dc80f/3d710341-b8ae-46ec-8286-801269d38a40/%EC%B9%B4%ED%8E%98%EB%AA%A8%EC%B9%B4.png?table=block&id=3fcdd36d-5807-4fbc-8588-9b2b60887c37&spaceId=6e800ee0-e9da-4766-b834-502cf74dc80f&expirationTimestamp=1738324800000&signature=01VqrY4SK1LHvxxWAgYzbw7KasHDRBu1AzU9hFER0OI&downloadName=%EC%B9%B4%ED%8E%98%EB%AA%A8%EC%B9%B4.png"
                ),
                Quadruple(
                    "카페라떼",
                    "커피+우유",
                    3000,
                    "https://file.notion.so/f/f/6e800ee0-e9da-4766-b834-502cf74dc80f/3d710341-b8ae-46ec-8286-801269d38a40/%EC%B9%B4%ED%8E%98%EB%AA%A8%EC%B9%B4.png?table=block&id=3fcdd36d-5807-4fbc-8588-9b2b60887c37&spaceId=6e800ee0-e9da-4766-b834-502cf74dc80f&expirationTimestamp=1738324800000&signature=01VqrY4SK1LHvxxWAgYzbw7KasHDRBu1AzU9hFER0OI&downloadName=%EC%B9%B4%ED%8E%98%EB%AA%A8%EC%B9%B4.png"
                ),
                Quadruple(
                    "카푸치노",
                    "커피+거품 우유",
                    3500,
                    "https://file.notion.so/f/f/6e800ee0-e9da-4766-b834-502cf74dc80f/3d710341-b8ae-46ec-8286-801269d38a40/%EC%B9%B4%ED%8E%98%EB%AA%A8%EC%B9%B4.png?table=block&id=3fcdd36d-5807-4fbc-8588-9b2b60887c37&spaceId=6e800ee0-e9da-4766-b834-502cf74dc80f&expirationTimestamp=1738324800000&signature=01VqrY4SK1LHvxxWAgYzbw7KasHDRBu1AzU9hFER0OI&downloadName=%EC%B9%B4%ED%8E%98%EB%AA%A8%EC%B9%B4.png"
                ),
                Quadruple(
                    "모카",
                    "커피+초콜릿+우유",
                    4000,
                    "https://file.notion.so/f/f/6e800ee0-e9da-4766-b834-502cf74dc80f/3d710341-b8ae-46ec-8286-801269d38a40/%EC%B9%B4%ED%8E%98%EB%AA%A8%EC%B9%B4.png?table=block&id=3fcdd36d-5807-4fbc-8588-9b2b60887c37&spaceId=6e800ee0-e9da-4766-b834-502cf74dc80f&expirationTimestamp=1738324800000&signature=01VqrY4SK1LHvxxWAgYzbw7KasHDRBu1AzU9hFER0OI&downloadName=%EC%B9%B4%ED%8E%98%EB%AA%A8%EC%B9%B4.png"
                ),
                Quadruple(
                    "에스프레소",
                    "진한 커피 샷",
                    2500,
                    "https://file.notion.so/f/f/6e800ee0-e9da-4766-b834-502cf74dc80f/3d710341-b8ae-46ec-8286-801269d38a40/%EC%B9%B4%ED%8E%98%EB%AA%A8%EC%B9%B4.png?table=block&id=3fcdd36d-5807-4fbc-8588-9b2b60887c37&spaceId=6e800ee0-e9da-4766-b834-502cf74dc80f&expirationTimestamp=1738324800000&signature=01VqrY4SK1LHvxxWAgYzbw7KasHDRBu1AzU9hFER0OI&downloadName=%EC%B9%B4%ED%8E%98%EB%AA%A8%EC%B9%B4.png"
                ),
                Quadruple(
                    "에스프레소",
                    "진한 커피 샷",
                    2500,
                    "https://file.notion.so/f/f/6e800ee0-e9da-4766-b834-502cf74dc80f/3d710341-b8ae-46ec-8286-801269d38a40/%EC%B9%B4%ED%8E%98%EB%AA%A8%EC%B9%B4.png?table=block&id=3fcdd36d-5807-4fbc-8588-9b2b60887c37&spaceId=6e800ee0-e9da-4766-b834-502cf74dc80f&expirationTimestamp=1738324800000&signature=01VqrY4SK1LHvxxWAgYzbw7KasHDRBu1AzU9hFER0OI&downloadName=%EC%B9%B4%ED%8E%98%EB%AA%A8%EC%B9%B4.png"
                ),
                Quadruple(
                    "에스프레소",
                    "진한 커피 샷",
                    2500,
                    "https://file.notion.so/f/f/6e800ee0-e9da-4766-b834-502cf74dc80f/3d710341-b8ae-46ec-8286-801269d38a40/%EC%B9%B4%ED%8E%98%EB%AA%A8%EC%B9%B4.png?table=block&id=3fcdd36d-5807-4fbc-8588-9b2b60887c37&spaceId=6e800ee0-e9da-4766-b834-502cf74dc80f&expirationTimestamp=1738324800000&signature=01VqrY4SK1LHvxxWAgYzbw7KasHDRBu1AzU9hFER0OI&downloadName=%EC%B9%B4%ED%8E%98%EB%AA%A8%EC%B9%B4.png"
                ),
            )

            MenuMenuList(menuList, navController)
        }
    }
}
