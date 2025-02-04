package com.ssafy.keywe.presentation.order.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ssafy.keywe.ui.theme.contentTextColor
import com.ssafy.keywe.ui.theme.greyBackgroundColor
import com.ssafy.keywe.ui.theme.subtitle1
import com.ssafy.keywe.ui.theme.subtitle2
import com.ssafy.keywe.ui.theme.whiteBackgroundColor

@Composable
fun MenuCartBottom(amount: Int, price: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp,)
            .background(whiteBackgroundColor)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MenuCartInfo(amount, price)
            MenuCartBottomOrderButton("결제하기", onClick = {})
        }
    }
}


@Composable
fun MenuCartInfo(amount: Int, price: Int) {

    val formattedPrice = String.format("%,d원", price) // 쉼표 포함 형식 지정

    Box(
        modifier = Modifier
            .height(36.dp)
            .background(
                color = greyBackgroundColor,
                shape = RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "총 ${amount}개",
                style = subtitle1,
                color = contentTextColor
            )
            Text(
                text = formattedPrice,
                style = subtitle2,
                color = contentTextColor
            )
        }
    }
}