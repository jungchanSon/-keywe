package com.ssafy.keywe.presentation.order.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.ssafy.keywe.R

@Composable
fun MenuBackgroundRactangle() {
    Image(
        modifier = Modifier
            .width(20.dp)
            .height(20.dp)
            .zIndex(1f),
        painter = painterResource(R.drawable.menu_background_rectangle),
        contentDescription = "checkbox"
    )
}