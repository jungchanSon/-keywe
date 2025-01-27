package com.ssafy.keywe.presentation.order.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssafy.keywe.ui.theme.polishedSteelColor
import com.ssafy.keywe.ui.theme.pretendardkr
import com.ssafy.keywe.ui.theme.titleTextColor

@Composable
fun MenuDescription(name: String, recipe: String, price: Int) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Box(
                modifier = Modifier
//                    .fillMaxSize()
                    .fillMaxHeight()
                    .padding(horizontal = 6.dp)
            ) {
                Column(
                    modifier = Modifier
//                        .fillMaxSize(),
                        .fillMaxHeight(),
//                        .padding(bottom = 1.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.Start,
                ) {
                    Box(
                        modifier = Modifier
//                            .fillMaxSize()
//                            .fillMaxHeight()
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.Top),
                            horizontalAlignment = Alignment.Start,
                        ) {
                            Text(
                                text = name,
                                style = TextStyle(
                                    fontSize = 15.sp,
                                    fontFamily = pretendardkr,
                                    fontWeight = FontWeight.Bold,
                                    color = titleTextColor
                                )
                            )
                            Text(
                                text = recipe,
                                style = TextStyle(
                                    fontSize = 13.sp,
                                    fontFamily = pretendardkr,
                                    fontWeight = FontWeight.Normal,
                                    color = polishedSteelColor
                                )
                            )
                        }
                    }
                    Text("${price}원")
                }
            }
            MenuPlusButton()
        }
    }
}
