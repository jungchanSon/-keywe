package com.ssafy.keywe.presentation.order.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.ssafy.keywe.R

@Composable
fun DefaultMenuImage(modifier: Modifier = Modifier) {
    Image(
        painter = rememberAsyncImagePainter(model = R.drawable.americano),
        contentDescription = "Default Image",
        modifier = modifier
            .width(171.dp)
            .height(100.dp)
            .background(color = Color.Transparent),
        contentScale = ContentScale.Fit
    )
}