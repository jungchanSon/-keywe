package com.ssafy.keywe.presentation.order.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.ssafy.keywe.ui.theme.h5
import com.ssafy.keywe.ui.theme.lightColor
import com.ssafy.keywe.ui.theme.primaryColor

@Composable
fun MenuDetailMenu(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MenuDetailImage()    // {원 + 이미지}
            Text(
                text = "아이스 아메리카노",
                color = primaryColor,
                style = h5.copy(fontWeight = FontWeight.ExtraBold)
            )
            Text(
                text = "1800 원",
                style = h5.copy(fontWeight = FontWeight.SemiBold)
            )
        }
    }
}

@Composable
fun MenuDetailImage() {
    Box(
        modifier = Modifier
            .size(200.dp) // width와 height를 size로 합침
            .background(color = lightColor, shape = CircleShape) // 원형 배경
            .padding(12.dp) // 내부 여백
    ) {
        val imageUrl = "https://fibercreme.com/wp-content/uploads/2024/10/Sub-1.jpg"
        Image(
            painter = rememberAsyncImagePainter(model = imageUrl),
            contentDescription = "Web Image",
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape), // 이미지를 원형으로 자름
            contentScale = ContentScale.Crop // 이미지를 원에 맞게 잘라냄
        )
    }
}