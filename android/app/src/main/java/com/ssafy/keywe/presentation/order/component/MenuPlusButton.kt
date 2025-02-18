package com.ssafy.keywe.presentation.order.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ssafy.keywe.R
import com.ssafy.keywe.presentation.order.viewmodel.MenuViewModel
import com.ssafy.keywe.ui.theme.orangeColor
import com.ssafy.keywe.webrtc.data.KeyWeButtonEvent
import com.ssafy.keywe.webrtc.viewmodel.KeyWeViewModel

@Composable
fun MenuPlusButton(
    addItem: () -> Unit,
    menuId: Long,
) {

    Box(modifier = Modifier
        .size(40.dp) // 버튼 크기 지정
        .graphicsLayer {
            shadowElevation = 8.dp.toPx() // 적절한 그림자 크기
            shape = CircleShape
            clip = false
        }
        .semantics { contentDescription = "add_to_cart_$menuId" }
        .clickable {
            addItem()
        }
        .background(color = orangeColor, shape = CircleShape), // 원형 버튼
        contentAlignment = Alignment.Center,
        ) {
        Image(
            modifier = Modifier.size(20.dp), // 이미지 크기 조정
            painter = painterResource(R.drawable.white_plus),
            contentDescription = "white_plus",
        )
    }
}