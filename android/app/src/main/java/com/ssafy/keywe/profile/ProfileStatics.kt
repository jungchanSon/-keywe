package com.ssafy.keywe.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ssafy.keywe.ui.theme.overline
import com.ssafy.keywe.ui.theme.subtitle2

@Composable
fun OrderStaticsBox(
    modifier: Modifier = Modifier,
    orderCount: Int = 39
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFFEF7F4),
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = orderCount.toString(),
                style = subtitle2,
                color = Color(0xFFEE5B22),
            )
            Text(
                text = "Total Orders",
                style = overline,
            )
        }
    }
}
