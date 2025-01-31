package com.ssafy.keywe.presentation.order.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ssafy.keywe.R
import com.ssafy.keywe.ui.theme.greyBackgroundColor
import com.ssafy.keywe.ui.theme.orangeColor

@Composable
fun MenuPlusButton() {
    Box(
        modifier = Modifier
            .size(40.dp) // 버튼 크기 지정
            .graphicsLayer {
                shadowElevation = 16.dp.toPx() // 그림자 크기 더 키움
                shape = CircleShape // 원형 그림자 적용
                clip = false // 클리핑 방지
            }
            .drawBehind {
                drawCircle(
                    color = Color(0xDD000000), // 반투명한 검은색 그림자 (66 = 약 40% 불투명)
                    radius = size.width / 2f + 5.dp.toPx() // 그림자가 더 넓게 퍼지도록 설정
                )
            }
            .background(color = orangeColor, shape = CircleShape), // 원형 버튼
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier
                .size(20.dp), // 이미지 크기 조정
            painter = painterResource(R.drawable.white_plus),
            contentDescription = "white_plus",
        )
    }
}