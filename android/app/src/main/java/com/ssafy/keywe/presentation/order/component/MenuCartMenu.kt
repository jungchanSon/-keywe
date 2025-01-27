package com.ssafy.keywe.presentation.order.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.ssafy.keywe.R
import com.ssafy.keywe.ui.theme.caption
import com.ssafy.keywe.ui.theme.greyBackgroundColor
import com.ssafy.keywe.ui.theme.subtitle2

@Composable
fun MenuCartMenuBox() {
    Box() {
        Column {
            // x버튼
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.5.dp)
                    .align(Alignment.End)
            ) {
                Image(
                    modifier = Modifier
                        .width(8.5.dp)
                        .height(8.5.dp),
                    painter = painterResource(R.drawable.x),
                    contentDescription = "x"
                )
            }
            //이미지 + 이름 + 가격
        }
    }
}

@Composable
fun MenuCartMenu() {
    Box(modifier = Modifier.fillMaxWidth()) {
        Row {
            Box() {

            }
        }
    }
}

@Composable
fun MenuCartMenuInfo(name: String, price: Int, optionCount: Int) {
    Box(
        modifier = Modifier
            .height(50.dp)
            .width(122.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            Text(name)
            Box(modifier = Modifier) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(18.12.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "${price}원",
                        style = subtitle2.copy(fontWeight = FontWeight.Bold, letterSpacing = 0.em)
                    )
                    VerticalDivider()
                    Text(
                        text = "${optionCount}개의 옵션",
                        style = caption.copy(fontSize = 12.sp, letterSpacing = 0.em),
                        color = greyBackgroundColor

                    )
                }
            }
        }
    }
}