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
import com.ssafy.keywe.presentation.order.viewmodel.MenuViewModel

@Composable
fun MenuImage(
    menuId: Long,
    viewModel: MenuViewModel
) {

    val menu = viewModel.getMenuSimpleModelById(menuId)
    val menuImage = menu?.image ?: ""

    Image(
        painter = rememberAsyncImagePainter(model = menuImage),
        contentDescription = "Web Image",
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(color = Color.Transparent),
        contentScale = ContentScale.Fit
    )
}