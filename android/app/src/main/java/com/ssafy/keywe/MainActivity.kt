package com.ssafy.keywe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ssafy.keywe.common.order.HorizontalDivider
import com.ssafy.keywe.common.order.Menu
import com.ssafy.keywe.common.order.MenuCategoryScreen
import com.ssafy.keywe.ui.theme.KeyWeTheme
import com.ssafy.keywe.ui.theme.greyBackgroundColor

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KeyWeTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background
                ) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        HorizontalDivider()
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(87.dp)
                                .background(color = greyBackgroundColor)
                                .padding(horizontal = 24.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp) // 버튼 간격 조정
                            ) {
                                Box(modifier = Modifier.weight(1f)) {
                                    MenuCategoryScreen("커피")
                                }
                                Box(modifier = Modifier.weight(1f)) {
                                    MenuCategoryScreen("차")
                                }
                                Box(modifier = Modifier.weight(1f)) {
                                    MenuCategoryScreen("에이드")
                                }
                                Box(modifier = Modifier.weight(1f)) {
                                    MenuCategoryScreen("주스")
                                }
                            }
                        }
                        HorizontalDivider()
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .padding(horizontal = 16.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                                    .fillMaxHeight(),
                                horizontalArrangement = Arrangement.spacedBy(
                                    0.dp,
                                    Alignment.CenterHorizontally
                                ),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Box(modifier = Modifier.weight(1f)) {
                                    Menu("아메리카노", 2000)
                                }
                                Box(modifier = Modifier.weight(1f)) {
                                    Menu("카페라떼", 3000)
                                }
                            }
                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                                    .fillMaxHeight(),
                                horizontalArrangement = Arrangement.spacedBy(
                                    0.dp,
                                    Alignment.CenterHorizontally
                                ),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Box(modifier = Modifier.weight(1f)) {
                                    Menu("바닐라라떼", 2500)
                                }
                                Box(modifier = Modifier.weight(1f)) {
                                    Menu("카페모카", 4000)
                                }
                            }
                        }
//                        OptionAmount(amount = 3)
                    }
                }
            }
        }
    }
}

