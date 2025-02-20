package com.ssafy.keywe.presentation.order.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ssafy.keywe.ui.theme.h6sb


@Composable
fun OrderOptionCard(
    title: String,
    imageRes: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .wrapContentHeight()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF5EE)) // 연한 배경색 적용
    ) {
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = imageRes,
                contentDescription = null,
                modifier = Modifier
                    .size(90.dp) // 아이콘 크기 증가
                    .background(Color.Transparent)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                style = h6sb
            )
        }
    }
}

//@Composable
//fun OrderOptionCard(
//    title: String,
//    imageRes: Int,
//    onClick: () -> Unit
//) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(120.dp)
//            .clip(RoundedCornerShape(12.dp))
//            .clickable { onClick() },
//        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF0E6))
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            AsyncImage(
//                model = imageRes,
//                contentDescription = null,
//                modifier = Modifier
//                    .size(56.dp)
//                    .background(Color.Transparent)
//            )
//
//            Spacer(modifier = Modifier.width(16.dp))
//
//            Text(
//                text = title,
//                fontSize = 18.sp,
//                color = Color.Black
//            )
//        }
//    }
//}