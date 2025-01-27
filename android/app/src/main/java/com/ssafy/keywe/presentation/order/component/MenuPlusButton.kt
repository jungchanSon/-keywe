package com.ssafy.keywe.presentation.order.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ssafy.keywe.R
import com.ssafy.keywe.ui.theme.greyBackgroundColor
import com.ssafy.keywe.ui.theme.orangeColor

@Composable
fun MenuPlusButton() {
    Box(
        modifier = Modifier
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(size = 1000.dp),
                spotColor = greyBackgroundColor,
                ambientColor = greyBackgroundColor
            )
            .width(30.dp)
            .height(30.dp)
            .background(color = orangeColor, shape = RoundedCornerShape(size = 1000.dp)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            painter = painterResource(R.drawable.white_plus),
            contentDescription = "white_plus",
        )
    }
}