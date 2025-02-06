package com.ssafy.keywe.presentation.order.component

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ssafy.keywe.ui.theme.polishedSteelColor
import androidx.compose.material3.HorizontalDivider as MaterialHorizonDivider
import androidx.compose.material3.VerticalDivider as MaterialVerticalDivider

@Composable
fun HorizontalDivider(
    thickness: Dp = 1.dp,
    color: Color = polishedSteelColor,
) {
    MaterialHorizonDivider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        thickness = thickness,
        color = color
    )
}

@Composable
fun VerticalDivider(
    thickness: Dp = 1.dp,
    color: Color = polishedSteelColor,
) {
    MaterialVerticalDivider(
        modifier = Modifier
            .fillMaxHeight(),
        thickness = thickness,
        color = color
    )
}