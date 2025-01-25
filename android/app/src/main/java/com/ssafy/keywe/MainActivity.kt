package com.ssafy.keywe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.ssafy.keywe.common.app.BottomButton
import com.ssafy.keywe.common.app.DefaultAppBar
import com.ssafy.keywe.common.app.DefaultDialog
import com.ssafy.keywe.common.app.DefaultModalBottomSheet
import com.ssafy.keywe.common.app.DefaultTextFormField
import com.ssafy.keywe.common.order.HorizontalDivider
import com.ssafy.keywe.common.order.MenuCategory
import com.ssafy.keywe.common.order.MenuCategoryScreen
import com.ssafy.keywe.common.order.MenuDescription
import com.ssafy.keywe.common.order.MenuMenu
import com.ssafy.keywe.common.order.MenuMenuList
import com.ssafy.keywe.common.order.MenuMenuScreen
import com.ssafy.keywe.common.order.MenuSubCategory
import com.ssafy.keywe.common.order.Quadruple
import com.ssafy.keywe.ui.theme.KeyWeTheme
import com.ssafy.keywe.ui.theme.primaryColor

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KeyWeTheme {
                MenuScreen()
            }
        }
    }
}

@Composable
fun MenuDetailScreen() {
    Scaffold(
        topBar = { DefaultAppBar(title = "주문하기") }, modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.Top
        ) {
        }
    }
}

@Composable
fun MenuScreen() {
    Scaffold(
        topBar = { DefaultAppBar(title = "주문하기") }, modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {},
                elevation = FloatingActionButtonDefaults.elevation(Dp(0F)),
                containerColor = primaryColor,
                shape = CircleShape
            ) {
                Image(
                    modifier = Modifier,
                    painter = painterResource(R.drawable.rounded_shopping_cart_24),
                    contentDescription = "Cart Button"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(bottom = 32.dp)
        ) {
            MenuCategoryScreen()
            MenuSubCategory("Popular Coffee")

            val menuList = listOf(
                Quadruple(
                    "아메리카노",
                    "커피+물",
                    2000,
                    "https://file.notion.so/f/f/6e800ee0-e9da-4766-b834-502cf74dc80f/f674efcc-0102-4645-ad48-801e111e90a8/%EC%95%84%EB%A9%94%EB%A6%AC%EC%B9%B4%EB%85%B8.png?table=block&id=b022cea1-d75a-4644-85c2-839109a6113a&spaceId=6e800ee0-e9da-4766-b834-502cf74dc80f&expirationTimestamp=1737720000000&signature=jOqHikoR8etsHVDbV5vIQ3J7Z2qu6sFu_8k6SlqCq6A&downloadName=%EC%95%84%EB%A9%94%EB%A6%AC%EC%B9%B4%EB%85%B8.png"
                ),
                Quadruple(
                    "카페라떼",
                    "커피+우유",
                    3000,
                    "https://file.notion.so/f/f/6e800ee0-e9da-4766-b834-502cf74dc80f/f674efcc-0102-4645-ad48-801e111e90a8/%EC%95%84%EB%A9%94%EB%A6%AC%EC%B9%B4%EB%85%B8.png?table=block&id=b022cea1-d75a-4644-85c2-839109a6113a&spaceId=6e800ee0-e9da-4766-b834-502cf74dc80f&expirationTimestamp=1737720000000&signature=jOqHikoR8etsHVDbV5vIQ3J7Z2qu6sFu_8k6SlqCq6A&downloadName=%EC%95%84%EB%A9%94%EB%A6%AC%EC%B9%B4%EB%85%B8.png"
                ),
                Quadruple(
                    "카푸치노",
                    "커피+거품 우유",
                    3500,
                    "https://file.notion.so/f/f/6e800ee0-e9da-4766-b834-502cf74dc80f/f674efcc-0102-4645-ad48-801e111e90a8/%EC%95%84%EB%A9%94%EB%A6%AC%EC%B9%B4%EB%85%B8.png?table=block&id=b022cea1-d75a-4644-85c2-839109a6113a&spaceId=6e800ee0-e9da-4766-b834-502cf74dc80f&expirationTimestamp=1737720000000&signature=jOqHikoR8etsHVDbV5vIQ3J7Z2qu6sFu_8k6SlqCq6A&downloadName=%EC%95%84%EB%A9%94%EB%A6%AC%EC%B9%B4%EB%85%B8.png"
                ),
                Quadruple(
                    "모카",
                    "커피+초콜릿+우유",
                    4000,
                    "https://file.notion.so/f/f/6e800ee0-e9da-4766-b834-502cf74dc80f/f674efcc-0102-4645-ad48-801e111e90a8/%EC%95%84%EB%A9%94%EB%A6%AC%EC%B9%B4%EB%85%B8.png?table=block&id=b022cea1-d75a-4644-85c2-839109a6113a&spaceId=6e800ee0-e9da-4766-b834-502cf74dc80f&expirationTimestamp=1737720000000&signature=jOqHikoR8etsHVDbV5vIQ3J7Z2qu6sFu_8k6SlqCq6A&downloadName=%EC%95%84%EB%A9%94%EB%A6%AC%EC%B9%B4%EB%85%B8.png"
                ),
                Quadruple(
                    "에스프레소",
                    "진한 커피 샷",
                    2500,
                    "https://file.notion.so/f/f/6e800ee0-e9da-4766-b834-502cf74dc80f/f674efcc-0102-4645-ad48-801e111e90a8/%EC%95%84%EB%A9%94%EB%A6%AC%EC%B9%B4%EB%85%B8.png?table=block&id=b022cea1-d75a-4644-85c2-839109a6113a&spaceId=6e800ee0-e9da-4766-b834-502cf74dc80f&expirationTimestamp=1737720000000&signature=jOqHikoR8etsHVDbV5vIQ3J7Z2qu6sFu_8k6SlqCq6A&downloadName=%EC%95%84%EB%A9%94%EB%A6%AC%EC%B9%B4%EB%85%B8.png"
                ),
                Quadruple(
                    "에스프레소",
                    "진한 커피 샷",
                    2500,
                    "https://file.notion.so/f/f/6e800ee0-e9da-4766-b834-502cf74dc80f/f674efcc-0102-4645-ad48-801e111e90a8/%EC%95%84%EB%A9%94%EB%A6%AC%EC%B9%B4%EB%85%B8.png?table=block&id=b022cea1-d75a-4644-85c2-839109a6113a&spaceId=6e800ee0-e9da-4766-b834-502cf74dc80f&expirationTimestamp=1737720000000&signature=jOqHikoR8etsHVDbV5vIQ3J7Z2qu6sFu_8k6SlqCq6A&downloadName=%EC%95%84%EB%A9%94%EB%A6%AC%EC%B9%B4%EB%85%B8.png"
                ),
                Quadruple(
                    "에스프레소",
                    "진한 커피 샷",
                    2500,
                    "https://file.notion.so/f/f/6e800ee0-e9da-4766-b834-502cf74dc80f/f674efcc-0102-4645-ad48-801e111e90a8/%EC%95%84%EB%A9%94%EB%A6%AC%EC%B9%B4%EB%85%B8.png?table=block&id=b022cea1-d75a-4644-85c2-839109a6113a&spaceId=6e800ee0-e9da-4766-b834-502cf74dc80f&expirationTimestamp=1737720000000&signature=jOqHikoR8etsHVDbV5vIQ3J7Z2qu6sFu_8k6SlqCq6A&downloadName=%EC%95%84%EB%A9%94%EB%A6%AC%EC%B9%B4%EB%85%B8.png"
                ),
            )


            MenuMenuList(menuList)
        }
    }
}
