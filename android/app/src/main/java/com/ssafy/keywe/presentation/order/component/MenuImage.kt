package com.ssafy.keywe.presentation.order.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun MenuImage(imageURL: String) {
//    val imageURL = "https://img.freepik.com/free-photo/vertical-closeup-glass-ice-tea-table-lights-against-white-background_181624-22315.jpg?semt=ais_hybrid"

    Image(
        painter = rememberAsyncImagePainter(model = imageURL),
        contentDescription = "Web Image",
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(color = Color.Transparent),
        contentScale = ContentScale.Fit
    )
}