package com.ssafy.keywe.common.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ssafy.keywe.ui.theme.greyBackgroundColor

@Composable
fun Spacer(height:Int){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height.dp)
            .background(color = greyBackgroundColor)
    )
}